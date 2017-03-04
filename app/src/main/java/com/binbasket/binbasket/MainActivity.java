package com.binbasket.binbasket;


import com.binbasket.binbasket.fragment.HomeFragment;
import com.binbasket.binbasket.fragment.MyAccount;
import com.binbasket.binbasket.fragment.MyOrders;
import com.binbasket.binbasket.navdrawer.NavDrawerItem;
import com.binbasket.binbasket.navdrawer.NavDrawerListAdapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String SERVERIPADDRESS = "54.169.86.227:8080";
    SharedPreferences sharedpreferences;
    public final static String ISNEWUSER = "com.binbasket.binbasket.isnewuser";
    public final static String SHAREDPREFERENCES = "com.binbasket.binbasket.sharedpreferences";
    public final static String USERMOBILENO = "com.binbasket.binbasket.sharedpreferences.mobileno";
    public final static String USERPASS = "com.binbasket.binbasket.sharedpreferences.userpass";
    public final static String SELECTEDITEMS = "com.binbasket.binbasket.sharedpreferences.selecteditems";
    public static String customerPasswordString;
    public static String customerMobileNoString;
    boolean isNewUser = true;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public static Context context;
    private boolean isAtHome = true;
    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    public static Toolbar toolbar;
    public static boolean isBookSelected=false,ispaperSelected=false,isPlasticSelected=false,isIronSelected=false,isAluminiumSelected=false,
            isCopperSelected=false,isSteelSelected=false,isCartoonSelected=false,isTinSelected=false,
            isBattrySelected=false,isTetrapackSelected=false;
    List<String> selectedItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            context = MainActivity.this;
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24px);
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
            sharedpreferences = getSharedPreferences(SHAREDPREFERENCES, Context.MODE_PRIVATE);
            isNewUser = sharedpreferences.getBoolean(ISNEWUSER,true);
            customerPasswordString = sharedpreferences.getString(MainActivity.USERPASS, null);
            customerMobileNoString = sharedpreferences.getString(MainActivity.USERMOBILENO, null);
            Log.d("Main Activity://", "Line Executed2");
            mTitle = mDrawerTitle = getTitle();
            // load slide menu items
            navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

            // nav drawer icons from resources
            navMenuIcons = getResources()
                    .obtainTypedArray(R.array.nav_drawer_icons);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
            Log.d("Main Activity://", "Line Executed3");
            navDrawerItems = new ArrayList<NavDrawerItem>();
            // adding nav drawer items to array
            // Home
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
            // Home
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
            // My Orders
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
            // My Account
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
            // Pages
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
            // What's hot, We  will add a counter here
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
            // Login/Logout
            if(isNewUser){
                navDrawerItems.add(new NavDrawerItem("Login/SingUp", navMenuIcons.getResourceId(3, -1)));
            }else {
                navDrawerItems.add(new NavDrawerItem("Logout", navMenuIcons.getResourceId(3, -1)));
            }
            // Recycle the typed array
            navMenuIcons.recycle();

            mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
            Log.d("Main Activity://", "Line Executed");
            // setting the nav drawer list adapter
            adapter = new NavDrawerListAdapter(getApplicationContext(),
                    navDrawerItems);
            mDrawerList.setAdapter(adapter);
            // enabling action bar app icon and behaving it as toggle button
            //getActionBar().setDisplayHomeAsUpEnabled(true);
            //getActionBar().setHomeButtonEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    toolbar, //nav menu toggle icon
                    R.string.select, // nav drawer open - description for accessibility
                    R.string.app_name // nav drawer close - description for accessibility
            ) {
                public void onDrawerClosed(View view) {
                    // getActionBar().setTitle(mTitle);
                    // calling onPrepareOptionsMenu() to show action bar icons
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    //getActionBar().setTitle(mDrawerTitle);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    invalidateOptionsMenu();
                }
            };
            Log.d("Main Activity://", "Line Executed4");
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            if (savedInstanceState == null) {
                // on first time display view for first nav item
                Log.d("Main Activity://", "Line Executed");
                displayView(0);
                Log.d("Main Activity://", "Line Executed");
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        try {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    isAtHome=true;
                    break;
                case 1:
                    fragment = new HomeFragment();
                    isAtHome=true;
                    break;
                case 2:
                    if(AppStatus.getInstance(getApplicationContext()).isOnline()){
                        if(isNewUser){
                            /*Intent LoginActivity = new Intent(MainActivity.this, SingUp.class);
                            LoginActivity.putExtra(SELECTEDITEMS,"MYORDERS");
                            startActivity(LoginActivity);
                            finish();*/
                            Toast.makeText(getApplicationContext(),"You are not logged in.",Toast.LENGTH_SHORT).show();
                        }else {
                            fragment = new MyOrders();
                            isAtHome=false;
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Internet connection lost.",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if(AppStatus.getInstance(getApplicationContext()).isOnline()){
                        if(isNewUser){
                            /*Intent LoginActivity = new Intent(MainActivity.this, SingUp.class);
                            LoginActivity.putExtra(SELECTEDITEMS,"MYACCOUNT");
                            startActivity(LoginActivity);
                            finish();*/
                            Toast.makeText(getApplicationContext(),"You are not logged in.",Toast.LENGTH_SHORT).show();
                        }else {
                            fragment = new MyAccount();
                            isAtHome=false;
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Internet connection lost.",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    if(AppStatus.getInstance(getApplicationContext()).isOnline()){
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/binbasket"));
                        startActivity(browserIntent);
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    }else {
                        Toast.makeText(getApplicationContext(),"Internet connection lost",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    if(AppStatus.getInstance(getApplicationContext()).isOnline()){
                        try{
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_SUBJECT, "BinBasket- Ab kuch v bekar nahi");
                            String sAux = "\nHi! Check out BinBasket app. I found it really nice for Selling Scrap \n";
                            sAux = sAux + "https://play.google.com/store/apps/details?id=com.binbasket.binbasket \n\n";
                            i.putExtra(Intent.EXTRA_TEXT, sAux);
                            startActivity(Intent.createChooser(i, "Share using:"));
                        }
                        catch(Exception e) {
                            Log.d("MainActivty:// ","Fragment:// "+e.getMessage());
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Internet connection lost.",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    if(AppStatus.getInstance(getApplicationContext()).isOnline()){
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    }else {
                        Toast.makeText(getApplicationContext(),"Internet connection lost",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 7:
                    if(AppStatus.getInstance(getApplicationContext()).isOnline()){
                        if(isNewUser){
                            Intent LoginActivity = new Intent(MainActivity.this, SingUp.class);
                            LoginActivity.putExtra(SELECTEDITEMS,"SINGUP");
                            startActivity(LoginActivity);
                            finish();
                            isAtHome=false;
                        }else {
                            sharedpreferences = getSharedPreferences(SHAREDPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(MainActivity.ISNEWUSER, true);
                            editor.putString(MainActivity.USERMOBILENO, null);
                            editor.putString(MainActivity.USERPASS, null);
                            editor.commit();
                            isNewUser=true;
                            Toast.makeText(getApplicationContext(),"Log out Successfully",Toast.LENGTH_SHORT).show();
                            Intent mainActivity = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(mainActivity);
                            finish();
                            isAtHome=true;
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Internet connection lost.",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();

                // update selected item and title, then close the drawer
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                //setTitle(navMenuTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                // error in creating fragment
                Log.e("MainActivity", "Error in creating fragment");
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void pickUp(View view){
        if(isAluminiumSelected){
            selectedItems.add("Aluminium");
        }
        if(isCopperSelected){
            selectedItems.add("Copper");
        }
        if(isSteelSelected){
            selectedItems.add("Steel");
        }
        if(isCartoonSelected){
            selectedItems.add("Carton");
        }
        if(isTinSelected){
            selectedItems.add("Tin");
        }
        if(isBattrySelected){
            selectedItems.add("Battry");
        }
        if(isTetrapackSelected){
            selectedItems.add("Tetra Pack");
        }
        if(isBookSelected){
            selectedItems.add("Book");
        }
        if(ispaperSelected){
            selectedItems.add("Paper");
        }
        if(isPlasticSelected){
            selectedItems.add("Plastic");
        }
        if(isIronSelected){
            selectedItems.add("Iron");
        }
        if(selectedItems.size()==0){
            Toast.makeText(getApplicationContext(),"You have not selected any item",Toast.LENGTH_SHORT).show();
            return;
        }
        String selectedItemsInString = selectedItems.toString();
        Log.d("MainActivity: ","Selected items: "+selectedItemsInString);
        if(AppStatus.getInstance(getApplicationContext()).isOnline()) {
            if (isNewUser) {
                Intent LoginActivity = new Intent(MainActivity.this, SingUp.class);
                LoginActivity.putExtra(SELECTEDITEMS,selectedItemsInString);
                startActivity(LoginActivity);
                finish();
            } else {
                Intent AddressActivity = new Intent(MainActivity.this, AddressActivity.class);
                AddressActivity.putExtra(SELECTEDITEMS,selectedItemsInString);
                startActivity(AddressActivity);
                finish();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
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
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    @Override
    public void onBackPressed(){
        Log.d("CDA", "onBackPressed Called");
        if(!isAtHome){
            Fragment fragment = null;
            fragment = new HomeFragment();
            isAtHome=true;
            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();

                // update selected item and title, then close the drawer
                // mDrawerList.setItemChecked(position, true);
                // mDrawerList.setSelection(position);
                //setTitle(navMenuTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                // error in creating fragment
                Log.e("MainActivity", "Error in creating fragment");
            }
        }else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
