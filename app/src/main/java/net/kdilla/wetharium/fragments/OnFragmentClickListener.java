package net.kdilla.wetharium.fragments;



public interface OnFragmentClickListener {
    void onFragmentItemClick(int id);
    void onDbUpdateWeatherID(String city, int temp, int pressure, int hum, int wind, long date, int weatherId);
    void onTitleUpdate(String title);
    void onFragmentRefresh();
}
