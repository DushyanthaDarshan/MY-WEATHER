package com.ddr.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        WeatherModel weatherModel = (WeatherModel) getIntent().getSerializableExtra("weatherObject");

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        TextView secondBlockDate = (TextView) findViewById(R.id.secondBlockDate);
        TextView secondBlockLocation = (TextView) findViewById(R.id.secondBlockLocation);
        ImageView secondBlockIcon = (ImageView) findViewById(R.id.secondBlockIcon);
        TextView secondBlockTemp = (TextView) findViewById(R.id.secondBlockTemp);
        TextView secondBlockDescription = (TextView) findViewById(R.id.secondBlockDescription);
        TextView secondBlockHumidity = (TextView) findViewById(R.id.secondBlockHumidity);

        if (weatherModel != null) {
            secondBlockDate.setText(weatherModel.getDayOfWeek());
            String location = weatherModel.getCityName() + ", " + weatherModel.getCountry();
            secondBlockLocation.setText(location);
            secondBlockIcon.setImageBitmap(bmp);
            secondBlockTemp.setText(weatherModel.getTemperature().toString());
            secondBlockDescription.setText(weatherModel.getWeatherDescription().toUpperCase());
            secondBlockHumidity.setText("Humidity: " + weatherModel.getHumidity().toString() + "%");
        }
    }
}