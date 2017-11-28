package net.kdilla.wetharium.utils;

import android.content.Context;

import net.kdilla.wetharium.R;


public class GetWeatherFromRes {

    public static  String getWeather(Context context, int pos){
        String[] countriesWeatherList = context.getResources().getStringArray(R.array.temperature);

        return countriesWeatherList[pos];
    }

    public static String[] getWeatherList(Context context){

        return context.getResources().getStringArray(R.array.temperature);
    }
    public static String getCity(Context context, int pos){
        String[] citylist = context.getResources().getStringArray(R.array.city_selection);

        return citylist[pos];
    }

    public static String getAdditionItem(Context context, int pos){
        String[] itemList = context.getResources().getStringArray(R.array.addition_list);

        return itemList[pos];
    }
    public static String[] getCityList(Context context){

        return context.getResources().getStringArray(R.array.city_selection);
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
    public static String getSomething(Context context, int pos){
        int[] stormList = context.getResources().getIntArray(R.array.something);

        return context.getResources().getString(R.string.storm_title)+" "+
                Integer.toString(stormList[pos])+
                context.getResources().getString(R.string.something_dim);
    }

}
