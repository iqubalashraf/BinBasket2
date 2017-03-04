package com.binbasket.binbasket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
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
 * Created by ashrafiqubal on 29/06/16.
 */
public class ExecuteUrl extends AsyncTask<String, Void, Boolean> {
    String serverResponse;
    private ProgressDialog pDialog;
    Context context1;
    SharedPreferences sharedpreferences;
    public ExecuteUrl(Context context){
        context1 = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Starting download");
        pDialog = new ProgressDialog(context1);
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
        Object obj = JSONValue.parse(serverResponse);
        JSONObject jsonObject = (JSONObject)obj;
        String status = (String)jsonObject.get("STATUS");
        if(status.equals("0")){
            Log.d("SingUp:// ", "onPostExecution ");
            if(CustomAdapter.check==1){
                Toast.makeText(context1,"PickUp Cancelled",Toast.LENGTH_SHORT).show();
            }
            if(CustomAdapter.check==2){
                Toast.makeText(context1,"Order Placed Successfully",Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(context1,MainActivity.class);
            context1.startActivity(intent);
            ((MainActivity)context1).finish();
        }
        if(status.equals("1")){
            Toast.makeText(context1,"Phone no already exists",Toast.LENGTH_SHORT).show();
        }
        if(status.equals("2")){
            Log.d("SingUp:","Registration Successful");
            Toast.makeText(context1,"Invalid details",Toast.LENGTH_SHORT).show();
        }
        if(status.equals("3")){
            Log.d("SingUp:","Registration Successful");
            Toast.makeText(context1,"You already have a Active Order !",Toast.LENGTH_SHORT).show();
        }
    }
}