package com.example.yev.foodbarbaz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import POJO.Restaurant;
import POJO.WebserviceOperations;
import adapter.RestaurantListAdapter;

public class NearbyRestaurantList extends AppCompatActivity {
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView restaurantListView;
    private View mProgressView;
    private List<Restaurant> restaurants =  new ArrayList<Restaurant>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // restaurants.add(new Restaurant("123","food", "1272 de la chevrotiere", "Home", "5.0", null));
        // restaurants.add(new Restaurant("456","barbaz", "1272 de la gauchetiere", "Home foodz", "5.0", null));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_restaurant_list);

        toolBar = findViewById(R.id.appBar);
        setSupportActionBar(toolBar);

        toolBar = findViewById(R.id.appBar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.navigation_view);

        mProgressView = findViewById(R.id.restoProgress);
        restaurantListView = findViewById(R.id.restaurant_list_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        WebserviceOperations webserviceOperations = new WebserviceOperations() {
            @Override
            public void onBackgroundTaskCompleted(List<Object> objects) {
                for (int i = 0; i < objects.size(); i++) {
                    restaurants.add((Restaurant) objects.get(i));
                    Log.i("REST",  objects.get(i).toString());
                }
                setRestaurantList();
            }
        };

        showProgress(true);
        webserviceOperations.execute("h3C0E9");
        showProgress(false);

        for (int i = 0; i < restaurants.size(); i++) {
            Log.i("Restaurant " + i, restaurants.get(i).toString());
        }



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

    public void setRestaurantList(){
        // Custom ListView create

        ListAdapter restaurantAdapter = new RestaurantListAdapter(this, restaurants);

        restaurantListView.setAdapter(restaurantAdapter);
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            restaurantListView.setVisibility(show ? View.GONE : View.VISIBLE);
            restaurantListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    restaurantListView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            restaurantListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}


