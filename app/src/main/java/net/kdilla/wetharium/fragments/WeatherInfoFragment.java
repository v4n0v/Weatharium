package net.kdilla.wetharium.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.kdilla.wetharium.R;
import net.kdilla.wetharium.utils.FileManager;
import net.kdilla.wetharium.utils.FlickrSearch;
import net.kdilla.wetharium.utils.Preferences;
import net.kdilla.wetharium.utils.gson.Weather;
import net.kdilla.wetharium.utils.gson.WeatherDeserializer;
import net.kdilla.wetharium.utils.gson.WeatherMain;
import net.kdilla.wetharium.utils.gson.WeatherMainDeserializer;
import net.kdilla.wetharium.utils.tasks.LastUpdateTask;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class WeatherInfoFragment extends Fragment {


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
    String additionInfo;

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    int weatherId;
    //    private String additionalInfo;
    private String description;

    private String city;

//    TextView cityTextView;
    TextView additionalTextView;
    TextView temperatureTextView;
    TextView descriptionTextView;
    TextView lastUpdTextView;
    Drawable icon;
    private long date;

    public void setLastDate(long lastDate) {
        this.lastDate = lastDate;
    }

    private long lastDate;
    public void setCityImageView(ImageView cityImageView) {
        this.cityImageView = cityImageView;
    }

    ImageView cityImageView;
    ImageView weatherImage;

    String lat;
    String lon;
    FlickrSearch flickrSearch;

    Bitmap cityBitmap;
    BroadcastReceiver br;

    public void setToolbarLayout(CollapsingToolbarLayout collapsingToolbarLayout) {
        this.toolbarLayout = collapsingToolbarLayout;
    }

    CollapsingToolbarLayout toolbarLayout;


    OnFragmentClickListener mainActivity;
    public void onAttach(Context context) {
        mainActivity = (OnFragmentClickListener) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_info, container, false);
        flickrSearch = new FlickrSearch(cityImageView, getContext());

        // cityImageView = view.findViewById(R.id.header);
        initViews(view);
        cityBitmap = FileManager.loadBitmap(getContext(), city);
        if (cityBitmap == null) {
            getAindSetToolbarImage();
        } else {
            cityImageView.setImageBitmap(cityBitmap);
            Toast.makeText(getContext(), "Loaded from storage", Toast.LENGTH_SHORT).show();
            Log.d("DEBUGGG", "Picture loaded from storage");
        }

        temperatureTextView.setText(temperatureFormat(temperature));
        descriptionTextView.setText(description);
        additionalTextView.setText(additionInfo);
        weatherImage.setImageDrawable(getWeatherIcon(weatherId));
        mainActivity.onTitleUpdate(city);
        mainActivity.onDbUpdateWeatherID(city, temperature, pressure, humidity, wind, date, weatherId);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //  flickrSearch.downloadAndSetImage(city);
                temperature = intent.getIntExtra(Preferences.ADD_TEMP, 0);
                humidity = intent.getIntExtra(Preferences.ADD_HUMIDITY, 0);
                wind = intent.getIntExtra(Preferences.ADD_WIND, 0);
                pressure= intent.getIntExtra(Preferences.ADD_PRESSURE, 0);
                String decription = intent.getStringExtra(Preferences.ADD_DESCRIPTION);
                weatherId = intent.getIntExtra(Preferences.ADD_IMAGE_ID, 0);
                cityBitmap = FileManager.loadBitmap(getContext(), city);
                if (cityBitmap == null) {
                    getAindSetToolbarImage();
                } else {
                    cityImageView.setImageBitmap(cityBitmap);
                    Toast.makeText(getContext(), "Loaded from storage", Toast.LENGTH_SHORT).show();
                }
                mainActivity.onTitleUpdate(city);
                 date = System.currentTimeMillis();
//                long agoTime=(date-lastDate)/1000;
//
//                String timeMetric = "seconds";
//                if (agoTime>60*60*24) {
//                    timeMetric = "days";
//                    agoTime /= 60*60*24;
//                }if (agoTime>60*60){
//                    timeMetric = "hours";
//                    agoTime/=60*60;
//                }else if (agoTime>60){
//                    timeMetric = "minutes";
//                    agoTime/=60;
//                }
//
//                lastUpdTextView.setText("Updated: "+String.valueOf(agoTime)+" "+timeMetric+" ago");
            //    cityTextView.setText(city);

                additionalTextView.setText(formatAdditionInfoString());
                temperatureTextView.setText(temperatureFormat(temperature));
                descriptionTextView.setText(decription);
                weatherImage.setImageDrawable(getWeatherIcon(weatherId));

                mainActivity.onDbUpdateWeatherID(city, temperature, pressure, humidity, wind, date, weatherId);

            }

        };

        IntentFilter intFilt = new IntentFilter(Preferences.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        getActivity().registerReceiver(br, intFilt);

        return view;
    }
    public void updateAdditionInfo(String additionalInfo){
        additionalTextView.setText(additionalInfo);

    }
    private String temperatureFormat(float temperature) {
        if (temperature > 0) return "+" + String.valueOf(Math.round(temperature));
        else return String.valueOf(Math.round(temperature));
    }

    private String formatAdditionInfoString(){
        String additionalInfo="";
        if (isPressure) {
            additionalInfo += "Pressure: " + pressure + " " + getString(R.string.pressure_dim) + "\n";
        }
        if (isWind) {
            additionalInfo += "Wind: " + wind + " " + getString(R.string.wind_dim) + "\n";
        }
        if (isHumidity) {
            additionalInfo += "Humidity: " + humidity + " " + getString(R.string.humidity_dim) + "\n";
        }
        return additionalInfo;
    }
    void initViews(View view) {

        additionalTextView = view.findViewById(R.id.info_additional_info_tv);
        temperatureTextView = view.findViewById(R.id.info_temperature_tv);
        descriptionTextView = view.findViewById(R.id.info_description_tv);
        lastUpdTextView = view.findViewById(R.id.tv_last_update);

        weatherImage = view.findViewById(R.id.info_weather_ico);
    }

    public void getAindSetToolbarImage() {
        //  flickrSearch.setLatAndLon(lat, lon);
        flickrSearch.downloadAndSetImage(city);
    }

    private Drawable getWeatherIcon(int id) {
        id = id / 100;
        Drawable ico = null;
        switch (id) {
            case 2:

                ico = getResources().getDrawable(R.drawable.day_thunder);
                break;
            case 3:
                ico = getResources().getDrawable(R.drawable.day_drizzle);
                break;
            case 5:
                ico = getResources().getDrawable(R.drawable.day_rainy);
                break;
            case 6:
                ico = getResources().getDrawable(R.drawable.day_snowie);
                break;
            case 7:
                ico = getResources().getDrawable(R.drawable.day_foggy);
                break;
            case 8:
                ico = getResources().getDrawable(R.drawable.day_cloudly);
                break;

            default:
                break;
        }
        return ico;
    }

    public void setAdditionalParams(String additionInfo) {
        this.additionInfo = additionInfo;
    }
    public void setOptions(boolean isHumidity, boolean isWind, boolean isPressure){
        setIsHumidity(isHumidity);
        setIsWind(isWind);
        setIsPressure(isPressure);
    }
    public boolean isWind() {
        return isWind;
    }

    public void setIsWind(boolean wind) {
        isWind = wind;
    }

    public boolean isPressure() {
        return isPressure;
    }

    public void setIsPressure(boolean pressure) {
        isPressure = pressure;
    }

    public boolean isHumidity() {
        return isHumidity;
    }

    public void setIsHumidity(boolean humidity) {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(long date) {
        this.date = date;
    }
}