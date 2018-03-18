package com.example.yev.foodbarbaz;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import POJO.Restaurant;
import adapter.RestaurantListAdapter;

public class NearbyRestaurantList extends AppCompatActivity {
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    List<Restaurant> restaurants =  new ArrayList<Restaurant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restaurants.add(new Restaurant("123","food", "1272 de la chevrotiere", "Home", "5.0", null));
        restaurants.add(new Restaurant("456","barbaz", "1272 de la gauchetiere", "Home foodz", "5.0", null));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_restaurant_list);

        toolBar = findViewById(R.id.appBar);
        setSupportActionBar(toolBar);

        toolBar = findViewById(R.id.appBar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


        // Custom ListView create
        ListAdapter restaurantAdapter = new RestaurantListAdapter(this, restaurants);
        ListView restaurantListView = findViewById(R.id.restaurant_list_view);
        restaurantListView.setAdapter(restaurantAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                Toast.makeText(this,"Settings Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_favs:
                Toast.makeText(this,"Favourites Clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
