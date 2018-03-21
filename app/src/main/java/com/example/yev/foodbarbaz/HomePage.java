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

public class HomePage extends AppCompatActivity {

    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle data = getIntent().getExtras();


        if (data != null){
            /*if (data.keySet().contains("query"))
                query = (String) data.get("query");
            else
                query = "H2M 1M2";*/
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        toolBar = findViewById(R.id.include);
        setSupportActionBar(toolBar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        Log.i("ON CREATE", "SHOULD BE HERE ON CCREATE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Log.i("CREATING MENU", "SHOULD BE HERE");
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

    public void scanArea(View v) {
        EditText query = findViewById(R.id.query);

        Intent intent = new Intent(this, NearbyRestaurantList.class);
        intent.putExtra("query", query.getText().toString());
        startActivity(intent);
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
}
