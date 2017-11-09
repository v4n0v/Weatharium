package net.kdilla.weatharium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.kdilla.weatharium.utils.PreferencesID;

public class ShowWeatherActivity extends AppCompatActivity {

    private static final String KEY_WEATHER = "weather";

    Button btnShare;
    TextView tvShowWeather;


    private String currentWeather;
    private int cityPosition;
    //  private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        initLayouts();

        TextView tvShowPressure = (TextView) findViewById(R.id.tv_show_pressure);
        TextView tvShowWind = (TextView) findViewById(R.id.tv_show_wind);
        TextView tvShowStorm = (TextView) findViewById(R.id.tv_show_storm);
        if (savedInstanceState != null) {

            currentWeather = savedInstanceState.getString(KEY_WEATHER);

        }

        // получаем значения из предыдущего активити
        Intent intent = getIntent();
        if (intent != null) {
            currentWeather = intent.getStringExtra(PreferencesID.WEATHER_POS);

            if (currentWeather != null)
                tvShowWeather.setText(currentWeather);
            if (intent.getBooleanExtra(PreferencesID.ADD_PRESSURE, false)){
                tvShowPressure.setText("pressure");

            }
            if (intent.getBooleanExtra(PreferencesID.ADD_WIND, false)){
                tvShowWind.setText("wind");
            }
            if (intent.getBooleanExtra(PreferencesID.ADD_STORM, false)){
                tvShowStorm.setText("storm");
            }
        }
    }

    private void initLayouts() {
        btnShare = (Button) findViewById(R.id.btn_share);
        tvShowWeather = (TextView) findViewById(R.id.textview_weather_show);
        btnShare.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_share) {
              //  shareWeather(currentWeather);
            }
        }
    };

//    void shareWeather(String sendMsg) {
//        // отправляем значение погоды в приложение, которое может его отправить контактам
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_TEXT, sendMsg);
//        Intent chooseIntent = Intent.createChooser(intent, getString(R.string.select_app));
//
//        PackageManager packageManager = getPackageManager();
//        //if (intent.resolveActivity(packageManager)!=null)
//        if(!packageManager.queryIntentActivities(intent, 0).isEmpty())
//            startActivity(chooseIntent);
//    }

    // при нажатии "назад" отправляем данные в 1ую активити
    @Override
    public void onBackPressed() {

//
//        // создаем Intent
//        Intent returnIntent = new Intent();
//        // заполняем переменной currentWeather с ключем SAVED_WEATHER
//        returnIntent.putExtra(PreferencesID.SAVED_WEATHER, currentWeather);
//        // сохраняем значения для отправки
//        setResult(RESULT_OK, returnIntent);
//        // завершаем работу активити
//        finish();
        super.onBackPressed();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //  Log.i(TAG, "onSaveInstanceState");
     //   savedInstanceState.putInt(KEY, spinnerSelectCountry.getSelectedItemPosition());
          savedInstanceState.putString(KEY_WEATHER, currentWeather);
//        savedInstanceState.putBoolean(KEY_WAS_RUNNING, wasRunning);
    }
}
