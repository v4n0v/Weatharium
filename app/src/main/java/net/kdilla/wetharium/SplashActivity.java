package net.kdilla.wetharium;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    BroadcastReceiver br;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadPreferences();
        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
            //    Toast.makeText(getBaseContext(), "Splash service connected", Toast.LENGTH_SHORT).show();
                Log.d("DEBUGGG", "Splash service connected");
                serviceWeather = ((ServiceWeather.WeatherBinder) service).getService();
                serviceWeather.changeCity(city);
                bind = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bind = false;
            }
        };

        Intent intent = new Intent(getBaseContext(), ServiceWeather.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int temp = intent.getIntExtra(Preferences.ADD_TEMP, 0);
                int hum = intent.getIntExtra(Preferences.ADD_HUMIDITY, 0);
                int wind = intent.getIntExtra(Preferences.ADD_WIND, 0);
                int press = intent.getIntExtra(Preferences.ADD_PRESSURE, 0);
                String decription = intent.getStringExtra(Preferences.ADD_DESCRIPTION);
                String additionalInfo = "";
                int weatherId = intent.getIntExtra(Preferences.ADD_IMAGE_ID, 0);
                if (isPressure) {
                    additionalInfo += "Pressure: " + press + " " + getString(R.string.pressure_dim) + "\n";
                }
                if (isWind) {
                    additionalInfo += "Wind: " + wind + " " + getString(R.string.wind_dim) + "\n";
                }
                if (isHumidity) {
                    additionalInfo += "Humidity: " + hum + " " + getString(R.string.humidity_dim) + "\n";
                }

                Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
                intent1.putExtra(Preferences.ADD_IS_HUMIDITY, isHumidity);
                intent1.putExtra(Preferences.ADD_IS_PRESSURE, isPressure);
                intent1.putExtra(Preferences.ADD_IS_WIND, isWind);
                intent1.putExtra(Preferences.ADD_CITY, city);
                intent1.putExtra(Preferences.ADD_TEMP, temp);
                intent1.putExtra(Preferences.ADD_PRESSURE, press);
                intent1.putExtra(Preferences.ADD_HUMIDITY, hum);
                intent1.putExtra(Preferences.ADD_WIND, wind);
                intent1.putExtra(Preferences.ADD_ADDITION, additionalInfo);
                intent1.putExtra(Preferences.ADD_DESCRIPTION,decription );
                intent1.putExtra(Preferences.ADD_IMAGE_ID, weatherId);
                intent1.putExtra(Preferences.ADD_IS_BIND, bind);
                intent1.putExtra(Preferences.SOURCE, Preferences.SPLASH);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                unregisterReceiver(br);
                unbindService(sConn);
                startActivity(intent1);
                finish();

            }
        };
        IntentFilter intFilt = new IntentFilter(Preferences.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

    }

    private void loadPreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        String savedCity = preferences.getString(Preferences.SAVED_CITY, "Moscow");

        isPressure = preferences.getBoolean(Preferences.SAVED_PRESSURE, true);
        isWind = preferences.getBoolean(Preferences.SAVED_WIND, true);
        isHumidity = preferences.getBoolean(Preferences.SAVED_HUMIDITY, true);
        if (savedCity.equals("") || savedCity == null) savedCity = "Moscow";
        city = savedCity;
        //  serviceWeather.setCity(city);
        // weatherInfoFragment.setCity(city);

    }

    private void showNext() {
        Log.d("DEBUGGG", "Splash show next");
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
