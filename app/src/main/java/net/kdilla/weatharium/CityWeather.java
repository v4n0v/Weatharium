package net.kdilla.weatharium;

import android.content.Context;

/**
 * Created by avetc on 12.11.2017.
 */

public class CityWeather {


    private String city;
    private String weather;


    private String pressure;
    private String wind;
    private String storm;

    private String[] cityArr;
    private String[] weatherArr;

    public CityWeather(String city, String weather) {
        this.city = city;
        this.weather = weather;

    }

    public String getCity() {
        return city;
    }

    public String getWeather() {
        return weather;
    }

    //
//    static final CityWeather[] cityWeatherList = {
//       new CityWeather("Москва", "+5"),
//            new CityWeather("Питер", "+5"),
//            new CityWeather("Лондон", "+5"),
//            new CityWeather("Париж", "+5"),
//            new CityWeather("Гонк-Конг", "+5"),
//
//    } ;
    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getStorm() {
        return storm;
    }

    public void setStorm(String storm) {
        this.storm = storm;
    }

}
