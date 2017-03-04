package com.binbasket.binbasket.fragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binbasket.binbasket.MainActivity;
import com.binbasket.binbasket.R;


/**
 * Created by ashrafiqubal on 18/06/16.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    public static final int LISTITEMS = 0;

    private static String[] mItemName;
    private static int[] mImageId;
    private static String[] mItemPrice;
    private static int[] mDataSetTypes;
    private static Integer[] mimageid;

    public CustomAdapter(String[] itemName,String[] itemPrice,int[] imageId, int[] dataSetTypes) {
        mItemName = itemName;
        mImageId = imageId;
        mItemPrice = itemPrice;
        mDataSetTypes = dataSetTypes;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick " + getPosition() + " " + mDataSetTypes[getPosition()]);
            if(mDataSetTypes[getPosition()]==0){
                switch (getPosition()){
                    case 0:
                        if (!MainActivity.isBookSelected) {
                            MainActivity.isBookSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isBookSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 1:
                        if (!MainActivity.ispaperSelected) {
                            MainActivity.ispaperSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.ispaperSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 2:
                        if (!MainActivity.isPlasticSelected) {
                            MainActivity.isPlasticSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isPlasticSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 3:
                        if (!MainActivity.isIronSelected) {
                            MainActivity.isIronSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isIronSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 4:
                        if (!MainActivity.isAluminiumSelected) {
                            MainActivity.isAluminiumSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isAluminiumSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 5:
                        if (!MainActivity.isCopperSelected) {
                            MainActivity.isCopperSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isCopperSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 6:
                        if (!MainActivity.isSteelSelected) {
                            MainActivity.isSteelSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isSteelSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 7:
                        if (!MainActivity.isCartoonSelected) {
                            MainActivity.isCartoonSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isCartoonSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 8:
                        if (!MainActivity.isTinSelected) {
                            MainActivity.isTinSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isTinSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 9:
                        if (!MainActivity.isBattrySelected) {
                            MainActivity.isBattrySelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isBattrySelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    case 10:
                        if (!MainActivity.isTetrapackSelected) {
                            MainActivity.isTetrapackSelected = true;
                            view.setBackgroundColor(Color.BLACK);
                        } else {
                            MainActivity.isTetrapackSelected = false;
                            view.setBackgroundColor(217217217);
                        }
                        break;
                    default:
                        Toast.makeText(MainActivity.context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            //Log.d(TAG, "onClick " + " " + mDataSetTypes[getPosition()]);

        }
    }
    public class ItemListViewHolder extends ViewHolder {
        TextView itemName,itemPrice;
        ImageView itemImage;

        public ItemListViewHolder(View v) {
            super(v);
            this.itemName = (TextView) v.findViewById(R.id.item_scrap_name);
            this.itemPrice = (TextView) v.findViewById(R.id.item_scrap_price);
            this.itemImage = (ImageView) v.findViewById(R.id.item_scrap_image);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.listview_home, viewGroup, false);
            return new ItemListViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == LISTITEMS) {
            ItemListViewHolder holder = (ItemListViewHolder) viewHolder;
            holder.itemName.setText(mItemName[position]);
            holder.itemPrice.setText(mItemPrice[position]);
            holder.itemImage.setImageResource(mImageId[position]);
        }
    }
    @Override
    public int getItemCount() {
        return mItemName.length;
    }
    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }
}