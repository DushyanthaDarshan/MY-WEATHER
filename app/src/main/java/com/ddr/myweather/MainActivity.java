package com.ddr.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String[] text_list = {"nm to mm", "mm to cm", "cm to m", "m to km", "cm to inches", "inches to feet", "km to miles"};
    Integer[] icon_list = {R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3, R.drawable.pic_4,
            R.drawable.pic_1, R.drawable.pic_1, R.drawable.pic_2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FetchData fetchData = new FetchData();
        fetchData.execute();

        CustomListAdapter adapter = new CustomListAdapter(this, text_list, icon_list);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    public class FetchData extends AsyncTask<String, Void, String> {

        String forecastJsonStr;
        ProgressDialog progress = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
        }

        @Override
        protected void onPostExecute(String s) {
//            TextView txtData = (TextView) findViewById(R.id.txtTitle);
//            txtData.setText(forecastJsonStr);

            TextView txtDataTemp = (TextView) findViewById(R.id.txtTemp);
            TextView txtDataDescription = (TextView) findViewById(R.id.txtDescription);
            TextView txtDataFeelsLike = (TextView) findViewById(R.id.txtFeelsLike);

            try {
                JSONObject fullObject = new JSONObject(forecastJsonStr);
                JSONObject main = fullObject.getJSONObject("main");
                txtDataTemp.setText(main.getString("temp"));
                txtDataFeelsLike.setText(main.getString("feels_like"));

                JSONArray weatherArray = fullObject.getJSONArray("weather");
                JSONObject weather_obj = weatherArray.getJSONObject(0);
                txtDataDescription.setText(weather_obj.getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progress.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=piliyandala&appid=32508302e351a0acecbe33ef6efeb52a";
                URL url = new URL(BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line1;

                while ((line1 = reader.readLine()) != null) {
                    buffer.append(line1 + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("Hi", "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Hi", "Error closing stream", e);
                    }
                }
            }
            return forecastJsonStr;
        }
    }
}