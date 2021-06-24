package com.ddr.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity3 extends AppCompatActivity {

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
}