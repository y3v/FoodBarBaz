package POJO;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.example.yev.foodbarbaz.NearbyRestaurantList;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

/**
 * Created by olile on 2018-03-18.
 */

public abstract class WebserviceOperations extends AsyncTask<Object, Void, List<Object>> implements BackgroundTask {


    public WebserviceOperations() {
    }

    public abstract void onBackgroundTaskCompleted(List<Object> objects);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("WEBSERVICE OPERATION", "Fetching data...");
    }

    @Override
    protected List<Object> doInBackground(Object[] postalCodes) {
        String response = "";

        try{
            URL url = new URL("http://192.168.1.20:9090/foodbarbaz-api-0.0.1-SNAPSHOT/myRestaurants/" + postalCodes[0]);
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

        Type listType = new TypeToken<List<Restaurant>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(response, listType);
    }

    @Override
    protected void onPostExecute(List<Object> objects) {
        super.onPostExecute(objects);
        onBackgroundTaskCompleted(objects);
    }
}
