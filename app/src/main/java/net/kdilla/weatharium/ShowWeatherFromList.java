package net.kdilla.weatharium;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.kdilla.weatharium.utils.GetWeatherFromRes;
import net.kdilla.weatharium.utils.PreferencesID;

public class ShowWeatherFromList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather_from_list);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
          //  LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            TextView city = (TextView) findViewById(R.id.city);
            TextView temperature = (TextView) findViewById(R.id.texview_weather);

            int cityNom = bundle.getInt(PreferencesID.EXTRA_CITY_NOM);
            CityWeather weather = new CityWeather(GetWeatherFromRes.getCity(this, cityNom),
                    GetWeatherFromRes.getWeather(this, cityNom));//CityWeather.cityWeatherList[cityNom];
            Log.d("ShowWeatherFromList", "cityNom " + weather);
            Log.d("ShowWeatherFromList", "city " + weather.getCity());
            Log.d("ShowWeatherFromList", "weather " + weather.getWeather());

            // получаю LinearLayout и создаю в нем новые TextView
            LinearLayout container = (LinearLayout) findViewById(R.id.addition_container);
            LinearLayout.LayoutParams params = new   LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            // вот тут косячек, не получается вытянуть из ресурсов margin, там они float,
            // отсуп 10, это не 10dp это что-то не понятное, видимо пиксели
            params.setMargins(10, 5, 5, 5);
            if (bundle.getBoolean(PreferencesID.ADD_PRESSURE)){

                weather.setPressure( GetWeatherFromRes.getPressure(this, cityNom));
                TextView tv_pressure = new TextView(this);
                tv_pressure.setText(weather.getPressure());
                container.addView(tv_pressure, params);

               // TextView pressure = (TextView) findViewById(R.id.texview_pressure);
               // pressure.setText(weather.getPressure());

                Log.d("ShowWeatherFromList", "pressure " + weather.getPressure());

            }
            if (bundle.getBoolean(PreferencesID.ADD_WIND)){
                weather.setWind(GetWeatherFromRes.getWind(this, cityNom));

                TextView tv_wind = new TextView(this);
                tv_wind.setText(weather.getWind());
                container.addView(tv_wind, params);
//                TextView wind = (TextView) findViewById(R.id.texview_wind);
//                wind.setText(weather.getWind());

                Log.d("ShowWeatherFromList", "wind " + weather.getWind());

            }
            if (bundle.getBoolean(PreferencesID.ADD_STORM)){
                weather.setStorm(GetWeatherFromRes.getStorm(this, cityNom));
                TextView tv_smthing = new TextView(this);
                // tv.setBackgroundResource(R.color.textviewColor);
                tv_smthing.setText(weather.getStorm());
                container.addView(tv_smthing, params);

                //TextView wind = (TextView) findViewById(R.id.texview_storm);
                //wind.setText(weather.getStorm());
                Log.d("ShowWeatherFromList", "storm " + weather.getStorm());
            }

            // Заполнение наименования услуги ногтевого сервиса

            city.setText(weather.getCity());

            // Заполнение описания услуги ногтевого сервиса

            temperature.setText(weather.getWeather());
        }
    }

}
