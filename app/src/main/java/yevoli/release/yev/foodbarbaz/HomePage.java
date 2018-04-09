package yevoli.release.yev.foodbarbaz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.provider.Settings;
import POJO.GeolocationService;
import POJO.User;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LocationManager locationManager;
    String lat, lon;
    Location location;

    //The value of this user determines what will happen when you press on the account button
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //If user is logged in, or just registered, the User object will be created
        Bundle data = getIntent().getExtras();
        if (data != null) {
            if (data.containsKey("user")) {
                user = data.getParcelable("user");
                if (user != null) {
                    Log.i("USERNAME----", user.getUsername());
                    Log.i("PASSWORD----", user.getPassword());
                    Log.i("FIRSTNAME----", user.getFirstname());
                    Log.i("EMAIL----", user.getEmail());
                }
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        toolBar = findViewById(R.id.include);
        setSupportActionBar(toolBar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        Log.i("ON CREATE", "SHOULD BE HERE ONCREATE");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //FIRST RUN OF THE APP -- USER NEEDS TO GIVE PERMISSIONS TO ALLOW THE DEVICE TO OBTAIN LOCATION
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.;
            //
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
            Log.i("lat,lon", "NO PERMISSION");
            return;
        }

        //CHECK IF LOCATION SERVICES ARE TURNED ON -- IF NOT OPEN DIALOG TO ALLOW USER TO TURN THEM ON
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(R.string.GPS_not_enabled);
            dialog.setPositiveButton(R.string.turn_on_location, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    //THE FOLLOWING INTENT WILL SEND THE USER TO THEIR PHONE SETTINGS TO TURN ON LOCATION SERVICES
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // NOTHING HAS TO BE DONE HERE, DIALOG WILL SIMPLY CLOSE
                }
            });
            dialog.show();
        }

        //NOW THAT PERMISSION IS ESTABLISHED WE CAN GET THE LOCATION OF THE USER -- UNLESS THE LOCATION SERVICES ARE NOT TURNED ON
        //IF THEY ARE NOT TURNED ON, A DIALOG WILL TAKE THE USER TO THEIR PHONE SETTINGS TO TURN ON LOCATION SERVICES
        //ONCE LOCATION SERVICES ARE TURNED ON, THE USER WILL RUN THE SAME CODE BLOCK AS BELOW... BUT IN THE onRestart() METHOD
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null){
            Log.i("LOCATION", "INSIDE BLOCK");
            lat = "" + location.getLatitude();
            lon = "" + location.getLongitude();

            Log.i("lat,lon", lat + ", " + lon);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //NEED TO CHECK PERMISSIONS AGAIN -- OTHERWISE ERROR WILL BE THROWN AT THE LOCATION_MANAGER IN THE NEXT CODE BLOCK
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
            Log.i("lat,lon", "NO PERMISSION");
            return;
        }

        //IN THE CASE THAT THE USER HAS TO EXIT THE APP TO TURN ON LOCATION SERVICES...RUN THIS TO GET COORDINATES
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null){
            Log.i("LOCATION", "INSIDE BLOCK");
            lat = "" + location.getLatitude();
            lon = "" + location.getLongitude();

            Log.i("lat,lon", lat + ", " + lon);
        }
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

    private void accountPressed(){
        if (user == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, UserDetails.class);
            intent.putExtra("user", user);
            //Toast.makeText(this,"TO DO: DISPLAY ACCOUNTS PAGE", Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        String buttonPressed = "";
        int id = item.getItemId();

        Log.i("" + id, "NAVIGATION OPTION PRESSED");
        switch (id){
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

            case R.id.following_drawer:
                Intent intent2 = new Intent(this, Following.class);
                intent2.putExtra("user", user);
                startActivity(intent2);
                break;

            case R.id.report_drawer:
                buttonPressed = "Report";
                break;
        }

        /*intent.putExtra("navigation", buttonPressed);
        intent.putExtra("user", user);
        startActivity(intent);*/

        return false;
    }
}
