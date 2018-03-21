package com.example.yev.foodbarbaz;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import POJO.User;

public class Settings extends AppCompatActivity {
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConstraintLayout constraintLayout;
    TextView textView;
    String navigation;

    //The value of this user determines what will happen when you press on the account button
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        constraintLayout = findViewById(R.id.context_relative_layout);
        textView = findViewById(R.id.context_relative_text);

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

            if (data.containsKey("navigation")){
                navigation = (String) data.get("navigation");
                switch(navigation){
                    case "About us":
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "Favourites":
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.lightRed));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "Settings":
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "History":
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.darkGrey));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    default:
                        constraintLayout.setBackgroundColor(getResources().getColor(R.color.teal));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
        }

        toolBar = findViewById(R.id.appBar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        for (int i = 0; i < drawerLayout.getChildCount(); i++) {
            drawerLayout.getChildAt(i).setOnClickListener(onNavigationItemClickListener());
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
