package com.example.yev.foodbarbaz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import POJO.Restaurant;
import POJO.User;
import POJO.WebserviceOperations;
import adapter.RestaurantListAdapter;

public class NearbyRestaurantList extends AppCompatActivity {
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView restaurantListView;
    private View mProgressView;
    private List<Restaurant> restaurants =  new ArrayList<Restaurant>();
    private boolean isTitleVisible = false;
    private String query;

    //The value of this user determines what will happen when you press on the account button
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle data = getIntent().getExtras();

        if (data != null){
            if (data.keySet().contains("query"))
                query = (String) data.get("query");
            else
                query = "H2M 1M2";

            user = data.getParcelable("user");
            if (user!=null){
                Log.i("USERNAME----", user.getUsername());
                Log.i("PASSWORD----" , user.getPassword());
                Log.i("FIRSTNAME----", user.getFirstname());
                Log.i("EMAIL----" , user.getEmail());
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_restaurant_list);

        toolBar = findViewById(R.id.appBar);
        setSupportActionBar(toolBar);

        toolBar = findViewById(R.id.appBar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.navigation_view);

        final TextView title = findViewById(R.id.nearby_restaurant_title);
        mProgressView = findViewById(R.id.restoProgress);
        restaurantListView = findViewById(R.id.restaurant_list_view);

        restaurantListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                if ((i == SCROLL_STATE_TOUCH_SCROLL || i== SCROLL_STATE_FLING) && isTitleVisible){
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0f);
                    valueAnimator.setDuration(750);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float alpha = (float) animation.getAnimatedValue();
                            title.setAlpha(alpha);
                        }
                    });
                    valueAnimator.start();
                    isTitleVisible = false;
                }
                if (i == SCROLL_STATE_IDLE){
                    if (!isTitleVisible){
                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
                        valueAnimator.setDuration(750);
                        valueAnimator.setStartDelay(500);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float alpha = (float) animation.getAnimatedValue();
                                title.setAlpha(alpha);
                            }
                        });
                        valueAnimator.start();
                        isTitleVisible = true;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        // Queries the webservice
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
        webserviceOperations.execute(query);
        showProgress(false);

        for (int i = 0; i < restaurants.size(); i++) {
            Log.i("Restaurant " + i, restaurants.get(i).toString());
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_not_home, menu);
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
            case R.id.account:
                accountPressed();
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

    // ON NAVIGATION ITEM CLICK LISTENER
    public View.OnClickListener onNavigationItemClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Settings.class);
                String buttonPressed = "";

                switch (v.getId()){
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
            }
        };
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


