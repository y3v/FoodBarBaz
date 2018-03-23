package com.example.yev.foodbarbaz;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import POJO.Restaurant;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lon = 0.0;
    private double lat = 0.0;
    private String address = "";
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle data = getIntent().getExtras();
        if (data != null){
            if (data.containsKey("address")){
                address = (String)data.get("address");
                name = (String)data.get("name");
            }
        }

        TextView restName = findViewById(R.id.rest_name);
        TextView restAddress = findViewById(R.id.rest_address);

        restName.setText(name);
        restAddress.setText(address);

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
        new WebserviceCoords().execute(address);
        LatLng montreal = new LatLng(45.5017, 73.5673);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(montreal));
    }

    public void onBackClick(View v){
        finish();
    }

    private class WebserviceCoords extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String response = "";

            try{
                String address = strings[0].toString();

                Log.i("QUERY:", address);
                address = address.replaceAll(" ", "%20");
                Log.i("QUERY:", address);
                URL url = new URL("http://foodbarbaz.onthewifi.com:9090/foodbarbaz-api/myGeoCode/" + address);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "");
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(inputStream));

                String myLine;
                StringBuilder strBuilder= new StringBuilder();

                while((myLine = responseBuffer.readLine()) != null) {
                    Log.i("Content: ", myLine);
                    strBuilder.append(myLine);
                }

                Log.i("CONTENT", response);
                response = strBuilder.toString();

            }
            catch(Exception e){
                Log.e("URL EXCEPTION", e.toString());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("QUERY:", s);

            if (s != null){
                String[] strings = s.split("/");
                lat = Double.parseDouble(strings[0]);
                lon = Double.parseDouble(strings[1]);

                LatLng restaurant = new LatLng(lat, lon);

                // mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurant));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurant, 18.0f));
                mMap.addMarker(new MarkerOptions().position(restaurant).title(address));
            }
        }
    }
}
