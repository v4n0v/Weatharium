package net.kdilla.weatharium;

import android.content.Context;

/**
 * Created by avetc on 30.10.2017.
 */

class WeatherByCountry {
   public String[] countriesList;

    static  String getWeatherInCountry(Context context, int pos){
        String[] countriesWeatherList = context.getResources().getStringArray(R.array.weather);

        return countriesWeatherList[pos];
    }


    static String[] getCountriesList(Context context){
        String[] countriesList = context.getResources().getStringArray(R.array.city_selection);

        return countriesList;
    }

}