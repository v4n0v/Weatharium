package net.kdilla.wetharium;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.kdilla.wetharium.DB.WeatherDataSource;
import net.kdilla.wetharium.DB.WeatherNote;
import net.kdilla.wetharium.utils.Preferences;
import net.kdilla.wetharium.utils.RecyclerAdapter;

import java.util.List;

/**
 * Created by avetc on 16.12.2017.
 */
public class DBListActivity extends AppCompatActivity {

    private List<WeatherNote> weatherNotese;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    float midSize;
    float bigSize;
    float lilSize;
    WeatherDataSource notesDataSource;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_list);


        bigSize = getResources().getDimension(R.dimen.last_city_font_size);
        midSize = getResources().getDimension(R.dimen.info_max_min_font_size);
        lilSize = getResources().getDimension(R.dimen.last_addition_font_size);

        notesDataSource = new WeatherDataSource(getApplicationContext());
        notesDataSource.open();
        weatherNotese = notesDataSource.getAllNotes();
//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (weatherNotese.size() > 0) {
            recyclerView = (RecyclerView) findViewById(R.id.recycler);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new RecyclerAdapter(this, weatherNotese);
            recyclerView.setAdapter(new MyAdapter());
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cityTV;
        private TextView tempTV;
        private TextView humidityTV;
        private TextView windTV;
        private TextView pressureTV;
        private TextView descriptionTV;
        private TextView timeTV;
        private ImageView img;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_last_shown, parent, false));
            itemView.setOnClickListener(this);
            cityTV = (TextView) itemView.findViewById(R.id.last_city);
            tempTV = (TextView) itemView.findViewById(R.id.last_temp);
            humidityTV = (TextView) itemView.findViewById(R.id.last_humidity);
            pressureTV = (TextView) itemView.findViewById(R.id.last_pressure);
            windTV = (TextView) itemView.findViewById(R.id.last_wind);
            timeTV = (TextView) itemView.findViewById(R.id.last_time);
            descriptionTV = (TextView) itemView.findViewById(R.id.last_description);
            img = (ImageView) itemView.findViewById(R.id.last_img);
        }

        void bind(int position) {
            if (weatherNotese.size() > 0) {

                String city = weatherNotese.get(position).getCity();
                String time = weatherNotese.get(position).getTime();
                String description = weatherNotese.get(position).getDescription();
                int temp = weatherNotese.get(position).getTemperature();
                int pressure = weatherNotese.get(position).getPressure();
                int humidity = weatherNotese.get(position).getHumidity();
                int wind = weatherNotese.get(position).getWind();
                int weatherId = weatherNotese.get(position).getWeatherID();


                String pressureInfo = pressure + getString(R.string.pressure_dim);
                String windInfo = wind + getString(R.string.wind_dim);
                String humInfo = humidity + getString(R.string.humidity_dim);

                int length = city.length();
                if (city.length() < 11) cityTV.setTextSize(32);
                else if (city.length() < 18) cityTV.setTextSize(24);
                else if (city.length() < 24) cityTV.setTextSize(18);

                cityTV.setText(city);
                tempTV.setText(temp + getString(R.string.cels));
                pressureTV.setText(pressureInfo);
                humidityTV.setText(humInfo);
                windTV.setText(windInfo);
                timeTV.setText(getString(R.string.updated) + " " + time);

                if (weatherId==800) description=getString(R.string.clear);
                else description=Preferences.getWeatherDescription(weatherId, getApplicationContext());
                descriptionTV.setText(description);

                img.setImageDrawable(Preferences.getWeatherIcon(weatherId, getApplication()));
            }
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(DBListActivity.this);
            return new MyViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(position);
        }

        public int getItemCount() {
            return weatherNotese.size();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DBListActivity.this, MainActivity.class);
        intent.putExtra(Preferences.SOURCE, Preferences.DB_LIST);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
