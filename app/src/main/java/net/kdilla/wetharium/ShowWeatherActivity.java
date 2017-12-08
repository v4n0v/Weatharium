//package net.kdilla.wetharium;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import net.kdilla.wetharium.utils.CityWeather;
//import net.kdilla.wetharium.utils.GetWeatherFromRes;
//import net.kdilla.wetharium.utils.PreferencesID;
//import net.kdilla.wetharium.utils.WeatherIcon;
//
///**
// * Created by avetc on 15.11.2017.
// */
//
//public class ShowWeatherActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_weather_from_list);
//
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            //  LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//
//            TextView city = (TextView) findViewById(R.id.city);
//            TextView temperature = (TextView) findViewById(R.id.texview_weather);
//
//            int cityNom = bundle.getInt(PreferencesID.EXTRA_CITY_NOM);
//            CityWeather weather = new CityWeather(GetWeatherFromRes.getCity(this, cityNom),
//                    GetWeatherFromRes.getWeather(this, cityNom));//CityWeather.cityWeatherList[cityNom];
//            Log.d("ShowWeatherActivity", "cityNom " + weather);
//            Log.d("ShowWeatherActivity", "city " + weather.getCity());
//            Log.d("ShowWeatherActivity", "weather " + weather.getTemperature());
//
//            // получаю LinearLayout и создаю в нем новые TextView
//            LinearLayout container = (LinearLayout) findViewById(R.id.addition_info_container);
//            LinearLayout.LayoutParams params = new   LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//
//            // вот тут косячек, не получается вытянуть из ресурсов margin, там они float,
//            // отсуп 10, это не 10dp это что-то не понятное, видимо пиксели
//            params.setMargins(10, 5, 5, 5);
//            if (bundle.getBoolean(PreferencesID.ADD_PRESSURE)){
//
//                weather.setPressure( GetWeatherFromRes.getPressure(this, cityNom));
//                TextView tv_pressure = new TextView(this);
//                tv_pressure.setText(weather.getPressure());
//                container.addView(tv_pressure, params);
//
//                // TextView pressure = (TextView) findViewById(R.id.texview_pressure);
//                // pressure.setText(weather.getPressure());
//
//                Log.d("ShowWeatherActivity", "pressure " + weather.getPressure());
//
//            }
//            if (bundle.getBoolean(PreferencesID.ADD_WIND)){
//                weather.setWind(GetWeatherFromRes.getWind(this, cityNom));
//
//                TextView tv_wind = new TextView(this);
//                tv_wind.setText(weather.getWind());
//                container.addView(tv_wind, params);
//
//
//                Log.d("ShowWeatherActivity", "wind " + weather.getWind());
//
//            }
//            if (bundle.getBoolean(PreferencesID.ADD_STORM)){
//                weather.setSomething(GetWeatherFromRes.getSomething(this, cityNom));
//                TextView tvSomething = new TextView(this);
//                // tv.setBackgroundResource(R.color.textviewColor);
//                tvSomething.setText(weather.getSomething());
//                container.addView(tvSomething, params);
//
//                Log.d("ShowWeatherActivity", "storm " + weather.getSomething());
//            }
//
//
//
//            city.setText(weather.getCity());
//            ImageView imageView = (ImageView) findViewById(R.id.image_weather_ico);
//            imageView.setImageResource(WeatherIcon.sunny);
//
//
//            temperature.setText(weather.getTemperature());
//        }
//    }
//
//}
