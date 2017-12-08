//package net.kdilla.wetharium.fragments;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import net.kdilla.wetharium.R;
//import net.kdilla.wetharium.utils.CityWeather;
//
//
///**
// * Created by avetc on 20.11.2017.
// */
//
//public class AdditionalInfoFragment extends Fragment {
//
//    private int cityId;
//    private boolean showPressure;
//    private boolean showWind;
//    private boolean showSomething;
//
//
//
//    private CityWeather weather;
//
//    public void setWeather(CityWeather weather) {
//        this.weather = weather;
//    }
//
//    public AdditionalInfoFragment() {
//
//    }
//
//    public static AdditionalInfoFragment init(Bundle bundle) {
//        AdditionalInfoFragment fragment = new AdditionalInfoFragment();
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d("StopwatchFragment", "savedInstanceState " + savedInstanceState);
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//       View rootView = inflater.inflate(R.layout.fragment_add_info, container, false);
//       getArguments();
////        if (savedInstanceState != null) {
//            LinearLayout containerAddInfo = (LinearLayout) rootView.findViewById(R.id.linear_add_info);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//    //        int cityId = savedInstanceState.getInt(PreferencesID.EXTRA_CITY_NOM);
//
//
//            params.setMargins(10, 5, 5, 5);
////            if (savedInstanceState.getBoolean(PreferencesID.ADD_PRESSURE)) {
//            if (showPressure) {
//
//                weather.setPressure(GetWeatherFromRes.getPressure(getActivity(), cityId));
//                TextView tv_pressure = new TextView(getActivity());
//                tv_pressure.setText(weather.getPressure());
//                containerAddInfo.addView(tv_pressure, params);
//
//                // TextView pressure = (TextView) findViewById(R.id.texview_pressure);
//                // pressure.setText(weather.getPressure());
//
//                Log.d("ShowWeatherActivity", "pressure " + weather.getPressure());
//
//            }
////            if (savedInstanceState.getBoolean(PreferencesID.ADD_WIND)) {
//            if (showWind) {
//                weather.setWind(GetWeatherFromRes.getTemp(getActivity(), cityId));
//
//                TextView tv_wind = new TextView(getActivity());
//                tv_wind.setText(weather.getTemp());
//                containerAddInfo.addView(tv_wind, params);
//
//
//                Log.d("ShowWeatherActivity", "wind " + weather.getTemp());
//
//            }
//            if (showSomething) {
//                weather.setSomething(GetWeatherFromRes.getSomething(getActivity(), cityId));
//                TextView tvSomething = new TextView(getActivity());
//                // tv.setBackgroundResource(R.color.textviewColor);
//                tvSomething.setText(weather.getSomething());
//                containerAddInfo.addView(tvSomething, params);
//
//                Log.d("ShowWeatherActivity", "storm " + weather.getSomething());
//            }
//
////        }
//
//        return rootView;
//    }
//
//    public void setPressure(boolean showPressure) {
//        this.showPressure = showPressure;
//    }
//
//    public void setShowSomething(boolean showSomething) {
//        this.showSomething = showSomething;
//    }
//
//    public void setShowWind(boolean showWind) {
//        this.showWind = showWind;
//    }
//
//    public void setCityId(int cityId) {
//        this.cityId = cityId;
//    }
//
//
//}
