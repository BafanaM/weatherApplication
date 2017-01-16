package services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.bafana.weatherapplication.MainActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bafanamankahla on 2017/01/15.
 */

public class NetworkService extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private final Context context;

    public NetworkService(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Weather Application");
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int data = inputStreamReader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;

                data = inputStreamReader.read();
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherData = new JSONObject(jsonObject.getString("main"));
            Double temperature = Double.parseDouble(weatherData.getString("temp"));

            int tempIn = (int) (temperature * 1.8 - 459.67);
            int tempInC = ((tempIn -32) * 100/180);
            String placeName = jsonObject.getString("name");

            MainActivity.tvTemperature.setText(String.valueOf(tempInC) + " C");
            MainActivity.tvCity.setText(placeName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
