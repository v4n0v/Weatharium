package net.kdilla.weatharium;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.kdilla.weatharium.utils.PreferencesID;

public class ShowWeatherActivity extends AppCompatActivity {


    Button btnShare;
    TextView tvShowWeather;
    private String currentWeather;

    //  private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        initLayouts();

        // получаем значения из предыдущего активити
        Intent intent = getIntent();
        if (intent != null) {
            currentWeather = intent.getStringExtra(PreferencesID.WEATHER_POS);
            if (currentWeather != null)
                tvShowWeather.setText(currentWeather);
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
                shareWeather(currentWeather);
            }
        }
    };

    void shareWeather(String sendMsg) {
        // отправляем значение погоды в приложение, которое может его отправить контактам
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sendMsg);
        Intent chooseIntent = Intent.createChooser(intent, getString(R.string.select_app));

        startActivity(chooseIntent);
    }

    // при нажатии "назад" отправляем данные в 1ую активити
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        // создаем Intent
        Intent returnIntent = new Intent();
        // заполняем переменной currentWeather с ключем SAVED_COUNTRY_WEATHER
        returnIntent.putExtra(PreferencesID.SAVED_COUNTRY_WEATHER, currentWeather);
        // сохраняем значения для отправки
        setResult(RESULT_OK, returnIntent);
        // завершаем работу активити
        finish();

    }

}
