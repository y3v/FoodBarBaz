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
import android.widget.EditText;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        toolBar = findViewById(R.id.appBar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void scanArea(View v) {
        EditText query = findViewById(R.id.query);

        Intent intent = new Intent(this, NearbyRestaurantList.class);
        intent.putExtra("query", query.getText().toString());
        startActivity(intent);
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
