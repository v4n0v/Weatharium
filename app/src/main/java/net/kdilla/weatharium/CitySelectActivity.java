package net.kdilla.weatharium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import net.kdilla.weatharium.utils.GetWeatherFromRes;
import net.kdilla.weatharium.utils.PreferencesID;

public class CitySelectActivity extends AppCompatActivity {

    private final static int VERTICAL = 1;
    private CheckBox chbPressure;
    private CheckBox chbWind;
    private CheckBox chbStorm;
    private boolean isWind;
    private boolean isPressure;
    private boolean isStorm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select_frame);
        initViews();

        RecyclerView citySelectRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(VERTICAL);
        citySelectRecyclerView.setLayoutManager(layoutManager);
        citySelectRecyclerView.setAdapter(new MyAdapter());
    }

    private void initViews() {
        chbPressure = (CheckBox) findViewById(R.id.chb_pressure);
        chbWind = (CheckBox) findViewById(R.id.chb_wind);
        chbStorm = (CheckBox) findViewById(R.id.chb_storm);
    }

    //Класс, который содержит в себе все элементы списка
    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView categoryNameTextView;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.city_list_item, parent, false));
            itemView.setOnClickListener(this);
            categoryNameTextView = (TextView) itemView.findViewById(R.id.category_name_text_view);
        }

        void bind(int position) {
            //   String category = CityWeather.cityWeatherList[position].getCity();
            String category = GetWeatherFromRes.getCity(CitySelectActivity.this, position);
            categoryNameTextView.setText(category);
        }

        @Override
        public void onClick(View view) {
            showWeatherActivity(this.getLayoutPosition());
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            return new MyViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
//        public int getItemCount() {
//            return CityWeather.cityWeatherList.length;
//        }
        public int getItemCount() {
            return GetWeatherFromRes.getWeatherList(CitySelectActivity.this).length;
        }
    }

    private void showWeatherActivity(int categoryId) {
        Intent intent = new Intent(this, ShowWeatherFromList.class);
        intent.putExtra(PreferencesID.EXTRA_CITY_NOM, categoryId);
        isWind = chbWind.isChecked();
        isPressure = chbPressure.isChecked();
        isStorm = chbStorm.isChecked();

        // передаю значение погоды на сл активити
        intent.putExtra(PreferencesID.ADD_PRESSURE, isPressure);
        intent.putExtra(PreferencesID.ADD_WIND, isWind);
        intent.putExtra(PreferencesID.ADD_STORM, isStorm);
        startActivity(intent);
    }
}
