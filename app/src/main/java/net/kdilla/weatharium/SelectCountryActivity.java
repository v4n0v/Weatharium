package net.kdilla.weatharium;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import net.kdilla.weatharium.utils.PreferencesID;


public class SelectCountryActivity extends AppCompatActivity {

    private static final String TAG = "****************";
    //    private static final String TAG = SelectCountryActivity.class.getSimpleName();
    private static final String BUNDLE_EXTRAS_CITY = "bundle_seconds";
    private static final String BUNDLE_EXTRAS_WEATHER = "bundle_weather";

    private static final String KEY_CITY = "city";
    private static final String KEY_WEATHER = "weather";

    private Button btnRun;
    private Spinner spinnerSelectCountry;
    private SharedPreferences preferences;

    CheckBox chbPressure;
    CheckBox chbWind;
    CheckBox chbStorm;

    private int cityPosition;
    private String currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView weather = (TextView) findViewById(R.id.textview_weather);


        Log.d(TAG, "onCreate");
        //
        initViews();
        if (savedInstanceState != null) {
            //   Log.i(TAG, "savedInstanceState");
            currentWeather = savedInstanceState.getString(KEY_WEATHER);
            cityPosition = savedInstanceState.getInt(KEY_CITY);
            spinnerSelectCountry.setSelection(cityPosition);

        }
        loadPreferencesAndShowWeather();


    }

    private void initViews() {
        spinnerSelectCountry = (Spinner) findViewById(R.id.spinner_sel_country);
        btnRun = (Button) findViewById(R.id.btn_run);
        btnRun.setOnClickListener(onClickListener);

        chbPressure = (CheckBox) findViewById(R.id.chb_pressure);
        chbWind = (CheckBox) findViewById(R.id.chb_wind);
        chbStorm = (CheckBox) findViewById(R.id.chb_storm);
    }

    boolean isWind;
    boolean isPressure;
    boolean isStorm;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_run) {

                // получаю значение погоды для текущего элемента спиннера
                currentWeather = WeatherByCountry.getWeatherInCountry(SelectCountryActivity.this,
                        spinnerSelectCountry.getSelectedItemPosition());
                Intent intent = new Intent(SelectCountryActivity.this, ShowWeatherActivity.class);

                isWind = chbWind.isChecked();
                isPressure = chbPressure.isChecked();
                isStorm = chbStorm.isChecked();

                // передаю значение погоды на сл активити
                intent.putExtra(PreferencesID.WEATHER_POS, currentWeather);
                intent.putExtra(PreferencesID.ADD_PRESSURE, isPressure);
                intent.putExtra(PreferencesID.ADD_WIND, isWind);
                intent.putExtra(PreferencesID.ADD_STORM, isStorm);
                // вызываю метод для сохраниения значения при возврате на это активити
                startActivity(intent);
                ///    startActivityForResult(intent, PreferencesID.REQUEST_CODE_WEATHER);
            }
        }
    };


    private void savePreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        cityPosition = spinnerSelectCountry.getSelectedItemPosition();
        // созраняю значение текущего элемента спиннера и чекбоксов
        ed.putInt(PreferencesID.SAVED_CITY, cityPosition).apply();
        ed.putBoolean(PreferencesID.SAVED_ADD_PRESSURE, isPressure).apply();
        ed.putBoolean(PreferencesID.SAVED_ADD_WIND, isWind).apply();
        ed.putBoolean(PreferencesID.SAVED_ADD_STORM, isStorm).apply();

        ed.commit();

        Log.d(TAG, "save preferences");
    }

    //
    private void loadPreferencesAndShowWeather() {
        preferences = getPreferences(MODE_PRIVATE);
        // восстанавливаю из настроек последнее состояние спиннера и чекбксов
        spinnerSelectCountry.setSelection(preferences.getInt(PreferencesID.SAVED_CITY, 1));
        chbPressure.setChecked(preferences.getBoolean(PreferencesID.SAVED_ADD_PRESSURE, false));
        chbStorm.setChecked(preferences.getBoolean(PreferencesID.SAVED_ADD_STORM, false));
        chbWind.setChecked(preferences.getBoolean(PreferencesID.SAVED_ADD_WIND, false));
        // Toast.makeText(SelectCountryActivity.this, (R.string.city_loaded), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "load preferences");

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        System.out.println("onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        System.out.println("onPause");
        super.onPause();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        System.out.println("onStart");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        System.out.println("onDestroy");
        super.onDestroy();
        savePreferences();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // проверяем есть ли результат
//        if (resultCode == RESULT_OK) {
//            // если код тот же, что мы отправили
//            if (requestCode == PreferencesID.REQUEST_CODE_WEATHER) {
//               // созраняем возвращенное значение в переменную
//                String returnString = data.getStringExtra(PreferencesID.SAVED_WEATHER);
//                // инициализируем текстовое поле и выводим на экран
//                TextView infoTextView = (TextView) findViewById(R.id.textview_weather);
//                infoTextView.setVisibility(View.VISIBLE);
//                infoTextView.setText(returnString);
//            }
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_CITY, spinnerSelectCountry.getSelectedItemPosition());
        savedInstanceState.putString(KEY_WEATHER, WeatherByCountry.getWeatherInCountry(SelectCountryActivity.this, spinnerSelectCountry.getSelectedItemPosition()));
//        savedInstanceState.putBoolean(KEY_WAS_RUNNING, wasRunning);
    }


}

