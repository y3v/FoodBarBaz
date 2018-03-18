package com.example.yev.foodbarbaz;

import android.database.DataSetObserver;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import POJO.MenuItem;
import POJO.Restaurant;
import adapter.RestaurantListAdapter;
import adapter.RestaurantMenuExListAdapter;

public class RestaurantMenu extends AppCompatActivity {
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ExpandableListView expandableListView;
    Restaurant restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Bundle data = getIntent().getExtras();
        if (data != null && data.keySet().contains("restaurant")){
            restaurant = data.getParcelable("restaurant");
            restaurant.setMenu(buildFakeMenu());
        }
        else {
            restaurant = new Restaurant("789","LaLa", "783 rue Saint-Paul, QC, Quebec", "Cuisine quebecoise", "5", null);
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

        menu.put("appetizers", appetizers);
        menu.put("mainCourse", mainCourse);
        menu.put("desserts", desserts);

        return menu;
    }
}
