package com.example.yan.yanweather.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yan.yanweather.R;
import com.example.yan.yanweather.model.Weather;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * Created by Yan on 10/19/2015.
 */
public class WeatherListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] dates;
    private final String[] iconURL;
    private final String[] description;
    private final float[] LTemperature;
    private final float[] HTemperature;
    private final String unit;

    public WeatherListAdapter(Activity context,Weather weather, int days, String unit){
        super(context, R.layout.weather_list,weather.mDates);
        this.context = context;
        this.dates = Arrays.copyOfRange(weather.mDates, 0,days);
        this.iconURL =Arrays.copyOfRange( weather.mIconURL, 0,days);
        this.description =Arrays.copyOfRange( weather.mDescrip, 0,days);

        if("Fahrenheit".equals(unit)){
            this.unit = " F";
            this.HTemperature = Arrays.copyOfRange(weather.mHTempF, 0,days);
            this.LTemperature = Arrays.copyOfRange(weather.mLTempF, 0,days);
        }
        else {
            this.unit = " C";
            this.HTemperature = Arrays.copyOfRange(weather.mHTempC, 0,days);
            this.LTemperature = Arrays.copyOfRange(weather.mLTempC, 0,days);
        }
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.weather_list, null, true);

        TextView vDate = (TextView) rowView.findViewById(R.id.date);
        ImageView vIcon = (ImageView) rowView.findViewById(R.id.w_icon);
        TextView vDescrip = (TextView) rowView.findViewById(R.id.description);
        vDescrip.setMaxWidth(((RelativeLayout)vDescrip.getParent()).getWidth()/4);
        TextView vTemp = (TextView) rowView.findViewById(R.id.temperature);
        vDate.setText(dates[position]);
        Picasso.with(context)
                .load(iconURL[position])
                .into(vIcon);
        vDescrip.setText(description[position]);
        vTemp.setText(HTemperature[position]+" - "+LTemperature[position]+ this.unit);

        return rowView;
    }
}
