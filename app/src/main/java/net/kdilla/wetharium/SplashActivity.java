package net.kdilla.wetharium;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.kdilla.wetharium.services.ServiceWeather;
import net.kdilla.wetharium.utils.Preferences;

public class SplashActivity extends AppCompatActivity {
    ServiceConnection sConn;
    boolean bind;
    ServiceWeather serviceWeather;

    SharedPreferences preferences;
    boolean isPressure;
    boolean isHumidity;
    boolean isWind;
    String city;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Log.d("Splash ", "Sleep interval end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showNext();
            }
        };
        timer.start();




//              loadPreferences();
//        sConn = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                Toast.makeText(getBaseContext(), "Service connected", Toast.LENGTH_SHORT).show();
//                Log.d("ServiceWeather", "Service connected");
//                serviceWeather = ((ServiceWeather.WeatherBinder) service).getService();
//                serviceWeather.changeCity(city);
//                bind = true;
//
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                intent.putExtra(Preferences.ADD_IS_HUMIDITY, isHumidity);
//                intent.putExtra(Preferences.ADD_IS_PRESSURE, isPressure);
//                intent.putExtra(Preferences.ADD_IS_WIND, isWind);
//                intent.putExtra(Preferences.ADD_CITY, city);
//                intent.putExtra(Preferences.ADD_IS_BIND, bind);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                bind = false;
//            }
//        };
//
//        Intent intent = new Intent(getBaseContext(), ServiceWeather.class);
//        bindService(intent, sConn, BIND_AUTO_CREATE);
//
//    }
//
//    private void loadPreferences() {
//        preferences = getPreferences(MODE_PRIVATE);
//        String savedCity = preferences.getString(Preferences.SAVED_CITY, "Moscow");
//
//        isPressure = preferences.getBoolean(Preferences.SAVED_PRESSURE, true);
//        isWind = preferences.getBoolean(Preferences.SAVED_WIND, true);
//        isHumidity = preferences.getBoolean(Preferences.SAVED_HUMIDITY, true);
//        if (savedCity.equals("") || savedCity == null) savedCity = "Moscow";
//        city = savedCity;
//        //  serviceWeather.setCity(city);
//       // weatherInfoFragment.setCity(city);
//    }
    }

    private void showNext(){
        Log.d("SplashActivity","Show next");
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
