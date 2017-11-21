package net.kdilla.wetharium;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import net.kdilla.wetharium.fragments.AdditionalInfoFragment;
import net.kdilla.wetharium.fragments.CitySelectListener;
import net.kdilla.wetharium.fragments.WeatherDetailFragment;
import net.kdilla.wetharium.utils.PreferencesID;

public class MainActivity extends AppCompatActivity implements CitySelectListener{
    private boolean isWind;
    private boolean isPressure;
    private boolean isSomething;
    private CheckBox chbPressure;
    private CheckBox chbWind;
    private CheckBox chbStorm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
//        WeatherDetailFragment detailFragment = new WeatherDetailFragment();
//        detailFragment.setCityId(1);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.fragment_container, detailFragment);
//        transaction.commit();
    }

    private void initViews() {
        chbPressure = (CheckBox) findViewById(R.id.chb_pressure);
        chbWind = (CheckBox) findViewById(R.id.chb_wind);
        chbStorm = (CheckBox) findViewById(R.id.chb_something);
    }

    @Override
    public void onListItemClick(int id) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        isWind = chbWind.isChecked();
        isPressure = chbPressure.isChecked();
        isSomething = chbStorm.isChecked();

        if (fragmentContainer != null) {
          // WeatherDetailFragment detailFragment =  WeatherDetailFragment.init(getIntent().getBundleExtra("key"));
            WeatherDetailFragment detailFragment = new WeatherDetailFragment();

            detailFragment.setCityId(id);
            detailFragment.setPressure(isPressure);
            detailFragment.setShowSomething(isSomething);
            detailFragment.setShowWind(isWind);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, detailFragment);
//            transaction.replace(R.id.fragment_container, detailFragment);

            transaction.commit();
        } else {
            Intent intent = new Intent(this, ShowWeatherFromList.class);
            intent.putExtra(PreferencesID.EXTRA_CITY_NOM, id);
            intent.putExtra(PreferencesID.ADD_PRESSURE, isPressure);
            intent.putExtra(PreferencesID.ADD_WIND, isWind);
            intent.putExtra(PreferencesID.ADD_STORM, isSomething);

            startActivity(intent);

        }

    }
}
