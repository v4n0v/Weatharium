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
    String additionInfo;

    public int getWeatherId() {
        return weatherId;
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

    public void setMainActivity(OnFragmentClickListener mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_info, container, false);
        flickrSearch = new FlickrSearch(cityImageView, getContext());

        // cityImageView = view.findViewById(R.id.header);
        initViews(view);


        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //  flickrSearch.downloadAndSetImage(city);
                int temp = intent.getIntExtra(Preferences.ADD_TEMP, 0);
                int hum = intent.getIntExtra(Preferences.ADD_HUMIDITY, 0);
                int wind = intent.getIntExtra(Preferences.ADD_WIND, 0);
                int press = intent.getIntExtra(Preferences.ADD_PRESSURE, 0);
                String decription = intent.getStringExtra(Preferences.ADD_DESCRIPTION);
                String additionalInfo = "";
                weatherId = intent.getIntExtra(Preferences.ADD_IMAGE_ID, 0);
                if (isPressure) {
                    additionalInfo += "Pressure: " + press + " " + getString(R.string.pressure_dim) + "\n";
                }
                if (isWind) {
                    additionalInfo += "Wind: " + wind + " " + getString(R.string.wind_dim) + "\n";
                }
                if (isHumidity) {
                    additionalInfo += "Humidity: " + hum + " " + getString(R.string.humidity_dim) + "\n";
                }

                cityBitmap = FileManager.loadBitmap(getContext(), city);
                if (cityBitmap == null) {
                    getAindSetToolbarImage();
                } else {
                    cityImageView.setImageBitmap(cityBitmap);
                    Toast.makeText(getContext(), "Loaded from storage", Toast.LENGTH_SHORT).show();
                }
                toolbarLayout.setTitle(city);
//                date = System.currentTimeMillis();
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

                additionalTextView.setText(additionalInfo);
                temperatureTextView.setText(temperatureFormat(temp));
                descriptionTextView.setText(decription);
                weatherImage.setImageDrawable(getWeatherIcon(weatherId));
                // TODO запилить обновление ячейки времени в базу

            }

        };

        IntentFilter intFilt = new IntentFilter(Preferences.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        getActivity().registerReceiver(br, intFilt);




        ///  flickrSearchThread = new FlickrSearchThread(cityImageView);
//        getWeather(city);

        return view;
    }

    private String temperatureFormat(float temperature) {
        if (temperature > 0) return "+" + String.valueOf(Math.round(temperature));
        else return String.valueOf(Math.round(temperature));
    }

    void initViews(View view) {
     //   cityTextView = view.findViewById(R.id.info_city_tv);
        additionalTextView = view.findViewById(R.id.info_additional_info_tv);
        temperatureTextView = view.findViewById(R.id.info_temperature_tv);
        descriptionTextView = view.findViewById(R.id.info_description_tv);
        lastUpdTextView = view.findViewById(R.id.tv_last_update);

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

         //   cityTextView.setText(weatherForGSon.getCity());
            additionalTextView.setText(additionalInfo);
            temperatureTextView.setText(temperatureFormat(weatherForGSon.getTemperature()));
            descriptionTextView.setText(weatherForGSon.getMainInfo());

            weatherImage.setImageDrawable(getWeatherIcon(weatherForGSon.getId()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void getWeather(final String city) {
//        this.city = city;
//        // cityBitmap = loadBitmap();
//
//        Thread jsonWeatherThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getActivity().getApplicationContext(), city);
//
//                if (jsonObject != null) {
//                    Log.d("TAG", jsonObject.toString());
//                    handler.post(new Runnable() {
//                        public void run() {
//                            renderWeather(jsonObject);
//
//                        }
//                    });
//                }
//            }
//        });
//
//        jsonWeatherThread.start();
//        cityBitmap = FileManager.loadBitmap(getContext(), city);
//        if (cityBitmap == null) {
//            getAindSetToolbarImage();
//        } else {
//            cityImageView.setImageBitmap(cityBitmap);
//            Toast.makeText(getContext(), "Loaded from storage", Toast.LENGTH_SHORT).show();
//        }
//
////                public void run() {
////                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getActivity().getApplicationContext(), city);
////
////                if (jsonObject != null) {
////                    Log.d("TAG", jsonObject.toString());
////                    handler.post(new Runnable() {
////                        public void run() {
////                            renderWeather(jsonObject);
////
////                        }
////                    });
////                }
////            }
////        }.start();
//
//    }

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


    private Bitmap loadBitmap() {
        File file = new File(getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), city.toLowerCase() + ".jpg");
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    private void saveBitmap(Bitmap bitmap) {

        try {

            File file = new File(getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), city.toLowerCase() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, file.getName(), file.getName()); // регистрация в фотоальбоме
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Saved in storage as " + city.toLowerCase() + ".jpg", Toast.LENGTH_SHORT).show();
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

    public void setIsWind(int wind) {
        this.wind = wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setIsHumidity(int humidity) {
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