package com.ddr.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Map<Integer, WeatherModel> weatherModelMap = new HashMap<>();
    Map<String, Integer> weatherIconMap = new HashMap<>();
    List<String> daysList = new ArrayList<>();
    List<Integer> iconList = new ArrayList<>();
    List<Bitmap> bitmapIconList = new ArrayList<>();
    List<String> tempList = new ArrayList<>();
    String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

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
        String city = null;
        String isFahrenheit = null;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            city = bundle.getString("cityName");
            isFahrenheit = bundle.getString("isFahrenheit");
        }
        fetchData.execute(city, isFahrenheit);
    }

    private void executeListView() {
        CustomListAdapter adapter = new CustomListAdapter(this, daysList, bitmapIconList, tempList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openSecondActivity = new Intent(MainActivity.this, MainActivity2.class);
                WeatherModel weatherModel = weatherModelMap.get(position+1);
                openSecondActivity.putExtra("weatherObject", weatherModel);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bmp = bitmapIconList.get(position);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                openSecondActivity.putExtra("image",byteArray);

                startActivity(openSecondActivity);
            }
        });
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("No data found for the provided city related");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Activity activity = null;
                activity.finish();
                Intent openFirstActivity = getIntent();
                startActivity(openFirstActivity);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class FetchData extends AsyncTask<String, Void, String> {

        String forecastJsonStr;
        String isFahrenheit;
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
            TextView dateForCurrentBlock = (TextView) findViewById(R.id.dateForCurrentBock);
            TextView weatherPlace = (TextView) findViewById(R.id.weatherPlace);
            TextView tempForCurrentBock = (TextView) findViewById(R.id.tempForCurrentBock);
            ImageView currentIconView = (ImageView) findViewById(R.id.currentIconView);
            TextView windSpeedCurrentBlock = (TextView) findViewById(R.id.windSpeedCurrentBlock);
            TextView humidityCurrentBlock = (TextView) findViewById(R.id.humidityCurrentBlock);

            try {
                if (forecastJsonStr != null) {
                    JSONObject fullObject = new JSONObject(forecastJsonStr);
                    JSONObject cityObject = fullObject.getJSONObject("city");
                    JSONArray mainArray = fullObject.getJSONArray("list");
                    for (int i = 0; i < mainArray.length(); i++) {
                        JSONObject weatherOfDay = mainArray.getJSONObject(i);
                        Date date = new Date((long) weatherOfDay.getInt("dt")*1000);

                        WeatherModel weatherModel = new WeatherModel();

                        if (weatherOfDay != null) {
                            int dayNumber = date.getDay();
                            weatherModel.setDayOfWeek(daysOfWeek[dayNumber]);
                            weatherModel.setDate(date);
                            weatherModel.setCityName(cityObject.getString("name"));
                            weatherModel.setCountry(cityObject.getString("country"));
                            weatherModel.setPopulation(cityObject.getInt("population"));
                            weatherModel.setTimezone(cityObject.getInt("timezone"));
                            weatherModel.setLon(cityObject.getJSONObject("coord").getDouble("lon"));
                            weatherModel.setLat(cityObject.getJSONObject("coord").getDouble("lat"));
                            weatherModel.setHumidity(weatherOfDay.getDouble("humidity"));
                            weatherModel.setRainVolume(weatherOfDay.getDouble("rain"));

                            if (isFahrenheit.equals("Imperial")) {
                                weatherModel.setTemperature(weatherOfDay.getJSONObject("temp").getString("day") + " ℉");
                                weatherModel.setWindSpeed(weatherOfDay.getString("speed") + " mi/h");
                            } else {
                                weatherModel.setTemperature(weatherOfDay.getJSONObject("temp").getString("day") + " ℃");
                                weatherModel.setWindSpeed(weatherOfDay.getString("speed") + " m/s");
                            }

                            JSONArray weatherArray = weatherOfDay.getJSONArray("weather");
                            weatherModel.setWeatherType(weatherArray.getJSONObject(0).getString("main"));
                            weatherModel.setWeatherDescription(weatherArray.getJSONObject(0).getString("description"));
                            weatherModel.setIconId(weatherArray.getJSONObject(0).getString("icon"));
                            weatherModel.setIconNumber(weatherIconMap.get(weatherModel.getWeatherType()));

                            FetchIcons fetchIcons = new FetchIcons();
                            Bitmap bitmap = fetchIcons.execute(weatherModel.getIconId()).get();

                            if (i == 0) {
                                dateForCurrentBlock.setText(weatherModel.getDayOfWeek());
                                currentIconView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 500, true));
                                tempForCurrentBock.setText(weatherModel.getTemperature());
                                weatherPlace.setText(weatherModel.getCityName().toUpperCase());
                                windSpeedCurrentBlock.setText(weatherModel.getWindSpeed());
                                humidityCurrentBlock.setText(weatherModel.getHumidity().toString() + "%");
                            }
                            weatherModelMap.put(i+1, weatherModel);

                            daysList.add(daysOfWeek[dayNumber]);
                            iconList.add(weatherIconMap.get(weatherModel.getWeatherType()));
                            bitmapIconList.add(Bitmap.createScaledBitmap(bitmap, 500, 500, true));
                            tempList.add(weatherModel.getTemperature());
                        }
                    }
                } else {
                    alertDialog();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            executeListView();
            progress.dismiss();
        }

        @Override
        protected String doInBackground(String... stringsArray) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String city;
            String unit;

            if (stringsArray.length == 0 || stringsArray[0] == null) {
                city = "piliyandala";
            } else {
                city = stringsArray[0];
            }

            if (stringsArray.length == 0 || stringsArray[1] == null || stringsArray[1].equals("No")) {
                unit = "Metric";
                isFahrenheit = "Metric";
            } else {
                unit = "Imperial";
                isFahrenheit = "Imperial";
            }

            try {
                final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&cnt=7&appid=a18b978603316d47c572d98d52a420f6&units=" + unit;
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

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settings){
            Intent openSettingsActivity = new Intent(MainActivity.this, MainActivity3.class);
            startActivity(openSettingsActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchIcons extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap image = null;
            try {
                String iconId = null;
                if (strings.length != 0) {
                    iconId = strings[0];
                }
                String baseUrl = "https://openweathermap.org/img/wn/" + iconId +"@2x.png";
                URL url = new URL(baseUrl);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
            }
            return image;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Exit");
        dialog.setMessage("Do you want to exit from the app? ");
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                System.exit(0);
            }
        });
        dialog.show();
    }
}