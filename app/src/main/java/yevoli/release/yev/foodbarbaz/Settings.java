package yevoli.release.yev.foodbarbaz;

import android.app.Activity;
import android.content.res.Resources;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import POJO.ActivityStarter;
import POJO.ThemeHandler;
import POJO.User;

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DrawerLayout constraintLayout;
    TextView textView;
    String navigation;
    Activity settings;

    //The value of this user determines what will happen when you press on the account button
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = this;

        //If user is logged in, or just registered, the User object will be created
        Bundle data = getIntent().getExtras();
        if (data != null) {
            if (data.containsKey("user")) {
                user = data.getParcelable("user");
                if (user != null) {
                    Log.i("ID--------", "" + user.getId());
                    Log.i("USERNAME----", user.getUsername());
                    Log.i("PASSWORD----", user.getPassword());
                    Log.i("FIRSTNAME----", user.getFirstname());
                    Log.i("EMAIL----", user.getEmail());
                }
            }
        }

        //Check which theme is enabled
        if (user != null){
            String theme = ThemeHandler.getTheme(this, user.getId());
            if (theme != null){
                if (theme.equals("dark")){
                    Log.i("THEME---", "DARK");
                    setDarkTheme();
                }
                else{
                    Log.i("THEME---", "LIGHT");
                    setLightTheme();
                }
            }
            else{
                Log.i("THEME---", "LIGHT");
                setLightTheme();
            }
        }else{
            Log.i("THEME---", "LIGHT");
            setLightTheme();
        }

        super.onCreate(savedInstanceState);

        final ToggleButton toggleButton = findViewById(R.id.toggleDarkMode);
        Button backButton = findViewById(R.id.buttonSettingsBack);

        if (ThemeHandler.getTheme(this,user.getId()).equals("dark")){
            toggleButton.setChecked(true);
        }

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleButton.isChecked()){
                    ThemeHandler.removeTheme(getApplicationContext(), user.getId());
                    ThemeHandler.addTheme("dark", user.getId(), getApplicationContext());
                }
                else{
                    ThemeHandler.removeTheme(getApplicationContext(), user.getId());
                    ThemeHandler.addTheme("light", user.getId(), getApplicationContext());
                }
                Log.i("THEME:::", ThemeHandler.getTheme(getApplicationContext(), user.getId()));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startHomepage(settings, user);
            }
        });
    }

    private void setLightTheme(){
        setContentView(R.layout.activity_settings);
        toolBar = findViewById(R.id.include);
        setSupportActionBar(toolBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setDarkTheme(){
        setTheme(R.style.Theme_AppCompat);
        setContentView(R.layout.activity_settings);
        toolBar = findViewById(R.id.include);
        toolBar.setVisibility(View.GONE);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.closed_drawer);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        ActivityStarter.OptionsItemsSelected(this, user ,item, drawerLayout);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ActivityStarter.NavigationItemSelected(this, user, item);
        Log.i("NAVIGATION:::", "" + item.toString());

        return false;
    }

}
