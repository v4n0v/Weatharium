package net.kdilla.wetharium.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.kdilla.wetharium.R;

public class WeatherInfoFragment extends Fragment{
    private String temperature;
    private String additionalInfo;
    private String city;

    TextView cityTextView;
    TextView additionalTextView;
    TextView temperatureTextView;
    Drawable icon;

    ImageView weatherImage;

    public void setParams(String city,  String temperature, String additionalInfo, Drawable icon) {
        this.temperature = temperature;
        this.additionalInfo = additionalInfo;
        this.city = city;
        this.icon=icon;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =inflater.inflate(R.layout.weather_info_fragment, container, false);
        initViews(view);

        cityTextView.setText(city);
        additionalTextView.setText(additionalInfo);
        temperatureTextView.setText(temperature);
        weatherImage.setImageDrawable(icon);
        return  view;
    }


    void initViews(  View view ){
        cityTextView = view.findViewById(R.id.info_city_tv);
        additionalTextView = view.findViewById(R.id.info_additional_info_tv);
        temperatureTextView = view.findViewById(R.id.info_temperature_tv);
        weatherImage = view.findViewById(R.id.info_weather_ico);
    }
}
