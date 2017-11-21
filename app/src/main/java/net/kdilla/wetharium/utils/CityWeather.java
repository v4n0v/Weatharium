package net.kdilla.wetharium.utils;

public class CityWeather {


    private String city;
    private String temperature;
    private String pressure;
    private String wind;
    private String something;



    private int imageResourceId;

    private String[] cityArr;
    private String[] weatherArr;

    public CityWeather(String city, String temperature) {
        this.city = city;
        this.temperature = temperature;

    }

    public String getCity() {
        return city;
    }

    public String getTemperature() {
        return temperature;
    }

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

    public String getSomething() {
        return something;
    }

    public void setSomething(String something) {
        this.something = something;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
