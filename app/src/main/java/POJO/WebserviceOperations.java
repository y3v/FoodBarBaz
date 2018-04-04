package POJO;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
            String postalCode = postalCodes[0].toString();
            Log.i("QUERY:", postalCode);
            postalCode = postalCode.replaceAll(" ", "%20");
            Log.i("QUERY:", postalCode);
            URL url = new URL("https://foodbarbaz.herokuapp.com/myRestaurants/" + postalCode);
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
