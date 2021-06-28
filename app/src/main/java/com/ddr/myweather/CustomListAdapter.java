package com.ddr.myweather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> dayList;
    private final List<Bitmap> iconList;
    private final List<String> tempList;

    public CustomListAdapter(Activity context, List<String> days, List<Bitmap> icons, List<String> temps) {
        super(context, R.layout.my_list, days);

        this.context = context;
        this.dayList = days;
        this.iconList = icons;
        this.tempList = temps;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.my_list, null, true);
        TextView dayOfWeek = (TextView) rowView.findViewById(R.id.dayOfWeek);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconView);
        TextView weatherType = (TextView) rowView.findViewById(R.id.weatherType);

        dayOfWeek.setText(dayList.get(position));
        imageView.setImageBitmap(iconList.get(position));
        weatherType.setText(tempList.get(position));
        return rowView;
    }
}