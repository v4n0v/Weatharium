package net.kdilla.wetharium.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import net.kdilla.wetharium.R;
import net.kdilla.wetharium.utils.FileManager;
import net.kdilla.wetharium.utils.FlickrSearch;
import net.kdilla.wetharium.utils.Preferences;

import java.text.SimpleDateFormat;


public class WeatherInfoFragment extends Fragment {

    private boolean isWind = true;
    private boolean isPressure = true;
    private boolean isHumidity = true;

    private int temperature;
    private int tempMin;
    private int tempMax;
    private int pressure;
    private int wind;
    private int humidity;
    private String additionInfo;

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    private int weatherId;

    private String description;

    private String city;

    private TextView additionalTextView;
    private TextView temperatureTextView;
    private TextView descriptionTextView;
    private TextView lastUpdTextView;
    private TextView pressureTextView;
    private TextView windTextView;
    private TextView humidityTextView;
    private TextView tempMinMaxTextView;

    private long date;

    public void setCityImageView(ImageView cityImageView) {
        this.cityImageView = cityImageView;
    }

    private ImageView cityImageView;
    private ImageView weatherImage;
    private String CELCIUM;
    private String lat;
    private String lon;
    private FlickrSearch flickrSearch;

    boolean isOk;
    private Bitmap cityBitmap;
    private BroadcastReceiver br;

    public void setToolbarLayout(CollapsingToolbarLayout collapsingToolbarLayout) {
        this.toolbarLayout = collapsingToolbarLayout;
    }

    private CollapsingToolbarLayout toolbarLayout;

    OnFragmentListener mainActivity;

    public void onAttach(Context context) {
        mainActivity = (OnFragmentListener) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_info, container, false);
        flickrSearch = new FlickrSearch(cityImageView, getContext());
        CELCIUM = getString(R.string.cels);

        initViews(view);

        refresh();

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isOk = intent.getBooleanExtra(Preferences.ADD_IS_OK, true);

                if (isOk) {
                    city = intent.getStringExtra(Preferences.ADD_CITY);

                    temperature = intent.getIntExtra(Preferences.ADD_TEMP, 0);
                    humidity = intent.getIntExtra(Preferences.ADD_HUMIDITY, 0);
                    wind = intent.getIntExtra(Preferences.ADD_WIND, 0);
                    pressure = intent.getIntExtra(Preferences.ADD_PRESSURE, 0);
                    description = intent.getStringExtra(Preferences.ADD_DESCRIPTION);
                    weatherId = intent.getIntExtra(Preferences.ADD_IMAGE_ID, 0);
                    tempMax = intent.getIntExtra(Preferences.ADD_TEMP_MAX, 0);
                    tempMin = intent.getIntExtra(Preferences.ADD_TEMP_MIN, 0);

                    refresh();
                } else {
                    Toast.makeText(getContext(), "Load weather error", Toast.LENGTH_SHORT).show();
                }

            }

        };

        IntentFilter intFilt = new IntentFilter(Preferences.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        getActivity().registerReceiver(br, intFilt);

        return view;
    }

    public void updateAdditionInfo(String additionalInfo) {
        additionalTextView.setText(additionalInfo);

    }

    private void refresh() {
        // получаем картинку города, если она закеширована, то из памяти, если нет, то качаем новую
        cityBitmap = FileManager.loadBitmap(getContext(), city);
        if (cityBitmap == null) {
            getAndSetToolbarImage();
        } else {
            cityImageView.setImageBitmap(cityBitmap);
        //    Toast.makeText(getContext(), "Loaded from storage", Toast.LENGTH_SHORT).show();
            Log.d("DEBUGGG", "Picture loaded from storage");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM' at ' HH:mm");

        String time = "Updated: " + dateFormat.format(System.currentTimeMillis());
        String pressureInfo = pressure + getString(R.string.pressure_dim);
        String windInfo = wind + getString(R.string.wind_dim);
        String humInfo = humidity + getString(R.string.humidity_dim);

        pressureTextView.setText(pressureInfo);
        windTextView.setText(windInfo);
        humidityTextView.setText(humInfo);
        lastUpdTextView.setText(time);
        String minMax = Preferences.temperatureFormat(tempMin) + CELCIUM + " " + Preferences.temperatureFormat(tempMax) + CELCIUM;
        tempMinMaxTextView.setText(minMax);


        if (weatherId == 800) description = getString(R.string.clear);
        else description = Preferences.getWeatherDescription(weatherId, getContext());

        temperatureTextView.setText(Preferences.temperatureFormat(temperature) + CELCIUM);
        descriptionTextView.setText(description);

        weatherImage.setImageDrawable(Preferences.getWeatherIcon(weatherId, getContext()));

        mainActivity.onTitleUpdate(city);
        mainActivity.onDbUpdateWeatherID(city, temperature, pressure, humidity, wind, date, weatherId);
    }

    private String temperatureFormat(float temperature) {
        if (temperature > 0) return "+" + String.valueOf(Math.round(temperature));
        else return String.valueOf(Math.round(temperature));
    }

    private String formatAdditionInfoString() {
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
        return additionalInfo;
    }

    void initViews(View view) {
        //  additionalTextView = view.findViewById(R.id.info_additional_info_tv);
        temperatureTextView = view.findViewById(R.id.info_temperature_tv);
        descriptionTextView = view.findViewById(R.id.info_description_tv);
        lastUpdTextView = view.findViewById(R.id.tv_last_update);
        weatherImage = view.findViewById(R.id.info_weather_ico);
        pressureTextView = view.findViewById(R.id.info_pressure);
        windTextView = view.findViewById(R.id.info_wind);
        humidityTextView = view.findViewById(R.id.info_humidity);
        tempMinMaxTextView = view.findViewById(R.id.info_min_max);
    }

    public void getAndSetToolbarImage() {
        flickrSearch.downloadAndSetImage(city);
    }

    public void configure(String city, int temp, int pressure, int wind, int hum, int tempMin, int tempMax, String description, int weatherId, String additionInfo) {
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.city = city;
        this.temperature = temp;
        this.pressure = pressure;
        this.wind = wind;
        this.humidity = hum;
        this.description = description;
        this.weatherId = weatherId;
        this.additionInfo = additionInfo;
    }

    public void setAdditionalParams(String additionInfo) {
        this.additionInfo = additionInfo;
    }

    public void setOptions(boolean isHumidity, boolean isWind, boolean isPressure) {
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