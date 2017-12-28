//package net.kdilla.wetharium;
//
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.ServiceConnection;
//import android.os.IBinder;
//import android.util.Log;
//
//import net.kdilla.wetharium.fragments.WeatherInfoFragment;
//import net.kdilla.wetharium.services.ServiceWeather;
//import net.kdilla.wetharium.utils.Preferences;
//
///**
// * Created by avetc on 26.12.2017.
// */
//
//public class ApplicationController {
//    private boolean bind;
//    private Context context;
//    private ServiceConnection sConn;
//    private ServiceWeather serviceWeather;
//    private boolean isPressure;
//    private boolean isHumidity;
//    private boolean isWind;
//    private String city;
//    private BroadcastReceiver br;
//    long date;
//
//    private WeatherInfoFragment weatherInfoFragment;
//
//    public ApplicationController(Context context) {
//        this.context = context;
//
//        onCreate();
//    }
//    public void loadCity(String city){
//        serviceWeather.changeCity(city);
//    }
//    private void onCreate() {
//        weatherInfoFragment=new WeatherInfoFragment();
//        sConn = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                //    Toast.makeText(getBaseContext(), "Splash service connected", Toast.LENGTH_SHORT).show();
//                Log.d("DEBUGGG", "Splash service connected");
//                serviceWeather = ((ServiceWeather.WeatherBinder) service).getService();
//                serviceWeather.changeCity(city);
//                bind = true;
//            }
//
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                bind = false;
//            }
//        };
//
//        loadCity(city);
//
//        Intent intent = new Intent(context, ServiceWeather.class);
//        context.bindService(intent, sConn, Context.BIND_AUTO_CREATE);
//
//        br = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                city = intent.getStringExtra(Preferences.ADD_CITY);
//                int temp = intent.getIntExtra(Preferences.ADD_TEMP, 0);
//                int hum = intent.getIntExtra(Preferences.ADD_HUMIDITY, 0);
//                int wind = intent.getIntExtra(Preferences.ADD_WIND, 0);
//                int press = intent.getIntExtra(Preferences.ADD_PRESSURE, 0);
//                String description = intent.getStringExtra(Preferences.ADD_DESCRIPTION);
//                int weatherId = intent.getIntExtra(Preferences.ADD_IMAGE_ID, 0);
//
//                String additionalInfo = "";
//                if (isPressure) {
//                    additionalInfo += "Pressure: " + press + " " + context.getString(R.string.pressure_dim) + "\n";
//                }
//                if (isWind) {
//                    additionalInfo += "Wind: " + wind + " " + context.getString(R.string.wind_dim) + "\n";
//                }
//                if (isHumidity) {
//                    additionalInfo += "Humidity: " + hum + " " + context.getString(R.string.humidity_dim) + "\n";
//                }
////                weatherInfoFragment.setCity(city);
////                weatherInfoFragment.setTemperature(temp);
////                weatherInfoFragment.setPressure(press);
////                weatherInfoFragment.setWind(wind);
////                weatherInfoFragment.setHumidity(hum);
////                weatherInfoFragment.setDescription(description);
////                weatherInfoFragment.setWeatherId(weatherId);
////                weatherInfoFragment.setAdditionalParams(additionalInfo);
//
//                weatherInfoFragment.configure(city, temp, press, wind, hum, description, weatherId, additionalInfo);
//
//                weatherInfoFragment.setOptions(isHumidity,isWind, isPressure);
//                date = System.currentTimeMillis();
//             //   weatherInfoFragment.refreshMe();
//            }
//        };
//
//        IntentFilter intFilt = new IntentFilter(Preferences.BROADCAST_ACTION);
//        // регистрируем (включаем) BroadcastReceiver
//        context.registerReceiver(br, intFilt);
//
//
//    }
//
//}
