package net.kdilla.weatharium;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnRun;
    private Spinner spinnerSelectCountry;
    private SharedPreferences preferences;
    private TextView textViewWeather;
    private final String SAVED_COUNTRY_WEATHER = "saved countery";
    private String currentWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        loadPreferencesAndShowWeather();

    }

    private void initViews() {
        spinnerSelectCountry = (Spinner) findViewById(R.id.spinner_sel_country);
        btnRun = (Button) findViewById(R.id.btn_run);
        btnRun.setOnClickListener(onClickListener);
        textViewWeather = (TextView) findViewById(R.id.textview_weather);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_run) {
                savePreferences();
                showWeather(currentWeather);
            }
        }
    };

    void showWeather(String weather){
        textViewWeather.setText(weather);
    }
    private void savePreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        currentWeather = WeatherByCountry.getWeatherInCountry(MainActivity.this, spinnerSelectCountry.getSelectedItemPosition());
        ed.putString(SAVED_COUNTRY_WEATHER, currentWeather);
        ed.commit();

    }

    private void loadPreferencesAndShowWeather() {
        preferences = getPreferences(MODE_PRIVATE);
        String savedPrefs = preferences.getString(SAVED_COUNTRY_WEATHER, "");

        // поулчаю имя страны и пытаюсь установливаю его, как элемент по умолчанию в спиннере
        String[] savedPrefsSplit = savedPrefs.split(" ");
        String savedCountry = savedPrefsSplit[0];
        String[] countries=this.getResources().getStringArray(R.array.country_selection);
        for (int i = 0; i < countries.length; i++) {
            if (countries[i].equals(savedCountry)){
                spinnerSelectCountry.setSelection(i);
                break;
            }
        }
        textViewWeather.setText(savedPrefs);
        Toast.makeText(MainActivity.this, (R.string.country_loaded), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePreferences();
    }
}

