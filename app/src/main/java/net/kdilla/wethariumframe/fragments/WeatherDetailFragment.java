package net.kdilla.wethariumframe.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.kdilla.wethariumframe.utils.CityWeather;
import net.kdilla.wethariumframe.R;
import net.kdilla.wethariumframe.utils.GetWeatherFromRes;
import net.kdilla.wethariumframe.utils.WeatherIcon;


public class WeatherDetailFragment extends Fragment {
    private int cityId;
    private boolean showPressure;
    private boolean showWind;
    private boolean showSomething;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_detail, container, false);
        TextView title = (TextView) view.findViewById(R.id.textTitle);

        LinearLayout additionalContainer = (LinearLayout) view.findViewById(R.id.addition_container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        CityWeather weather = new CityWeather(GetWeatherFromRes.getCity(getActivity(), cityId),
                GetWeatherFromRes.getWeather(getActivity(), cityId));
        title.setText(weather.getCity());
        TextView description = (TextView) view.findViewById(R.id.textDescription);
        description.setText(weather.getTemperature());

        if (showPressure) {

            weather.setPressure(GetWeatherFromRes.getPressure(getActivity(), cityId));
            TextView tv_pressure = new TextView(getActivity());
            tv_pressure.setText(weather.getPressure());

            additionalContainer.addView(tv_pressure, params);
            Log.d("ShowWeatherFromList", "pressure " + weather.getPressure());

        }
        if (showWind){
            weather.setWind(GetWeatherFromRes.getWind(getActivity(), cityId));

            TextView tv_wind = new TextView(getActivity());
            tv_wind.setText(weather.getWind());
            additionalContainer.addView(tv_wind, params);


            Log.d("ShowWeatherFromList", "wind " + weather.getWind());

        }
        if (showSomething){
            weather.setSomething(GetWeatherFromRes.getSomething(getActivity(), cityId));
            TextView tv_smthing = new TextView(getActivity());

            tv_smthing.setText(weather.getSomething());
            additionalContainer.addView(tv_smthing, params);

            Log.d("ShowWeatherFromList", "storm " + weather.getSomething());
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.image_weather_details);
        imageView.setImageResource(WeatherIcon.sunny);
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
