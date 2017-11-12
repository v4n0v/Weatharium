package net.kdilla.weatharium.utils;

import android.content.Context;

import net.kdilla.weatharium.R;

/**
 * Created by avetc on 30.10.2017.
 */

public class GetWeatherFromRes {


    public static  String getWeather(Context context, int pos){
        String[] countriesWeatherList = context.getResources().getStringArray(R.array.weather);

        return countriesWeatherList[pos];
    }

    public static String[] getWeatherList(Context context){
        String[] weatherList = context.getResources().getStringArray(R.array.weather);

        return weatherList;
    }
    public static String getCity(Context context, int pos){
        String[] citylist = context.getResources().getStringArray(R.array.city_selection);

        return citylist[pos];
    }
    public static String[] getCityList(Context context){
        String[] countriesList = context.getResources().getStringArray(R.array.city_selection);

        return countriesList;
    }

    public static String getPressure(Context context, int pos){
        int[] pressureList = context.getResources().getIntArray(R.array.pressure);

        return context.getResources().getString(R.string.pressure_title)+" "+
                Integer.toString(pressureList[pos])+
                context.getResources().getString(R.string.pressure_dim);
    }
    public static String getWind(Context context, int pos){
        int[] windList = context.getResources().getIntArray(R.array.wind);


        return context.getResources().getString(R.string.wind_title)+" "+
                Integer.toString(windList[pos])+
                context.getResources().getString(R.string.wind_dim);

    }
    public static String getStorm(Context context, int pos){
        int[] stormList = context.getResources().getIntArray(R.array.storm);

        return context.getResources().getString(R.string.storm_title)+" "+
                Integer.toString(stormList[pos])+
                context.getResources().getString(R.string.storm_dim);
    }
}