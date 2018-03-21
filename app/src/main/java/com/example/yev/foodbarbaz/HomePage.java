package com.example.yev.foodbarbaz;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import POJO.User;

public class HomePage extends AppCompatActivity {

    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //The value of this user determines what will happen when you press on the account button
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //If user is logged in, or just registered, the User object will be created
        Bundle data = getIntent().getExtras();
        if (data != null){
            if (data.containsKey("user")){
                user = data.getParcelable("user");
                if (user != null){
                    Log.i("USERNAME----", user.getUsername());
                    Log.i("PASSWORD----" , user.getPassword());
                    Log.i("FIRSTNAME----", user.getFirstname());
                    Log.i("EMAIL----" , user.getEmail());
                }
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        toolBar = findViewById(R.id.include);
        setSupportActionBar(toolBar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.navigation_view);

        // set children on click listener
        for (int i = 0; i < navigationView.getChildCount(); i++){
            navigationView.getChildAt(i).setOnClickListener(onNavigationItemClickListener());
            Log.i("ssasdf","is it passing here?");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        Log.i("ON CREATE", "SHOULD BE HERE ON CCREATE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Log.i("CREATING MENU", "SHOULD BE HERE");

        if (user !=null){
            menu.add(0, Menu.FIRST, Menu.FIRST+2, "Logout").setShowAsAction(Menu.NONE);
        }

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

    public void scanArea(View v) {
        EditText query = findViewById(R.id.query);

        Intent intent = new Intent(this, NearbyRestaurantList.class);
        intent.putExtra("query", query.getText().toString());
        intent.putExtra("user", user);
        startActivity(intent);
    }

    // ON NAVIGATION ITEM CLICK LISTENER
    public View.OnClickListener onNavigationItemClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(null, Settings.class);
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
