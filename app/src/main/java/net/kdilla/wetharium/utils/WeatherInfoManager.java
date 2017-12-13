package net.kdilla.wetharium.utils;

import android.content.Context;

import net.kdilla.wetharium.DB.WeatherDataSource;
import net.kdilla.wetharium.DB.WeatherNote;
import net.kdilla.wetharium.MainActivity;

import java.util.List;

/**
 * Created by avetc on 13.12.2017.
 */

public class WeatherInfoManager {


    public List<WeatherNote> getElements() {
        return elements;
    }

    public WeatherDataSource getNotesDataSource() {
        return notesDataSource;
    }

    private List<WeatherNote> elements;
    private WeatherDataSource notesDataSource;
    private String city;
    private int temperature;
    private int pressure;
    private int humidity;
    private int wind;
    private String description;

//    public WeatherInfoManager(String city,
//                              int temperature,
//                              int pressure,
//                              int humidity,
//                              int wind,
//                              String description,
//                              int icoId) {
//
//        this.city = city;
//        this.temperature = temperature;
//        this.pressure = pressure;
//        this.humidity = humidity;
//        this.wind = wind;
//        this.description = description;
//        this.icoId = icoId;
//
//    }
    private Context context;
    public WeatherInfoManager(Context context){
        this.context=context;
        notesDataSource = new WeatherDataSource(context);
        notesDataSource.open();
        elements = notesDataSource.getAllNotes();
    }
    public String getCity() {
        return city;
    }

    public void setParams(String city,
                          int temperature,
                          int pressure,
                          int humidity,
                          int wind,
                          String description,
                          int icoId){
        this.city = city;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.description = description;
        this.icoId = icoId;

    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcoId() {
        return icoId;
    }

    public void setIcoId(int icoId) {
        this.icoId = icoId;
    }

    private int icoId;

}
