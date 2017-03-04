package com.binbasket.binbasket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ashrafiqubal on 19/06/16.
 */
public class ConformationActivity extends Activity {
    private ProgressDialog pDialog;
    String serverResponse = null;
    static SharedPreferences sharedpreferences;
    public String selectedAddress=null;
    String customerPasswordString;
    String customerMobileNoString;
    private String selectedItemsInSTring=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AddressActivity://", "Line Executed");
        setContentView(R.layout.conformation_page);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.addressconformation);
        selectedItemsInSTring = intent.getStringExtra(MainActivity.SELECTEDITEMS);
        selectedAddress = intent.getStringExtra("CONFORMEDADDRESS");
        sharedpreferences = getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);
        customerPasswordString = sharedpreferences.getString(MainActivity.USERPASS, null);
        customerMobileNoString = sharedpreferences.getString(MainActivity.USERMOBILENO,null);
        TextView textView = (TextView)findViewById(R.id.selectedItems);
        String tempItems = selectedItemsInSTring.replace("[","");
        tempItems = tempItems.replace("]","");
        tempItems = tempItems.replace(", ","\n");
        textView.setText(tempItems);
        TextView textView1 = (TextView)findViewById(R.id.selectedAddress);
        textView1.setText(selectedAddress);
    }
    public void placeOrder(View view){
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(MainActivity.SERVERIPADDRESS)
                    .appendPath("binbasket")
                    .appendPath("PlaceOrder.jsp")
                    .appendQueryParameter("USERPHONENO", customerMobileNoString)
                    .appendQueryParameter("PASSWORD", customerPasswordString)
                    .appendQueryParameter("ADDRESS",selectedAddress)
                    .appendQueryParameter("SELECTEDITEMS",selectedItemsInSTring);
            String myUrl = builder.build().toString();
            myUrl = myUrl.replace("%3A", ":");
            Log.d("URL://",myUrl);
            PlaceOrder placeOrder1 = new PlaceOrder();
            placeOrder1.execute(myUrl);
    }
    public class PlaceOrder extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            pDialog = new ProgressDialog(ConformationActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {
                String str;
                HttpClient myClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                HttpResponse myResponse = myClient.execute(get);
                BufferedReader br = new BufferedReader(new InputStreamReader(myResponse.getEntity().getContent()));
                while ((str = br.readLine()) != null) {
                    serverResponse = str;
                    Log.d("Address Activity: ", str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            prepared = true;
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            Log.d("Address Activity: ", "onPostExecution ");
            Object obj = JSONValue.parse(serverResponse);
            JSONObject jsonObject = (JSONObject)obj;
            String status = (String)jsonObject.get("STATUS");
            if(status.equals("0")){
                Log.d("Address:", "Fetch Successfully");
                Toast.makeText(getApplicationContext(), "Order Place Successfully", Toast.LENGTH_SHORT).show();
                Intent mainActivity = new Intent(ConformationActivity.this,MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
            if(status.equals("1")){
                Toast.makeText(getApplicationContext(),"Phone no already exists",Toast.LENGTH_SHORT).show();
            }
            if(status.equals("2")){
                Log.d("SingUp:","Registration Successful");
                Toast.makeText(getApplicationContext(),"Invalid details",Toast.LENGTH_SHORT).show();
            }
            if(status.equals("3")){
                Log.d("SingUp:","Registration Successful");
                Toast.makeText(getApplicationContext(),"You already have a Active Order !",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent addressActivity = new Intent(ConformationActivity.this,MainActivity.class);
        startActivity(addressActivity);
        finish();
    }
}
