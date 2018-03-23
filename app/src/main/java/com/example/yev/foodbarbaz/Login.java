package com.example.yev.foodbarbaz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import POJO.LoginHandler;
import POJO.Restaurant;
import POJO.User;

public class Login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    TextInputEditText username;
    TextInputEditText password;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolBar = findViewById(R.id.include);
        setSupportActionBar(toolBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        username = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);


        Button loginButton = findViewById(R.id.buttonLogin);
        Button regButton = findViewById(R.id.buttonRegFromLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Clear errors
                username.setError(null);
                password.setError(null);
                if (!validateFormat(username.getText().toString(), password.getText().toString())){

                    //Create call to webservice
                    sendPost();
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start Register page
                Intent intent = new Intent(getApplicationContext(), RegisterUser.class);
                startActivity(intent);
            }
        });



    }

    private boolean validateFormat(String username, String password){
        boolean ret = false;
        if (TextUtils.isEmpty(username)){
            this.username.setError(getString(R.string.empty_username));
            ret = true;
        }
        if (TextUtils.isEmpty(password)){
            this.password.setError(getString(R.string.empty_username));
            ret = true;
        }
        return ret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_not_home, menu);

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
            case R.id.search:
                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                intent.putExtra("user",user);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            String response = "";

            @Override
            public void run() {
                try {
                    URL url = new URL("http://foodbarbaz.onthewifi.com:9090/foodbarbaz-api/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", username.getText().toString());
                    jsonParam.put("password", password.getText().toString());


                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();

                    InputStream inputStream = conn.getInputStream();
                    BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(inputStream));

                    String myLine;
                    StringBuilder strBuilder= new StringBuilder();

                    while((myLine = responseBuffer.readLine()) != null) {
                        Log.i("Content: ", myLine);
                        strBuilder.append(myLine);
                    }

                    Log.i("CONTENT", response);
                    response = strBuilder.toString();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Type type = new TypeToken<User>() {}.getType();
                Gson gson = new Gson();
                user = gson.fromJson(response, type);

                if (user != null){
                    //If a value is returned, display the results to console for testing
                    //Start new activity!
                    Log.i("LOGIN UN:" , user.getUsername());
                    Log.i("LOGIN PW:" , user.getPassword());
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            username.requestFocus();
                            username.setError(getString(R.string.Incorrect_login));
                        }
                    });
                }
            }
        });

        thread.start();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, Settings.class);
        String buttonPressed = "";
        int id = item.getItemId();

        Log.i("" + id, "jasldkfj");
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

            case R.id.report_drawer:
                buttonPressed = "Report";
                break;
        }

        intent.putExtra("navigation", buttonPressed);
        intent.putExtra("user", user);
        startActivity(intent);

        return false;
    }
}
