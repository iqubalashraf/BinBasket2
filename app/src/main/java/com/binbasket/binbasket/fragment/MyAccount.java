package com.binbasket.binbasket.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.binbasket.binbasket.*;

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
 * Created by ashrafiqubal on 26/06/16.
 */
public class MyAccount extends Fragment{
    private static final String TAG = "MyAccount";
    private static EditText firstName,lastName,email,password;
    private static TextView mobileNo;
    private static RadioButton changePassword;
    private static String serverResponse=null;
    private static FrameLayout frameLayout;
    private static Button updateButton;
    View rootView;
    static String customerPassword;
    SharedPreferences sharedpreferences;
    private ProgressDialog pDialog;
    private static int check;
    private static Boolean isSelected=false;
    public MyAccount(){
        check=0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_account, container, false);
        Log.d("Home Fragment://", "Line Executed");
        //initializeBooleans();
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        try{
            isSelected=false;
            firstName = (EditText)getView().findViewById(R.id.customer_first_name);
            lastName = (EditText)getView().findViewById(R.id.customer_last_name);
            mobileNo = (TextView)getView().findViewById(R.id.customer_mobile_no);
            email = (EditText)getView().findViewById(R.id.customer_email_address);
            password = (EditText)getView().findViewById(R.id.customer_password);
            changePassword = (RadioButton)getView().findViewById(R.id.radio_change_password);
            frameLayout = (FrameLayout)getView().findViewById(R.id.framepassword);
            updateButton = (Button)getView().findViewById(R.id.updateButtonMyAccount);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDetails();
                }
            });
            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Button Pressed");
                    if(!isSelected){
                        frameLayout.setVisibility(View.VISIBLE);
                        changePassword.setSelected(true);
                        isSelected=true;
                    }else {
                        frameLayout.setVisibility(View.GONE);
                        // changePassword.clearFocus();
                        //changePassword.setSelected(false);
                        //changePassword.refreshDrawableState();
                        //changePassword.
                        isSelected=false;
                    }

                }
            });
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(MainActivity.SERVERIPADDRESS)
                    .appendPath("binbasket")
                    .appendPath("MyAccount.jsp")
                    .appendQueryParameter("USERPHONENO", MainActivity.customerMobileNoString)
                    .appendQueryParameter("PASSWORD", MainActivity.customerPasswordString);
            String myUrl = builder.build().toString();
            myUrl = myUrl.replace("%3A", ":");
            Log.d("URL://", myUrl);
            MyAccountDetails myAccountDetails = new MyAccountDetails();
            myAccountDetails.execute(myUrl);
        }catch (Exception e){
            Toast.makeText(MainActivity.context,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void updateDetails(){
        try{
            String customerFirstName = firstName.getText().toString();
            String customerLastName = lastName.getText().toString();
            String customerEmail = email.getText().toString();
            if(isSelected){
                customerPassword = password.getText().toString();
                if(customerPassword.length()<8){
                    Toast.makeText(MainActivity.context,"Password very short",Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority(MainActivity.SERVERIPADDRESS)
                        .appendPath("binbasket")
                        .appendPath("updatePassDetails.jsp")
                        .appendQueryParameter("USERPHONENO", MainActivity.customerMobileNoString)
                        .appendQueryParameter("PASSWORD", MainActivity.customerPasswordString)
                        .appendQueryParameter("FIRSTNAME",customerFirstName)
                        .appendQueryParameter("LASTNAME",customerLastName)
                        .appendQueryParameter("EMAIL",customerEmail)
                        .appendQueryParameter("NEWPASS",customerPassword);
                String myUrl = builder.build().toString();
                myUrl = myUrl.replace("%3A", ":");
                Log.d("URL://", myUrl);
                check=2;
                MyAccountDetails myAccountDetails = new MyAccountDetails();
                myAccountDetails.execute(myUrl);
            }else {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority(MainActivity.SERVERIPADDRESS)
                        .appendPath("binbasket")
                        .appendPath("updateDetails.jsp")
                        .appendQueryParameter("USERPHONENO", MainActivity.customerMobileNoString)
                        .appendQueryParameter("PASSWORD", MainActivity.customerPasswordString)
                        .appendQueryParameter("FIRSTNAME",customerFirstName)
                        .appendQueryParameter("LASTNAME",customerLastName)
                        .appendQueryParameter("EMAIL",customerEmail);
                String myUrl = builder.build().toString();
                myUrl = myUrl.replace("%3A", ":");
                Log.d("URL://", myUrl);
                check=1;
                MyAccountDetails myAccountDetails = new MyAccountDetails();
                myAccountDetails.execute(myUrl);
            }
        }catch (Exception e){
            Toast.makeText(MainActivity.context,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    public class MyAccountDetails extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                pDialog = new ProgressDialog(MainActivity.context);
                pDialog.setMessage("Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }catch (Exception e){
                Toast.makeText(MainActivity.context,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
           /* try{

            }catch (Exception e){
                Toast.makeText(MainActivity.context,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }*/
            Boolean prepared;
            //progress.show();
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
            try {
                Log.d("Address Activity: ", "onPostExecution ");
                Object obj = JSONValue.parse(serverResponse);
                JSONObject jsonObject = (JSONObject) obj;
                String status = (String) jsonObject.get("STATUS");
                if (status.equals("0")) {
                    Log.d("Address:", "Fetch Successfully");
                    //Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    if (check == 1) {
                        Toast.makeText(MainActivity.context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else if (check == 2) {
                        sharedpreferences = MainActivity.context.getSharedPreferences(MainActivity.SHAREDPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(MainActivity.USERPASS, customerPassword);
                        String name = sharedpreferences.getString(MainActivity.USERMOBILENO,null);
                        Log.d("Phone No", name);
                        editor.commit();
                        Toast.makeText(MainActivity.context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        int maxInt = 0;
                        try {
                            Long max = (Long) jsonObject.get("MAX");
                            maxInt = ((int) (long) max);
                            Log.d("No error: ", Integer.toString(maxInt));
                            //Integer[] dataType = new Integer[maxInt];
                            int i = maxInt - 1;
                            for (int j = 0; i >= 0; i--, j++) {
                                String firstNameString = (String) jsonObject.get("FIRSTNAME" + Integer.toString(i));
                                String lastNameString = (String) jsonObject.get("LASTNAME" + Integer.toString(i));
                                String emailString = (String) jsonObject.get("EMAIL" + Integer.toString(i));
                                String userPhoneNo = (String) jsonObject.get("USERPHONENO" + Integer.toString(i));
                                mobileNo.setText(userPhoneNo);
                                firstName.setText(firstNameString);
                                lastName.setText(lastNameString);
                                email.setText(emailString);
                            }
                        } catch (Exception e) {
                            Log.d("Error: ", e.getMessage());
                        }
                    }
                }
                if(status.equals("1")){
                    Toast.makeText(MainActivity.context, "Phone no already exists", Toast.LENGTH_SHORT).show();
                }
                if(status.equals("2")){
                    Log.d("SingUp:","Registration Successful");
                    Toast.makeText(MainActivity.context,"Invalid details",Toast.LENGTH_SHORT).show();
                }
                if(status.equals("3")){
                    Log.d("SingUp:","Registration Successful");
                    Toast.makeText(MainActivity.context,"Something went wrong, Try again !",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(MainActivity.context,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
            //progress.cancel();

        }
    }
}
