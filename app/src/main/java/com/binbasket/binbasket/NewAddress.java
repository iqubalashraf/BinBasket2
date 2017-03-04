package com.binbasket.binbasket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
 * Created by ashrafiqubal on 04/06/16.
 */
public class NewAddress extends AppCompatActivity {
    private ProgressDialog pDialog;
    String serverResponse = null;
    static SharedPreferences sharedpreferences;
    EditText editText;
    String fullName,mobileNo,pincode,address,landmark;
    String selectedItemsInSTring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_address);
        Intent intent = getIntent();
        selectedItemsInSTring = intent.getStringExtra(MainActivity.SELECTEDITEMS);
        Log.d("New Address://", "Line Executed"+selectedItemsInSTring);
    }
    public void addAddress(View view){
        editText = (EditText)findViewById(R.id.fullName);
        fullName = editText.getText().toString();
        editText = (EditText)findViewById(R.id.mobileNo);
        mobileNo = editText.getText().toString();
        editText = (EditText)findViewById(R.id.pincode);
        pincode = editText.getText().toString();
        editText = (EditText)findViewById(R.id.addresslineone);
        address = editText.getText().toString();
        editText = (EditText)findViewById(R.id.addresslinetwo);
        address = address+" "+editText.getText().toString();
        editText = (EditText)findViewById(R.id.landmark);
        landmark = editText.getText().toString();
        if(fullName.length()<4){
            Toast.makeText(getApplicationContext(),"Invalid Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mobileNo.length()!=10){
            Toast.makeText(getApplicationContext(),"Invalid MOBILE NO",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pincode.length()!=6){
            Toast.makeText(getApplicationContext(),"Invalid PINCOCE",Toast.LENGTH_SHORT).show();
            return;
        }
        if(address.length()<8){
            Toast.makeText(getApplicationContext(),"Invalid ADDRESS",Toast.LENGTH_SHORT).show();
            return;
        }
        sharedpreferences = getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);
        String userPhoneNo = sharedpreferences.getString(MainActivity.USERMOBILENO,null);
        String userPass = sharedpreferences.getString(MainActivity.USERPASS,null);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(MainActivity.SERVERIPADDRESS)
                .appendPath("binbasket")
                .appendPath("addaddress.jsp")
                .appendQueryParameter("USERPHONENO",userPhoneNo )
                .appendQueryParameter("PASSWORD",userPass)
                .appendQueryParameter("FULLNAME",fullName)
                .appendQueryParameter("MOBILENO",mobileNo)
                .appendQueryParameter("PINCODE",pincode)
                .appendQueryParameter("ADDRESS",address)
                .appendQueryParameter("LANDMARK",landmark)
                .appendQueryParameter("SELECTEDITEMS",selectedItemsInSTring);
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A", ":");
        Log.d("URL://",myUrl);
        AddAddress addAddress = new AddAddress();
        addAddress.execute(myUrl);
    }
    public class AddAddress extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            pDialog = new ProgressDialog(NewAddress.this);
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
                    Log.d("SingUp: ", str);
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
            Log.d("SingUp:// ", "onPostExecution ");
            Object obj = JSONValue.parse(serverResponse);
            JSONObject jsonObject = (JSONObject)obj;
            String status = (String)jsonObject.get("STATUS");
            if(status.equals("0")){
                Log.d("SingUp:", "Registration Successful");
                Toast.makeText(getApplicationContext(),"Successful ! Order placed successfully.",Toast.LENGTH_SHORT).show();
                //sharedpreferences = getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);
                Intent AddressActivity = new Intent(NewAddress.this, MainActivity.class);
                startActivity(AddressActivity);
                finish();
            }
            if(status.equals("1")){
                Toast.makeText(getApplicationContext(),"Phone no already exists",Toast.LENGTH_SHORT).show();
                Intent AddressActivity = new Intent(NewAddress.this, MainActivity.class);
                startActivity(AddressActivity);
                finish();
            }
            if(status.equals("2")){
                Log.d("SingUp:","Registration Successful");
                Toast.makeText(getApplicationContext(),"Invalid details",Toast.LENGTH_SHORT).show();
                Intent AddressActivity = new Intent(NewAddress.this, MainActivity.class);
                startActivity(AddressActivity);
                finish();
            }
            if(status.equals("3")){
                Log.d("SingUp:","Registration Successful");
                Toast.makeText(getApplicationContext(),"Something went wrong, Try again !",Toast.LENGTH_SHORT).show();
                Intent AddressActivity = new Intent(NewAddress.this, MainActivity.class);
                startActivity(AddressActivity);
                finish();
            }
            if(status.equals("3Duplicate entry '9652356235' for key 'PRIMARY'")){
                Toast.makeText(getApplicationContext(),"You have already a Active order !",Toast.LENGTH_SHORT).show();
                Intent AddressActivity = new Intent(NewAddress.this, MainActivity.class);
                startActivity(AddressActivity);
                finish();
            }
        }
    }
    @Override
    public void onBackPressed(){
        Intent mainActivity = new Intent(NewAddress.this,MainActivity.class);
        startActivity(mainActivity);
        finish();
        Log.d("CDA", "onBackPressed Called");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}


