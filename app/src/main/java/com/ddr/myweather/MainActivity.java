package com.ddr.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String[] text_list = {"nm to mm", "mm to cm", "cm to m", "m to km", "cm to inches", "inches to feet", "km to miles"};
    Integer[] icon_list = {R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3, R.drawable.pic_4,
            R.drawable.pic_1, R.drawable.pic_1, R.drawable.pic_2};

    Map<Integer, WeatherModel> weatherModelMap = new HashMap<>();
    String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

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

            TextView dateForCurrentBlock = (TextView) findViewById(R.id.dateForCurrentBock);
            ImageView currentIconView = (ImageView) findViewById(R.id.currentIconView);
            TextView tempForCurrentBock = (TextView) findViewById(R.id.tempForCurrentBock);
            TextView windSpeedCurrentBlock = (TextView) findViewById(R.id.windSpeedCurrentBlock);
            TextView humidityCurrentBlock = (TextView) findViewById(R.id.humidityCurrentBlock);

            try {
                JSONObject fullObject = new JSONObject(forecastJsonStr);
                JSONArray mainArray = fullObject.getJSONArray("list");
                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject weatherOfDay = mainArray.getJSONObject(i);
                    Date date = new Date((long) weatherOfDay.getInt("dt")*1000);

                    WeatherModel weatherModel = new WeatherModel();

                    int dayNumber = date.getDay();
                    weatherModel.setDayOfWeek(daysOfWeek[dayNumber]);
                    weatherModel.setDate(date);
                    weatherModel.setHumidity(weatherOfDay.getDouble("humidity"));
                    weatherModel.setWindSpeed(weatherOfDay.getDouble("speed"));
                    weatherModel.setRainVolume(weatherOfDay.getDouble("rain"));
                    weatherModel.setTemperature(weatherOfDay.getJSONObject("temp").getDouble("day"));

                    JSONArray weatherArray = weatherOfDay.getJSONArray("weather");
                    weatherModel.setWeatherType(weatherArray.getJSONObject(0).getString("main"));
                    weatherModel.setWeatherDescription(weatherArray.getJSONObject(0).getString("description"));
                    weatherModel.setIconId(weatherArray.getJSONObject(0).getString("icon"));

                    if (i == 0) {
                        dateForCurrentBlock.setText(weatherModel.getDayOfWeek());
//                        currentIconView.setImageResource();
                        tempForCurrentBock.setText(weatherModel.getTemperature().toString());
                        windSpeedCurrentBlock.setText(weatherModel.getWindSpeed().toString());
                        humidityCurrentBlock.setText(weatherModel.getHumidity().toString());
                    }

                    weatherModelMap.put(i+1, weatherModel);
                }

//                txtDataTemp.setText(main.getString("temp"));
//                txtDataFeelsLike.setText(main.getString("feels_like"));
//
//                JSONArray weatherArray = fullObject.getJSONArray("weather");
//                JSONObject weather_obj = weatherArray.getJSONObject(0);
//                txtDataDescription.setText(weather_obj.getString("description"));
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
                final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/daily?q=colombo&cnt=7&appid=a18b978603316d47c572d98d52a420f6";
//                final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=piliyandala&appid=32508302e351a0acecbe33ef6efeb52a";
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