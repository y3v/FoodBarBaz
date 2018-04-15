package yevoli.release.yev.foodbarbaz;

import android.content.Intent;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import POJO.ActivityStarter;
import POJO.User;

public class RegisterUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    TextInputEditText username;
    TextInputEditText password;
    TextInputEditText password2;
    TextInputEditText firstname;
    TextInputEditText lastname;
    TextInputEditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //Initialize the Drawer Layout
        toolBar = findViewById(R.id.include);
        setSupportActionBar(toolBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Find Views
        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerPassword);
        password2 = findViewById(R.id.registerPasswordConfirm);
        firstname = findViewById(R.id.registerFirstname);
        lastname = findViewById(R.id.registerLastname);
        email = findViewById(R.id.registerEmail);

        //Buttons
        Button registerButton = findViewById(R.id.buttonRegister);
        Button registerLoginButton = findViewById(R.id.buttonRegLogin);

        //Listeners

        //To register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setError(null);
                password.setError(null);
                password2.setError(null);
                firstname.setError(null);
                lastname.setError(null);
                email.setError(null);
                if (!validateFormat(username.getText().toString(),
                        password.getText().toString(),
                        password2.getText().toString(),
                        firstname.getText().toString(),
                        lastname.getText().toString(),
                        email.getText().toString())){

                    //Create call to webservice
                    sendPost();
                }
            }
        });

        //Return to Login Page
        registerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });



    }

    private boolean validateFormat(String username, String password, String password2, String firstname, String lastname, String email){
        boolean ret = false;
        if (TextUtils.isEmpty(username)){
            this.username.setError(getString(R.string.empty_username));
            ret = true;
        }
        if (TextUtils.isEmpty(password)){
            this.password.setError(getString(R.string.empty_username));
            ret = true;
        }
        if (TextUtils.isEmpty(password2)){
            this.password2.setError(getString(R.string.empty_username));
            ret = true;
        }
        if (!password.equals(password2)){
            this.password.requestFocus();
            this.password.setError(getString(R.string.password_match));
        }
        if (TextUtils.isEmpty(firstname)){
            this.firstname.setError(getString(R.string.empty_username));
            ret = true;
        }
        if (TextUtils.isEmpty(lastname)){
            this.lastname.setError(getString(R.string.empty_username));
            ret = true;
        }
        if (TextUtils.isEmpty(email)){
            this.email.setError(getString(R.string.empty_username));
            ret = true;
        }
        return ret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_not_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActivityStarter.OptionsItemsSelected(this, null, item, drawerLayout);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ActivityStarter.NavigationItemSelected(this, null, item);

        return false;
    }

    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            Long response ;

            @Override
            public void run() {
                try {
                    URL url = new URL("https://foodbarbaz.herokuapp.com/register");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", username.getText().toString());
                    jsonParam.put("password", password.getText().toString());
                    jsonParam.put("firstname", firstname.getText().toString());
                    jsonParam.put("lastname", lastname.getText().toString());
                    jsonParam.put("email", email.getText().toString());


                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();

                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    response = Long.parseLong(reader.readLine());

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    if (conn.getResponseCode() == 200){
                        //ID is auto-generated so I pass a 0 because constructor needs it
                        User user = new User(response, username.getText().toString(),
                                password.getText().toString(),
                                firstname.getText().toString(),
                                lastname.getText().toString(),
                                email.getText().toString());

                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                username.requestFocus();
                                username.setError(getString(R.string.connection_error));
                            }
                        });
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
