package net.kdilla.wetharium.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.kdilla.wetharium.utils.CityWeather;
import net.kdilla.wetharium.R;
import net.kdilla.wetharium.utils.GetWeatherFromRes;


public class WeatherDetailFragment extends Fragment {
    private int cityId;
    private boolean showPressure;
    private boolean showWind;
    private boolean showSomething;


    public WeatherDetailFragment() {

    }

    public static WeatherDetailFragment init(Bundle bundle) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    //Тэг, которым мы почечаем фрагмент и по которому мы его находим после пересоздания активити
    //https://www.uuidgenerator.net/ - генератор уникальных чисел
    private final static String ADD_INFO_FRAGMENT_TAG = "9ef69c6a-5178-484e-9791-463c2d77c55c";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_detail, container, false);
        TextView title = (TextView) view.findViewById(R.id.textTitle);
//        getArguments();


        LinearLayout additionalContainer = (LinearLayout) view.findViewById(R.id.addition_info_container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        CityWeather weather = new CityWeather(GetWeatherFromRes.getCity(getActivity(), cityId),
                GetWeatherFromRes.getWeather(getActivity(), cityId));
        title.setText(weather.getCity());
        TextView description = (TextView) view.findViewById(R.id.textDescription);
        description.setText(weather.getTemperature());

        //Создаем Вложенный Фрагмент
        FragmentManager fragmentManager = getChildFragmentManager();//Внимание! ChildFragmentManager!
        //Ищем Фрагмент по тегу и если его нет, то создаем Фрагмент с таким тегом


       AdditionalInfoFragment additionalInfoFragment = (AdditionalInfoFragment) fragmentManager.findFragmentByTag(ADD_INFO_FRAGMENT_TAG);
         if (additionalInfoFragment == null) {
             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//             additionalInfoFragment = AdditionalInfoFragment.init(getActivity().getIntent().getBundleExtra("key"));
             additionalInfoFragment = new AdditionalInfoFragment();

             additionalInfoFragment.setPressure(showPressure);
             additionalInfoFragment.setShowWind(showWind);
             additionalInfoFragment.setShowSomething(showSomething);
             additionalInfoFragment.setCityId(cityId);
             additionalInfoFragment.setWeather(weather);


            fragmentTransaction.replace(R.id.addition_info_container, additionalInfoFragment, ADD_INFO_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }

        return view;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setPressure(boolean showPressure) {
        this.showPressure = showPressure;
    }

    public void setShowSomething(boolean showSomething) {
        this.showSomething = showSomething;
    }

    public void setShowWind(boolean showWind) {
        this.showWind = showWind;
    }
}
