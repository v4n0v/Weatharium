package net.kdilla.wetharium.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import net.kdilla.wetharium.R;
import net.kdilla.wetharium.utils.Preferences;
import net.kdilla.wetharium.utils.WeatherDataLoader;
import net.kdilla.wetharium.utils.gson.Weather;
import net.kdilla.wetharium.utils.gson.WeatherDeserializer;
import net.kdilla.wetharium.utils.gson.WeatherMain;
import net.kdilla.wetharium.utils.gson.WeatherMainDeserializer;
import net.kdilla.wetharium.utils.tasks.WeatherGetTask;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ServiceWeather extends Service {

    Timer timer;
    TimerTask tTask;

    String city;
    int temperature;
    int pressure;
    int wind;
    int humidity;
    String lat;
    String lon;
    String description;
    // обновляю каждые 10 минут
    private long interval = 600_000;

    WeatherBinder binder = new WeatherBinder();

    private boolean isWind = true;
    private boolean isPressure = true;
    private boolean isHumidity = true;
    private boolean isComplete=true;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        Log.d("ServiceWeather", "Service created");
     schedule();

    }
    public void setCity(String city){
        this.city=city;
    }
    public void changeCity(String city){
      setCity(city);
      isComplete=false;
      schedule();

    }
    private void schedule() {
        if (tTask != null) tTask.cancel();
        if (interval > 0) {
            // начинаем отсчет до 10минут
            tTask = new TimerTask() {
                public void run() {
                    Log.d("ServiceWeather", " " + interval);
                    if (city!=null) {
                        if (isComplete == false) {
                            loagWeatherJson();
                        }
                    }
                }
            };
            timer.schedule(tTask, 1000, interval);
        }
    }

    private void loagWeatherJson(){
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
            Log.d("ServiceWeather", "jsonObject=null ");
        }
        isComplete = true;
        Log.d("ServiceWeather", "jsonObject load complete");
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
            int imageId= weatherForGSon.getId();
           description=weatherForGSon.getMainInfo();

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
//
//            temperature = weatherForGSon.getTemperature();
//            description=weatherForGSon.getMainInfo();
//            Drawable weatherIcon = getWeatherIcon(weatherForGSon.getId());

            Log.d("GSON", gson.toJson(weatherForGSon));

//            cityTextView.setText(weatherForGSon.getCity());
//            additionalTextView.setText(additionalInfo);
//            temperatureTextView.setText(temperatureFormat(weatherForGSon.getTemperature()));
//            descriptionTextView.setText(weatherForGSon.getMainInfo());
//            weatherImage.setImageDrawable(getWeatherIcon(weatherForGSon.getId()));

            Intent intent = new Intent(Preferences.BROADCAST_ACTION);

            intent.putExtra(Preferences.ADD_TEMP, temperature);
            intent.putExtra(Preferences.ADD_HUMIDITY, humidity);
            intent.putExtra(Preferences.ADD_WIND, wind);
            intent.putExtra(Preferences.ADD_PRESSURE, pressure);
            intent.putExtra(Preferences.ADD_DESCRIPTION, description);
            intent.putExtra(Preferences.ADD_IMAGE_ID, imageId);

            sendBroadcast(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Service bind", Toast.LENGTH_SHORT).show();
        return binder;
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



    public class WeatherBinder extends Binder{
       public ServiceWeather getService(){ return ServiceWeather.this;}
    }


}
