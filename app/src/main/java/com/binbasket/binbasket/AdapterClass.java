package com.binbasket.binbasket;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ashrafiqubal on 30/05/16.
 */
public class AdapterClass extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] itemList;
    private final String[] priceList;
    private final Integer[] imageId;
    public AdapterClass(Activity context,
                        String[] itemList,String[] priceList, Integer[] imageId) {
        super(context, R.layout.listview_home, itemList);
        this.context = context;
        this.itemList = itemList;
        this.priceList = priceList;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.listview_home, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item_scrap_name);
        TextView txtPrice = (TextView)rowView.findViewById(R.id.item_scrap_price);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_scrap_image);
        //view.setBackgroundColor(Color.BLACK);
        txtTitle.setText(itemList[position]);
        txtPrice.setText(priceList[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}