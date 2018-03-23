package POJO;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Yev on 2018-03-20.
 */

public abstract class WebServiceUserLogin extends AsyncTask<Object, Void, List<Object>> implements BackgroundTask  {

    public WebServiceUserLogin() {
    }

    public abstract void onBackgroundTaskCompleted(List<Object> objects);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("WEBSERVICE OPERATION", "Fetching data...");
    }

    @Override
    protected List<Object> doInBackground(Object... params) {
        String response = "";
        OutputStream out = null;

        try{
            URL url = new URL("http://foodbarbaz.onthewifi.com:9090/foodbarbaz-api/login");
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "");
            urlConnection.setRequestMethod("POST");
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
