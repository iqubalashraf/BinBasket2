package com.binbasket.binbasket.fragment;

/**
 * Created by ashrafiqubal on 17/06/16.
 */
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.binbasket.binbasket.MainActivity;
import com.binbasket.binbasket.R;
import com.binbasket.binbasket.fragment.CustomAdapter;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static RecyclerView mRecyclerView;
    private static CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //Boolean values to check selected items
    String[] itemName;
    String[] itemPrice;

    View rootView;
    static View listView;
    int[] imageId = {
            R.drawable.ic_book,
            R.drawable.ic_newspaper_clip_art_xcgggrdca,
            R.drawable.ic_plastic,
            R.drawable.ic_iron,
            R.drawable.ic_aluminium,
            R.drawable.ic_copper,
            R.drawable.ic_steel,
            R.drawable.ic_carton,
            R.drawable.ic_tin_cans_square_metal_16880689,
            R.drawable.ic_battery1,
            R.drawable.ic_tetra_pack
    };
    int[] dataType = {0,0,0,0,0,0,0,0,0,0,0};
    ListView list;
    public HomeFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_home, container, false);
        Log.d("Home Fragment://", "Line Executed");
        initializeBooleans();
        return rootView;
    }
    @Override
    public void onStart(){
        super.onStart();
        itemName = getResources().getStringArray(R.array.listitemname);
        itemPrice = getResources().getStringArray(R.array.listitemprice);
        Log.d("Home fragment://", "Line Executed2");
        //AdapterClass adapterClass = new AdapterClass(HomeFragment.super.getActivity(),itemName,itemPrice,imageId);
        //list=(ListView)getView().findViewById(R.id.listview);
        //list.setAdapter(adapterClass);
        try{
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewHome);
            mLayoutManager = new LinearLayoutManager(MainActivity.context);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new CustomAdapter(itemName, itemPrice,imageId,dataType);
            mRecyclerView.setAdapter(mAdapter);
        }catch (Exception e){
            Log.d(TAG,"Error: "+e.getMessage());
        }
        Log.d("Home fragment://", "Line Executed2");
        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.context, "You Clicked at " + Integer.toString(position) + itemName[+position], Toast.LENGTH_SHORT).show();
                listView = view;
                switch (position) {
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
        });*/
    }
    private void updateList(){
        Log.d("HomeFragment", "UpdateList");
        if(MainActivity.isAluminiumSelected){
            Log.d("HomeFragment","UpdateList Aluminium");
        }
        if(MainActivity.isCopperSelected){

        }
        if(MainActivity.isSteelSelected){

        }
        if(MainActivity.isCartoonSelected){

        }
        if(MainActivity.isTinSelected){

        }
        if(MainActivity.isBattrySelected){

        }
        if(MainActivity.isTetrapackSelected){

        }
        if(MainActivity.isBookSelected){

        }
        if(MainActivity.ispaperSelected){

        }
        if(MainActivity.isPlasticSelected){

        }
        if(MainActivity.isIronSelected){

        }
    }
    private void initializeBooleans(){
        MainActivity.isAluminiumSelected = false;
        MainActivity.isCopperSelected = false;
        MainActivity.isSteelSelected = false;
        MainActivity.isCartoonSelected = false;
        MainActivity.isTinSelected = false;
        MainActivity.isBattrySelected = false;
        MainActivity.isTetrapackSelected = false;
        MainActivity.isBookSelected = false;
        MainActivity.ispaperSelected = false;
        MainActivity.isPlasticSelected = false;
        MainActivity.isIronSelected = false;
    }
}