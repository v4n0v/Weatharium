package net.kdilla.wetharium;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.kdilla.wetharium.fragments.WeatherInfoFragment;
import net.kdilla.wetharium.gson.WeatherGSon;
import net.kdilla.wetharium.gson.WeatherGsonDeserializer;
import net.kdilla.wetharium.gson.WeatherMain;
import net.kdilla.wetharium.gson.WeatherMainDeserializer;
import net.kdilla.wetharium.test.PostManager;
import net.kdilla.wetharium.utils.PreferencesID;
import net.kdilla.wetharium.utils.WeatherDataLoader;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private boolean isWind;
    private boolean isPressure;
    private boolean isSomething;
    private String city = "Moscow";

    private final Handler handler = new Handler();

    private TextView texts;

    FragmentManager myFragmentManager;
    String cityText;
    String temperature;
    String additionalInfo;

    String jsonX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texts = (TextView) findViewById(R.id.tv_texts);
        getWeather(city);

    }


    private synchronized void getWeather(final String city) {
        new Thread() {
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getApplicationContext(), city);

                final PostManager postManager = new PostManager();
                final String jsonStr =jsonObject.toString();
                try {
                    String response = postManager.post("https://jsonplaceholder.typicode.com/posts", jsonStr);
                    Log.d("POST", response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", jsonObject.toString());
                if (jsonObject != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(jsonObject);

                        }
                    });
                }
            }
        }.start();
    }

    public void showInputDialog(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.change_city_dialog));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(city);
        builder.setView(input);
        builder.setPositiveButton("Show me the weather", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getWeather(input.getText().toString());
            }
        });
        builder.show();
    }

    private void renderWeather(JSONObject json) {
        renderWeatherJSON(json);
     texts.setText(json.toString() + "\n" + additionalInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }


    public void showOptions(MenuItem item) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivityForResult(intent, PreferencesID.REQUEST_CODE_WEATHER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // проверяем есть ли результат
        if (resultCode == RESULT_OK) {
            // если код тот же, что мы отправили
            if (requestCode == PreferencesID.REQUEST_CODE_WEATHER) {
                // созраняем возвращенное значение в переменную
                isPressure = data.getBooleanExtra(PreferencesID.ADD_PRESSURE, false);
                isWind = data.getBooleanExtra(PreferencesID.ADD_WIND, false);
                isSomething = data.getBooleanExtra(PreferencesID.ADD_STORM, false);
            }
            getWeather(city);
        }

    }

    private void renderWeatherJSON(JSONObject json) {
        try {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(WeatherGSon.class, new WeatherGsonDeserializer())
                    .registerTypeAdapter(WeatherMain.class, new WeatherMainDeserializer())

                    .create();
            WeatherGSon weatherForGSon = gson.fromJson(json.toString(), WeatherGSon.class);
//
//            cityText = json.getString("name").toUpperCase(Locale.US) + ", "
//                    + json.getJSONObject("sys").getString("country");
//            additionalInfo="";
//            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
//            JSONObject main = json.getJSONObject("main");
//            JSONObject wind = json.getJSONObject("wind");
//            if (isPressure) {
//                additionalInfo += "Pressure: " + main.getString("pressure") + " " + getString(R.string.pressure_dim) + "\n";
//            }
//            if (isWind) {
//                additionalInfo += "Wind: " + wind.getString("speed") + " " + getString(R.string.wind_dim) + "\n";
//            }
//            if (isSomething) {
//                additionalInfo += "Humidity: " + main.getString("humidity") + " " + getString(R.string.humidity_dim) + "\n";
//            }
//
//            temperature = main.getString("temp");
//            Drawable weatherIcon = getWeatherIcon(details.getInt("id"));
            additionalInfo="";
            cityText = weatherForGSon.getCity();
            if (isPressure) {
                additionalInfo += "Pressure: " + weatherForGSon.getPressure()+ " " + getString(R.string.pressure_dim) + "\n";
            }
            if (isWind) {
                additionalInfo += "Wind: " + weatherForGSon.getWind() + " " + getString(R.string.wind_dim) + "\n";
            }
            if (isSomething) {
                additionalInfo += "Humidity: " + weatherForGSon.getHumidity() + " " + getString(R.string.humidity_dim) + "\n";
            }

            temperature = weatherForGSon.getTemperature();
            Drawable weatherIcon = getWeatherIcon(weatherForGSon.getId());
            WeatherInfoFragment weatherInfoFragment = new WeatherInfoFragment();
            weatherInfoFragment.setParams(cityText, temperature, additionalInfo, weatherIcon);


            Log.i("GSON", gson.toJson(weatherForGSon));



            myFragmentManager = getFragmentManager();
            //if (getIntent().getBundleExtra()==null){
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.info_container, weatherInfoFragment, PreferencesID.TAG_1);
            fragmentTransaction.commit();
            //}

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Drawable getWeatherIcon(int actualId){
        int id = actualId / 100;


        switch (id) {
            case 2:
                return getResources().getDrawable(R.drawable.thunder);

            case 3:
                return getResources().getDrawable(R.drawable.drizzle);

            case 5:
                return getResources().getDrawable(R.drawable.rainy);

            case 6:
                return getResources().getDrawable(R.drawable.snowie);

            case 7:
                return getResources().getDrawable(R.drawable.foggy);

            case 8:
                return getResources().getDrawable(R.drawable.cloudly);

            default:
                return getResources().getDrawable(R.drawable.sunny);

        }
    }


}
