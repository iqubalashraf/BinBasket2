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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashrafiqubal on 15/06/16.
 */
public class AddressActivity extends Activity {
    private ProgressDialog pDialog;
    private static RecyclerView mRecyclerView;
    private static CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String serverResponse = null;
    static SharedPreferences sharedpreferences;
    public static final int ADDRESSVIEW = 2;
    public static final int BUTTONVIEW = 3;
    public static String selectedAddress=null;
    String customerPasswordString;
    String customerMobileNoString;
    public static String selectedItemsInSTring;
    private static String[] address_list;
    private static int[] dataType;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AddressActivity://", "Line Executed");
        setContentView(R.layout.address_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.pickaddress);
        Intent intent = getIntent();
        context = this;
        selectedItemsInSTring = intent.getStringExtra(MainActivity.SELECTEDITEMS);
        sharedpreferences = getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);
        customerPasswordString = sharedpreferences.getString(MainActivity.USERPASS, null);
        customerMobileNoString = sharedpreferences.getString(MainActivity.USERMOBILENO,null);
        Log.d("AddressActivity://", "Line Executed");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(MainActivity.SERVERIPADDRESS)
                .appendPath("binbasket")
                .appendPath("fetchaddress.jsp")
                .appendQueryParameter("USERPHONENO", customerMobileNoString)
                .appendQueryParameter("PASSWORD", customerPasswordString);
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A", ":");
        Log.d("URL://",myUrl);
        FetchAddress fetchAddress = new FetchAddress();
        fetchAddress.execute(myUrl);
    }
    public void nextToConformationPage(View view){
        if(selectedAddress==null){
            Toast.makeText(getApplicationContext(),"Please select a address first",Toast.LENGTH_SHORT).show();
        }else {
            Intent conformationActivity = new Intent(AddressActivity.this,ConformationActivity.class);
            conformationActivity.putExtra(MainActivity.SELECTEDITEMS, selectedItemsInSTring);
            conformationActivity.putExtra("CONFORMEDADDRESS", selectedAddress);
            startActivity(conformationActivity);
        }
    }

    public class FetchAddress extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            pDialog = new ProgressDialog(AddressActivity.this);
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
                //Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                int maxInt=0;
                try{
                    Long max = (Long)jsonObject.get("MAX");
                    maxInt = ((int)(long)max);
                    Log.d("No error: ", Integer.toString(maxInt));
                    int i=0;
                    address_list= new String[maxInt+1];
                    dataType = new int[maxInt+1];
                    //Integer[] dataType = new Integer[maxInt];
                    for(;i<maxInt;i++){
                        String fullName = (String)jsonObject.get("FULLNAME"+Integer.toString(i));
                        String mobileNo = (String)jsonObject.get("MOBILENO"+Integer.toString(i));
                        String pincode = (String)jsonObject.get("PINCODE"+Integer.toString(i));
                        String address = (String)jsonObject.get("ADDRESS"+Integer.toString(i));
                        String landmark = (String)jsonObject.get("LANDMARK" + Integer.toString(i));
                        String finalAddress = fullName+"\n"+address+"\n"+landmark+"  Pincode:"+pincode+"\nMobile No:"+mobileNo;
                        address_list[i]=finalAddress;
                        dataType[i]=ADDRESSVIEW;
                        //Adapter is created in the last step
                    }
                    address_list[maxInt]="Add New Address";
                    dataType[maxInt]=BUTTONVIEW;
                }catch (Exception e){
                    Log.d("Error: ", e.getMessage());
                }
                mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                mLayoutManager = new LinearLayoutManager(AddressActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);
                updateUI();
            }
            if(status.equals("1")){
                Toast.makeText(getApplicationContext(),"Phone no already exists",Toast.LENGTH_SHORT).show();
            }
            if(status.equals("2")){
                sharedpreferences = getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(MainActivity.ISNEWUSER,true );
                editor.commit();
                Toast.makeText(getApplicationContext(),"Something went wrong, Try Again",Toast.LENGTH_SHORT).show();
            }
            if(status.equals("3")){
                Toast.makeText(getApplicationContext(),"Something went wrong, Try Again !",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void addNewAddress(View view){
        Intent AddressActivity = new Intent(AddressActivity.this, NewAddress.class);
        AddressActivity.putExtra(MainActivity.SELECTEDITEMS,selectedItemsInSTring);
        startActivity(AddressActivity);
        finish();
    }
    public static void updateUI(){
        mAdapter = new CustomAdapter(address_list, dataType);
        mRecyclerView.setAdapter(mAdapter);
    }
    /*public void confromationActivity (){
        Intent conformationActivity = new Intent(context,ConformationActivity.class);
        conformationActivity.putExtra(MainActivity.SELECTEDITEMS, selectedItemsInSTring);
        conformationActivity.putExtra("CONFORMEDADDRESS",selectedAddress);
        startActivity(conformationActivity);
    }*/
    @Override
    public void onBackPressed(){
        Intent mainActivity = new Intent(AddressActivity.this,MainActivity.class);
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


