package com.ddr.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity {

    String cityNameFromMain = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        LinearLayout cityLayout = (LinearLayout) findViewById(R.id.city);
        LinearLayout temperatureLayout = (LinearLayout) findViewById(R.id.tempUnitsChange);
        cityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectCityDialogClicked(v);
            }
        });

        temperatureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertTemperatureDialogClicked(v);
            }
        });
    }

    public void showSelectCityDialogClicked(View view) {
        final View cityLayout = getLayoutInflater().inflate(R.layout.choose_city, null);
        EditText editCity = (EditText) cityLayout.findViewById(R.id.editCity);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("City");
        builder.setView(cityLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent openFirstActivity = new Intent(MainActivity3.this, MainActivity.class);
                openFirstActivity.putExtra("cityName", editCity.getText().toString());
                startActivity(openFirstActivity);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void convertTemperatureDialogClicked(View view) {
        final View temperatureLayout = getLayoutInflater().inflate(R.layout.select_temperature_format, null);

        RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiogroup);

        RadioButton celsiusButton = (RadioButton) temperatureLayout.findViewById(R.id.radioButtonCelsius);
        RadioButton fahrenheitButton = (RadioButton) temperatureLayout.findViewById(R.id.radioButtonFahrenheit);

        cityNameFromMain = getIntent().getStringExtra("cityName");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Temperature Unit");
        builder.setView(temperatureLayout);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        Intent openFirstActivity = new Intent(MainActivity3.this, MainActivity.class);

        switch(view.getId()) {
            case R.id.radioButtonCelsius:
                if (checked)
                    openFirstActivity.putExtra("isFahrenheit", "No");
                    openFirstActivity.putExtra("cityFromTempConvert", cityNameFromMain);
                    startActivity(openFirstActivity);
                break;
            case R.id.radioButtonFahrenheit:
                if (checked)
                    openFirstActivity.putExtra("isFahrenheit", "Yes");
                    openFirstActivity.putExtra("cityFromTempConvert", cityNameFromMain);
                    startActivity(openFirstActivity);
                break;
        }
    }
}