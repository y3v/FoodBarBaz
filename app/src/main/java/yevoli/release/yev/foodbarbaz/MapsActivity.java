package yevoli.release.yev.foodbarbaz;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import POJO.Restaurant;
import POJO.User;
import POJO.UserLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String action, title, subtitle;
    private Restaurant restaurant;
    private User user, friend;
    private List<User> friends;
    private LatLng latlon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle data = getIntent().getExtras();
        // mapAction
        if (data != null){
            if (data.containsKey("mapAction")){
                action = data.getString("mapAction");
                switch (action){
                    case "restaurant":
                        restaurant = (Restaurant) data.get("restaurant");
                        latlon = (LatLng) data.get("latlon");
                        title = restaurant.getName();
                        subtitle = restaurant.getAddress();
                        break;

                    case "seeFriend":
                        friend = (User) data.get("friend");
                        UserLocation loc = (UserLocation) data.get("friendLocation");
                        latlon = new LatLng(loc.getLatitude(), loc.getLongitude());

                        title = "Your friend @" + friend.getUsername() + " was right there!";
                        subtitle = "Time to gather your friends and join " + friend.getFirstname() + " :)";
                        break;

                    case "seeFriends":
                        break;
                }
            }
        }

        TextView restName = findViewById(R.id.rest_name);
        TextView restAddress = findViewById(R.id.rest_address);

        restName.setText(title);
        restAddress.setText(subtitle);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurant));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon, 18.0f));

        switch (action){
            case "restaurant":
                mMap.addMarker(new MarkerOptions().position(latlon).title(restaurant.getAddress()));
                break;

            case "seeFriend":
                mMap.addMarker(new MarkerOptions().position(latlon).title(friend.getFirstname() + " " + friend.getLastname()));
                break;

            case "seeFriends":
                break;
        }

    }

    public void onBackClick(View v){
        finish();
    }
}
