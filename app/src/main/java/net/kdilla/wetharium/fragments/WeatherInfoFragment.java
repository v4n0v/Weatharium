package net.kdilla.wetharium.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.kdilla.wetharium.MainActivity;
import net.kdilla.wetharium.R;
import net.kdilla.wetharium.utils.WeatherDataLoader;
import net.kdilla.wetharium.utils.gson.Weather;
import net.kdilla.wetharium.utils.gson.WeatherDeserializer;
import net.kdilla.wetharium.utils.gson.WeatherMain;
import net.kdilla.wetharium.utils.gson.WeatherMainDeserializer;

import org.json.JSONObject;

/**
 * Created by avetc on 10.12.2017.
 */

public class WeatherInfoFragment extends Fragment {

    OnFragmentClickListener mainActivity;
    Handler handler = new Handler();

    private boolean isWind = true;
    private boolean isPressure = true;
    private boolean isHumidity = true;
    SharedPreferences preferences;


    // String cityText;



    int temperature;
    int pressure;
    int wind;
    int humidity;


    //    private String additionalInfo;
    private String description;

    private String city;

    TextView cityTextView;
    TextView additionalTextView;
    TextView temperatureTextView;
    TextView descriptionTextView;
    Drawable icon;


    ImageView weatherImage;
//
//    public void setParams(String city,  float temperature, String additionalInfo, String description, Drawable icon) {
//        this.temperature = temperature;
//        this.additionalInfo = additionalInfo;
//        this.city = city;
//        this.icon=icon;
//        this.description=description;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_info, container, false);

        initViews(view);
        getWeather(city);

        return view;
    }

    private String temperatureFormat(float temperature) {

        if (temperature > 0) return "+" + String.valueOf(Math.round(temperature));
        else return String.valueOf(Math.round(temperature));

    }

    void initViews(View view) {
        cityTextView = view.findViewById(R.id.info_city_tv);
        additionalTextView = view.findViewById(R.id.info_additional_info_tv);
        temperatureTextView = view.findViewById(R.id.info_temperature_tv);
        descriptionTextView = view.findViewById(R.id.info_description_tv);

//        descriptionTextView.setTypeface(Preferences.fontOswaldLight(getActivity().getAssets()));
//        additionalTextView.setTypeface(Preferences.fontOswaldLight(getActivity().getAssets()));
//        temperatureTextView.setTypeface(Preferences.fontOswaldLight(getActivity().getAssets()));
//        cityTextView.setTypeface(Preferences.fontOswaldLight(getActivity().getAssets()));

        weatherImage = view.findViewById(R.id.info_weather_ico);
    }

    private void renderWeather(JSONObject json) {
        try {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Weather.class, new WeatherDeserializer())
                    .registerTypeAdapter(WeatherMain.class, new WeatherMainDeserializer())

                    .create();
            Weather weatherForGSon = gson.fromJson(json.toString(), Weather.class);

            temperature = weatherForGSon.getTemperature();
            pressure = weatherForGSon.getPressure();
            wind = weatherForGSon.getWind();
            humidity = weatherForGSon.getHumidity();
            city = weatherForGSon.getCity();

            String additionalInfo = "";

            if (isPressure) {
                additionalInfo += "Pressure: " + pressure + " " + getString(R.string.pressure_dim) + "\n";
            }
            if (isWind) {
                additionalInfo += "Wind: " + wind + " " + getString(R.string.wind_dim) + "\n";
            }
            if (isHumidity) {
                additionalInfo += "Humidity: " + humidity + " " + getString(R.string.humidity_dim) + "\n";
            }
//
//            temperature = weatherForGSon.getTemperature();
//            description=weatherForGSon.getMainInfo();
//            Drawable weatherIcon = getWeatherIcon(weatherForGSon.getId());

            Log.i("GSON", gson.toJson(weatherForGSon));

            cityTextView.setText(weatherForGSon.getCity());
            additionalTextView.setText(additionalInfo);
            temperatureTextView.setText(temperatureFormat(weatherForGSon.getTemperature()));
            descriptionTextView.setText(weatherForGSon.getMainInfo());

            weatherImage.setImageDrawable(getWeatherIcon(weatherForGSon.getId()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getWeather(final String city) {
        this.city=city;
        new Thread() {
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getActivity().getApplicationContext(), city);

                if (jsonObject != null) {
                    Log.d("TAG", jsonObject.toString());
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(jsonObject);

                        }
                    });
                }
            }
        }.start();
    }

    private Drawable getWeatherIcon(int id) {
        id=id/100;
        Drawable ico = null;
        switch (id) {
            case 2:

                ico =getResources().getDrawable(R.drawable.day_thunder);
                break;
            case 3:
                ico =getResources().getDrawable(R.drawable.day_drizzle);
                break;
            case 5:
                ico = getResources().getDrawable(R.drawable.day_rainy);
                break;
            case 6:
                ico =getResources().getDrawable(R.drawable.day_snowie);
                break;
            case 7:
                ico =getResources().getDrawable(R.drawable.day_foggy);
                break;
            case 8:
                ico = getResources().getDrawable(R.drawable.day_cloudly);
                break;

            default:
                break;
        }
        return ico;
    }

    public void setAdditionalParams(boolean isWind, boolean isPressure, boolean isHumidity) {
        this.isHumidity = isHumidity;
        this.isPressure = isPressure;
        this.isWind = isWind;
    }

    public boolean isWind() {
        return isWind;
    }

    public void setWind(boolean wind) {
        isWind = wind;
    }

    public boolean isPressure() {
        return isPressure;
    }

    public void setPressure(boolean pressure) {
        isPressure = pressure;
    }

    public boolean isHumidity() {
        return isHumidity;
    }

    public void setHumidity(boolean humidity) {
        isHumidity = humidity;
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

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}