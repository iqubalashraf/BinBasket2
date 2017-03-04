package com.binbasket.binbasket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ashrafiqubal on 18/06/16.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private static String[] mDataSet;
    private static String[] mDataSet2;
    private static String[] maddressset;
    private static int[] mDataSetTypes;

    public static final int ADDRESSVIEW = 2;
    public static final int BUTTONVIEW = 3;
    public static final int ACTIVEORDER = 1;
    public static final int COMPLETEDORDER = 0;
    public static final int CANCELLEDORDER = 4;

    public static int check=0;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick " + getPosition() + " " + mDataSet[getPosition()]);
            if(mDataSetTypes[getPosition()]==2){
                Intent conformationActivity = new Intent(AddressActivity.context,ConformationActivity.class);
                conformationActivity.putExtra(MainActivity.SELECTEDITEMS, AddressActivity.selectedItemsInSTring);
                conformationActivity.putExtra("CONFORMEDADDRESS", mDataSet[getPosition()]);
                AddressActivity.context.startActivity(conformationActivity);
                ((AddressActivity)AddressActivity.context).finish();
            }
            if(mDataSetTypes[getPosition()]==1){
                check=1;
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Sure to Cancel")
                        .setMessage("Do you really want to Cancel the PickUp Order?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Toast.makeText(MainActivity.context, "Yaay", Toast.LENGTH_SHORT).show();
                                Uri.Builder builder = new Uri.Builder();
                                builder.scheme("http")
                                        .authority(MainActivity.SERVERIPADDRESS)
                                        .appendPath("binbasket")
                                        .appendPath("cancelOrder.jsp")
                                        .appendQueryParameter("USERPHONENO", MainActivity.customerMobileNoString)
                                        .appendQueryParameter("PASSWORD",MainActivity.customerPasswordString);
                                String myUrl = builder.build().toString();
                                myUrl = myUrl.replace("%3A", ":");
                                Log.d("URL://",myUrl);
                                ExecuteUrl executeUrl = new ExecuteUrl(MainActivity.context);
                                executeUrl.execute(myUrl);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
            if(mDataSetTypes[getPosition()]==0 ||mDataSetTypes[getPosition()]==4){
                check=2;
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Sure to Repeat")
                        .setMessage("Do you really want to Place the PickUp Again?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Toast.makeText(MainActivity.context, "Yaay", Toast.LENGTH_SHORT).show();
                                Uri.Builder builder = new Uri.Builder();
                                builder.scheme("http")
                                        .authority(MainActivity.SERVERIPADDRESS)
                                        .appendPath("binbasket")
                                        .appendPath("PlaceOrder.jsp")
                                        .appendQueryParameter("USERPHONENO", MainActivity.customerMobileNoString)
                                        .appendQueryParameter("PASSWORD", MainActivity.customerPasswordString)
                                        .appendQueryParameter("ADDRESS", maddressset[getPosition()])
                                        .appendQueryParameter("SELECTEDITEMS", mDataSet[getPosition()]);
                                String myUrl = builder.build().toString();
                                myUrl = myUrl.replace("%3A", ":");
                                Log.d("URL://",myUrl);
                                ExecuteUrl executeUrl = new ExecuteUrl(MainActivity.context);
                                executeUrl.execute(myUrl);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
            Log.d(TAG, "onClick " + " " + mDataSetTypes[getPosition()]);

        }
    }

    public class AddressViewHolder extends ViewHolder {
        TextView temp;

        public AddressViewHolder(View v) {
            super(v);
            this.temp = (TextView) v.findViewById(R.id.address_card);
        }
    }
    public class ActiveOrderViewHolder extends ViewHolder {
        TextView temp1,temp2;

        public ActiveOrderViewHolder(View v) {
            super(v);
            this.temp1 = (TextView) v.findViewById(R.id.active_order_card_list_of_items);
            this.temp2 = (TextView)v.findViewById(R.id.active_order_card_time);
        }
    }
    public class CompletedOrderViewHolder extends ViewHolder {
        TextView temp1,temp2;

        public CompletedOrderViewHolder(View v) {
            super(v);
            this.temp2 = (TextView) v.findViewById(R.id.completed_order_card_list_of_items);
            this.temp1 = (TextView)v.findViewById(R.id.completed_order_card_time);
        }
    }
    public class CancelledOrderViewHolder extends ViewHolder{
        TextView temp1,temp2;
        public CancelledOrderViewHolder(View view){
            super(view);
            this.temp1 = (TextView)view.findViewById(R.id.cancelled_order_card_list_of_items);
            this.temp2 = (TextView)view.findViewById(R.id.cancelled_order_card_time);
        }
    }
    public class AddAddressViewHolder extends ViewHolder {
        public AddAddressViewHolder(View v) {
            super(v);
        }
    }
    public CustomAdapter(String[] dataSet, int[] dataSetTypes) {
        mDataSet = dataSet;
        mDataSetTypes = dataSetTypes;
    }
    public CustomAdapter(String[] dataSet,String[] dataSet2,String[] addressSet, int[] dataSetTypes){
        mDataSet = dataSet;
        mDataSetTypes = dataSetTypes;
        mDataSet2 = dataSet2;
        maddressset = addressSet;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == ADDRESSVIEW) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.address_card, viewGroup, false);

            return new AddressViewHolder(v);
        } else if (viewType == BUTTONVIEW){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.add_address_button, viewGroup, false);
            return new AddAddressViewHolder(v);
        }else if (viewType == ACTIVEORDER){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.active_order_card, viewGroup, false);
            return new ActiveOrderViewHolder(v);
        }else if(viewType==COMPLETEDORDER){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.completed_order_card, viewGroup, false);
            return new CompletedOrderViewHolder(v);
        }else {
            v = LayoutInflater.from((viewGroup.getContext())).inflate(R.layout.cancelled_order_card,viewGroup,false);
            return new CancelledOrderViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == ADDRESSVIEW) {
            AddressViewHolder holder = (AddressViewHolder) viewHolder;
            holder.temp.setText(mDataSet[position]);
        }
        else if (viewHolder.getItemViewType() == BUTTONVIEW){
            AddAddressViewHolder holder = (AddAddressViewHolder) viewHolder;
        }else if (viewHolder.getItemViewType() == ACTIVEORDER){
            ActiveOrderViewHolder holder = (ActiveOrderViewHolder) viewHolder;
            holder.temp1.setText(mDataSet[position]);
            holder.temp2.setText(mDataSet2[position]);
        }else if (viewHolder.getItemViewType() == COMPLETEDORDER){
            CompletedOrderViewHolder holder = (CompletedOrderViewHolder) viewHolder;
            holder.temp2.setText(mDataSet[position]);
            holder.temp1.setText(mDataSet2[position]);
        }else if (viewHolder.getItemViewType() == CANCELLEDORDER){
            CancelledOrderViewHolder holder = (CancelledOrderViewHolder)viewHolder;
            holder.temp1.setText(mDataSet[position]);
            holder.temp2.setText(mDataSet2[position]);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }
}