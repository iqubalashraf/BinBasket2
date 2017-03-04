package com.binbasket.binbasket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
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
 * Created by ashrafiqubal on 02/06/16.
 */
public class LogIn extends AppCompatActivity {
    private ProgressDialog pDialog;
    String serverResponse=null;
    EditText customerMobileNo ,customerPassword;
    static SharedPreferences sharedpreferences;
    String customerMobileNoString,customerPasswordString,selectedItemsInSTring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        Intent intent = getIntent();
        selectedItemsInSTring = intent.getStringExtra(MainActivity.SELECTEDITEMS);
        customerMobileNo = (EditText)findViewById(R.id.customer_mobile_no);
        customerPassword = (EditText)findViewById(R.id.customer_password);
        Log.d("Sing Up://", "Line Executed");
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/
    public void logIn(View view){
        customerMobileNoString = customerMobileNo.getText().toString();
        if(customerMobileNoString.length()<10){
            Toast.makeText(getApplicationContext(),"Enter a valid Mobile no",Toast.LENGTH_SHORT).show();
            return;
        }
        customerPasswordString = customerPassword.getText().toString();
        if(customerPasswordString.length()<4){
            Toast.makeText(getApplicationContext(),"Password lenght should be greater than 4 ",Toast.LENGTH_SHORT).show();
            return;
        }
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(MainActivity.SERVERIPADDRESS)
                .appendPath("binbasket")
                .appendPath("customerLogin.jsp")
                .appendQueryParameter("USERPHONENO", customerMobileNoString)
                .appendQueryParameter("PASSWORD",customerPasswordString);
        String myUrl = builder.build().toString();
        myUrl = myUrl.replace("%3A", ":");
        Log.d("URL://",myUrl);
        ProcessRequest processRequest = new ProcessRequest();
        processRequest.execute(myUrl);
    }
    public class ProcessRequest extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            pDialog = new ProgressDialog(LogIn.this);
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
                Log.d("SingUp:","Login Successful");
                Toast.makeText(getApplicationContext(),"Log in Successfully",Toast.LENGTH_SHORT).show();
                sharedpreferences = getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(MainActivity.ISNEWUSER, false);
                editor.putString(MainActivity.USERMOBILENO, customerMobileNoString);
                editor.putString(MainActivity.USERPASS, customerPasswordString);
                editor.commit();
                if(selectedItemsInSTring.equals("MYORDERS") || selectedItemsInSTring.equals("MYACCOUNT") || selectedItemsInSTring.equals("SINGUP")){
                    Intent AddressActivity = new Intent(LogIn.this, MainActivity.class);
                    startActivity(AddressActivity);
                    finish();
                }else {
                    Intent AddressActivity = new Intent(LogIn.this, AddressActivity.class);
                    AddressActivity.putExtra(MainActivity.SELECTEDITEMS, selectedItemsInSTring);
                    startActivity(AddressActivity);
                    finish();
                }
            }
            if(status.equals("1")){
                Toast.makeText(getApplicationContext(),"Phone no not exists",Toast.LENGTH_SHORT).show();
            }
            if(status.equals("2")){
                Log.d("SingUp:","Registration Successful");
                Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
            }
            if(status.equals("3")){
                Log.d("SingUp:","Registration Successful");
                Toast.makeText(getApplicationContext(),"Something went wrong, Try again !",Toast.LENGTH_SHORT).show();
            }
            if(status.equals("4")){
                Log.d("SingUp:","Registration Successful");
                Toast.makeText(getApplicationContext(),"Something went wrong, Try again !",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void forgotPassword(View view){
        Toast.makeText(getApplicationContext(), "Please call our customer care to reset your password", Toast.LENGTH_SHORT).show();
    }
    public void joinToday(View view){
        Intent signUp = new Intent(LogIn.this,SingUp.class);
        startActivity(signUp);
        finish();
    }
    public void skipItIwillDoItLater(View view){
        //Toast.makeText(getApplicationContext(),"Not implemented yet",Toast.LENGTH_SHORT).show();
        Intent mainActivity = new Intent(LogIn.this,MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
    @Override
    public void onBackPressed(){
        Intent mainActivity = new Intent(LogIn.this,MainActivity.class);
        startActivity(mainActivity);
        finish();
        Log.d("CDA", "onBackPressed Called");
    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}


