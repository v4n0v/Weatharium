package net.kdilla.wetharium.gson;

/**
 * Created by avetc on 08.12.2017.
 */

public class WeatherGSon {


    public void setCity(String city) {
        this.city = city;
    }


    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }



    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    String humidity;

    public String getHumidity() {
        return humidity;
    }

    public String getCity() {
        return city;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWind() {
        return wind;
    }

    public int getId() {
        return id;
    }

    String city;

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    String temperature;
    String wind;
    String pressure;

    int id;
    public void setId(int id) {
        this.id = id;
    }


    public WeatherGSon(){}
    public WeatherGSon(String city, String temperature, String wind,
                       String pressure, String humidity ) {
        this.city = city;
        this.temperature = temperature;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
    }
}
