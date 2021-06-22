package com.ddr.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//    String[] text_list = {"nm to mm", "mm to cm", "cm to m", "m to km", "cm to inches", "inches to feet", "km to miles"};
//    Integer[] icon_list = {R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3, R.drawable.pic_4,
//            R.drawable.pic_1, R.drawable.pic_1, R.drawable.pic_2};

    Map<Integer, WeatherModel> weatherModelMap = new HashMap<>();
    Map<String, Integer> weatherIconMap = new HashMap<>();
    List<String> daysList = new ArrayList<>();
    List<Integer> iconList = new ArrayList<>();
    List<String> tempList = new ArrayList<>();
    String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    Boolean isBackgroundFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherIconMap.put("Thunderstorm", R.drawable.thunderstorm);
        weatherIconMap.put("Clouds", R.drawable.clouds);
        weatherIconMap.put("Clear", R.drawable.sunny);
        weatherIconMap.put("Snow", R.drawable.snow);
        weatherIconMap.put("Rain", R.drawable.rain);
        weatherIconMap.put("Drizzle", R.drawable.drizzle);

        FetchData fetchData = new FetchData();
        fetchData.execute();
    }

    private void executeListView() {
        CustomListAdapter adapter = new CustomListAdapter(this, daysList, iconList, tempList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = text_list[+position];
//                Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();
                Intent openSecondActivity = new Intent(MainActivity.this, MainActivity2.class);
                openSecondActivity.putExtra("weatherObject", weatherModelMap.get(+position));
                startActivity(openSecondActivity);
            }
        });
    }

    public class FetchData extends AsyncTask<String, Void, String> {

        String forecastJsonStr;
        ProgressDialog progress = new ProgressDialog(MainActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false);
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

                    if (weatherOfDay != null) {
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
                            currentIconView.setImageResource(weatherIconMap.get(weatherModel.getWeatherType()));
                            tempForCurrentBock.setText(weatherModel.getTemperature().toString());
                            windSpeedCurrentBlock.setText(weatherModel.getWindSpeed().toString());
                            humidityCurrentBlock.setText(weatherModel.getHumidity().toString());
                        }

                        weatherModelMap.put(i+1, weatherModel);

                        daysList.add(daysOfWeek[dayNumber]);
                        iconList.add(weatherIconMap.get(weatherModel.getWeatherType()));
                        tempList.add(weatherModel.getTemperature().toString());
                    }
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
            executeListView();
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