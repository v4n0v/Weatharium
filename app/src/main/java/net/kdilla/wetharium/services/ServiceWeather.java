package net.kdilla.wetharium.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import net.kdilla.wetharium.R;
import net.kdilla.wetharium.utils.Preferences;
import net.kdilla.wetharium.utils.gson.Weather;
import net.kdilla.wetharium.utils.gson.WeatherDeserializer;
import net.kdilla.wetharium.utils.gson.WeatherMain;
import net.kdilla.wetharium.utils.gson.WeatherMainDeserializer;
import net.kdilla.wetharium.utils.tasks.WeatherGetTask;
import net.kdilla.wetharium.widget.WidgetWeather;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ServiceWeather extends Service {

    private Timer timer;
    private TimerTask tTask;

    private String city;
    private int temperature;
    private int pressure;
    private int wind;
    private int humidity;
    private int tempMin;
    private int tempMax;
    private String lat;
    private String lon;
    private String description;
    // обновляю каждые 10 минут
    private long interval = 600_000;

    private WeatherBinder binder = new WeatherBinder();

    private boolean isWind = true;
    private boolean isPressure = true;
    private boolean isHumidity = true;
    private boolean isComplete = true;

    private boolean isOk;
    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        Log.d(Preferences.DEBUG_KEY,  "ServiceWeather created");
        schedule();

    }

    public void setCity(String city) {
        this.city = city;
    }

    public void changeCity(String city) {
        setCity(city);
        isComplete = false;
        schedule();

    }

    private void schedule() {
        if (tTask != null) tTask.cancel();
        if (interval > 0) {
            // начинаем отсчет до 10минут
            tTask = new TimerTask() {
                public void run() {
                    Log.d(Preferences.DEBUG_KEY, "Service weather interval " + interval);
                    if (city != null) {
                        if (!isComplete) {
                            loadWeatherJson();
                        }
                    }
                }
            };
            timer.schedule(tTask, 1000, interval);
        }
    }

    private void loadWeatherJson() {
        WeatherGetTask tak = (WeatherGetTask) new WeatherGetTask(getApplicationContext()).execute(city);

        JSONObject jsonObject = null;
        try {
            jsonObject = tak.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            renderWeather(jsonObject);
        } else {
            Log.d(Preferences.DEBUG_KEY, "ServiceWeather jsonObject=null ");
        }
        isComplete = true;
        Log.d(Preferences.DEBUG_KEY,"ServiceWeather jsonObject load complete");
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
            int imageId = weatherForGSon.getId();
            description = weatherForGSon.getMainInfo();
            tempMin = weatherForGSon.getTempMin();
            tempMax = weatherForGSon.getTempMax();
            lon = String.valueOf(weatherForGSon.getLon());
            lat = String.valueOf(weatherForGSon.getLat());

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

            Log.d("GSON", gson.toJson(weatherForGSon));


            Intent intent = new Intent(Preferences.BROADCAST_ACTION);
            intent.putExtra(Preferences.ADD_CITY, city);
            intent.putExtra(Preferences.ADD_TEMP, temperature);
            intent.putExtra(Preferences.ADD_HUMIDITY, humidity);
            intent.putExtra(Preferences.ADD_WIND, wind);
            intent.putExtra(Preferences.ADD_PRESSURE, pressure);
            intent.putExtra(Preferences.ADD_DESCRIPTION, description);
            intent.putExtra(Preferences.ADD_IMAGE_ID, imageId);
            intent.putExtra(Preferences.ADD_TEMP_MIN, tempMin);
            intent.putExtra(Preferences.ADD_TEMP_MAX, tempMax);
            intent.putExtra(Preferences.ADD_IS_OK, isOk);
            sendBroadcast(intent);

            Intent intent_city_update = new  Intent(getApplicationContext(), WidgetWeather.class);
            intent_city_update.setAction(WidgetWeather.ACTION_WIDGET_RECEIVER);
            intent_city_update.putExtra(Preferences.ADD_CITY, city);
            intent_city_update.putExtra(Preferences.ADD_TEMP, temperature);
            intent_city_update.putExtra(Preferences.SOURCE, Preferences.WIDGET);
            sendBroadcast(intent_city_update);
            Log.d(Preferences.DEBUG_KEY, "ServiceWeather complete Json render");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERRORO", e.getMessage());
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Toast.makeText(this, "Service bind", Toast.LENGTH_SHORT).show();
        Log.d("DEBUGG", "Service bind");
        return binder;
    }

    public void update() {
        isComplete = false;
        schedule();
    }

    public class WeatherBinder extends Binder {
        public ServiceWeather getService() {
            return ServiceWeather.this;
        }
    }

    protected void onWidgetChangeCity(String city){
        Intent intent_city_update = new  Intent(getApplicationContext(), WidgetWeather.class);
        intent_city_update.putExtra(WidgetWeather.ACTION_WIDGET_RECEIVER, city);
        sendBroadcast(intent_city_update);


        Intent active = new Intent(getApplicationContext(), WidgetWeather.class);
        active.setAction(WidgetWeather.ACTION_WIDGET_RECEIVER);

    }
}
