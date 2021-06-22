package com.ddr.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle bundle = getIntent().getExtras();
        WeatherModel weatherModel = bundle.getParcelable("weatherObject");
//        Toast.makeText(getApplicationContext(), weatherModel.getDayOfWeek(), Toast.LENGTH_LONG).show();
    }
}