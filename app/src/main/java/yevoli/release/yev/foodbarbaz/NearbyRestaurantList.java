package yevoli.release.yev.foodbarbaz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import POJO.ActivityStarter;
import POJO.Restaurant;
import POJO.SearchHistory;
import POJO.User;
import POJO.WebserviceOperations;
import adapter.RestaurantListAdapter;

public class NearbyRestaurantList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
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
            else if (data.containsKey("latlon"))
                query = (String) data.get("latlon");

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
        navigationView.setNavigationItemSelectedListener(this);

        final TextView title = findViewById(R.id.nearby_restaurant_title);
        ProgressBar mProgressView = findViewById(R.id.restoProgress);
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
        WebserviceOperations webserviceOperations = new WebserviceOperations(mProgressView) {
            @Override
            public void onBackgroundTaskCompleted(List<Object> objects) {
                if (objects != null){
                    for (int i = 0; i < objects.size(); i++) {
                        restaurants.add((Restaurant) objects.get(i));
                        Log.i("REST",  objects.get(i).toString());
                    }
                }
                setRestaurantList();
            }
        };

        webserviceOperations.execute(query);
        if (user != null){
            if(!SearchHistory.getList(this, user.getId()).contains(query)){
                SearchHistory.getList(this, user.getId()).add(0, query);
                SearchHistory.addToList(query, user.getId());
            }
            else{
                SearchHistory.getList(this, user.getId()).remove(query);
                SearchHistory.getList(this, user.getId()).add(0, query);
                SearchHistory.removeFromList(query, user.getId());
                SearchHistory.addToList(query, user.getId());
                //This is done to change the position of the query to go back to the top
            }
        }

        for (int i = 0; i < restaurants.size(); i++) {
            Log.i("Restaurant " + i, restaurants.get(i).toString());
        }


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
    public boolean onOptionsItemSelected(MenuItem item) {
        ActivityStarter.OptionsItemsSelected(this, user, item, drawerLayout);

        return super.onOptionsItemSelected(item);
    }

    public void setRestaurantList(){
        // Custom ListView create

        ListAdapter restaurantAdapter = new RestaurantListAdapter(this, restaurants, user);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ActivityStarter.NavigationItemSelected(this, user, item);

        return false;
    }

}


