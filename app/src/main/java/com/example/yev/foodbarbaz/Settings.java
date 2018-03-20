package com.example.yev.foodbarbaz;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
