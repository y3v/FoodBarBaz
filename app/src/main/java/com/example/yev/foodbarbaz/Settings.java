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
    DrawerLayout constraintLayout;
    TextView textView;
    String navigation;

    //The value of this user determines what will happen when you press on the account button
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        drawerLayout = findViewById(R.id.drawer_layout);
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
            toolBar = findViewById(R.id.appBar);
            setSupportActionBar(toolBar);

            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigation_view);

            if (data.containsKey("navigation")){
                navigation = (String) data.get("navigation");
                switch(navigation){
                    case "About us":
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "Favourites":
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.lightRed));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "Settings":
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "History":
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.darkGrey));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                    default:
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.teal));
                        textView.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_not_home, menu);

        if (user !=null){
            menu.add(0, Menu.FIRST, Menu.FIRST+2, R.string.logout).setShowAsAction(Menu.NONE);
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
