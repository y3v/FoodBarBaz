package com.example.yev.foodbarbaz;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import POJO.MenuItem;
import POJO.Restaurant;
import POJO.User;
import adapter.RestaurantListAdapter;
import adapter.RestaurantMenuExListAdapter;

public class RestaurantMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ExpandableListView expandableListView;
    Restaurant restaurant;

    //The value of this user determines what will happen when you press on the account button
    User user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //If user is logged in, or just registered, the User object will be created
        Bundle data = getIntent().getExtras();
        if (data != null){
            user = data.getParcelable("user");
            if (user!=null){
                Log.i("USERNAME----", user.getUsername());
                Log.i("PASSWORD----" , user.getPassword());
                Log.i("FIRSTNAME----", user.getFirstname());
                Log.i("EMAIL----" , user.getEmail());
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        // set toolbar and drawer menu
        toolBar = findViewById(R.id.appBar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        // get restaurant data
        if (data != null && data.keySet().contains("restaurant")){
            restaurant = data.getParcelable("restaurant");
            restaurant.setMenu(buildFakeMenu());
        }
        else {
            restaurant = new Restaurant("Là Là","789", "783 rue Saint-Paul, QC, Quebec", null,"0", "5", "Cuisine quebecoise");
            restaurant.setMenu(buildFakeMenu());
        }

        // set restaurant Name, Category & Address
        ((TextView)findViewById(R.id.restaurant_name)).setText(restaurant.getName());
        ((TextView)findViewById(R.id.category)).setText(restaurant.getCategory() + " - Rated " + restaurant.getRating() + "/5");
        ((TextView)findViewById(R.id.address)).setText(restaurant.getAddress());

        // MENU Custom ExpandableListView create
        expandableListView = findViewById(R.id.expandable_list_view);
        ExpandableListAdapter exListAdapter = new RestaurantMenuExListAdapter(this, restaurant.getMenu());
        expandableListView.setAdapter(exListAdapter);
        expandableListView.expandGroup(0);
    }

    public HashMap<String, List<MenuItem>> buildFakeMenu(){
        HashMap<String, List<MenuItem>> menu = new HashMap<>();

        List<MenuItem> appetizers = new ArrayList<>();
        List<MenuItem> mainCourse = new ArrayList<>();
        List<MenuItem> desserts = new ArrayList<>();

        MenuItem a1 = new MenuItem("Pea soup",12.99 , "Delicious pea soup");
        MenuItem a2 = new MenuItem("Mini pogos",10.99 , "Homemade mini pogos");
        MenuItem a3 = new MenuItem("Soupe aux pois",15.99 , "Delicieuse soupe aux pois");
        appetizers.add(a1);
        appetizers.add(a2);
        appetizers.add(a3);

        MenuItem m1 = new MenuItem("Tourtiere du Lac",21.99 , "Traditional tourtiere du lac saint jean");
        MenuItem m2 = new MenuItem("Meatpie",17.99 , "Homemade meat pie");
        MenuItem m3 = new MenuItem("Partridge and potatoes",19.99 , "Just some partridge");
        mainCourse.add(m1);
        mainCourse.add(m2);
        mainCourse.add(m3);

        MenuItem d1 = new MenuItem("Blueberry pie",10.99 , "Delicious Blueberry pie");
        MenuItem d2 = new MenuItem("Apple pie",15.99 , "Delicious Apple pie");
        MenuItem d3 = new MenuItem("Pi pie",14.99 , "Delicious Pi pie");
        desserts.add(d1);
        desserts.add(d2);
        desserts.add(d3);

        menu.put("Appetizers", appetizers);
        menu.put("Main Course", mainCourse);
        menu.put("Desserts", desserts);

        return menu;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        String buttonPressed = "";
        int id = item.getItemId();

        Log.i("" + id, "jasldkfj");
        switch (id){
            case R.id.about_us_drawer:
                buttonPressed = "About us";
                break;

            case R.id.favourites_drawer:
                buttonPressed = "Favourites";
                break;

            case R.id.app_settings_drawer:
                buttonPressed = "Settings";
                break;

            case R.id.history_drawer:
                buttonPressed = "History";
                break;

            case R.id.report_drawer:
                buttonPressed = "Report";
                break;
        }

        intent.putExtra("navigation", buttonPressed);
        intent.putExtra("user", user);
        startActivity(intent);

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_not_home, menu);

        if (user !=null){
            menu.add(0, Menu.FIRST, Menu.FIRST+2, "Logout").setShowAsAction(Menu.NONE);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                Toast.makeText(this,"Settings Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_favs:
                Toast.makeText(this,"Favourites Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.account:
                accountPressed();
                break;
            case Menu.FIRST:
                Toast.makeText(this, R.string.user_logged_out, Toast.LENGTH_SHORT).show();
                user = null;
                getIntent().removeExtra("user");
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void accountPressed(){
        if (user == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else{
            //TO DO: set up intent for Display Accounts page!
            //Intent intent = new Intent()
            //intent.putExtra("user", user);
            Toast.makeText(this,"TO DO: DISPLAY ACCOUNTS PAGE", Toast.LENGTH_SHORT).show();
        }
    }
}
