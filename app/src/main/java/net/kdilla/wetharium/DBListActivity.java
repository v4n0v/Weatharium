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
import android.widget.TextView;

import net.kdilla.wetharium.DB.WeatherDataSource;
import net.kdilla.wetharium.DB.WeatherNote;
import net.kdilla.wetharium.utils.RecyclerAdapter;

import java.util.List;

/**
 * Created by avetc on 16.12.2017.
 */
public class DBListActivity extends AppCompatActivity {

    private List<WeatherNote> weatherNotese;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    WeatherDataSource notesDataSource;
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_list);

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


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(this, weatherNotese);
        recyclerView.setAdapter(new  MyAdapter());

    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cityTV;
        private TextView tempTV;
        private TextView humidityTV;
        private TextView windTV;
        private TextView pressureTV;
        private TextView timeTV;
        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_last_shown, parent, false));
            itemView.setOnClickListener(this);
            cityTV = (TextView) itemView.findViewById(R.id.last_city);
            tempTV = (TextView) itemView.findViewById(R.id.last_temp);
            humidityTV = (TextView) itemView.findViewById(R.id.last_humidity);
            pressureTV = (TextView) itemView.findViewById(R.id.last_pressure);
            windTV = (TextView) itemView.findViewById(R.id.last_wind);
            timeTV = (TextView) itemView.findViewById(R.id.last_time);
        }

        void bind(int position) {
            String city = weatherNotese.get(position).getCity();
            String time = weatherNotese.get(position).getTime();
            int temp = weatherNotese.get(position).getTemperature();
            int pressure = weatherNotese.get(position).getPressure();
            int humidity = weatherNotese.get(position).getHumidity();
            int wind = weatherNotese.get(position).getWind();
            cityTV.setText(city);
            tempTV.setText(getString(R.string.temp)+" "+String.valueOf(temp)+getString(R.string.cels));
            pressureTV.setText(getString(R.string.pressure)+" "+String.valueOf(pressure)+getString(R.string.pressure_dim));
            humidityTV.setText(getString(R.string.humidity)+" "+String.valueOf(humidity)+getString(R.string.humidity_dim));
            windTV.setText(getString(R.string.wind)+" "+String.valueOf(wind)+getString(R.string.wind_dim));
            timeTV.setText(getString(R.string.updated)+time);
            //     muscleGroupTextView.setText(muscles[position]);
        }

        @Override
        public void onClick(View view) {
            //startWorkout(view);

        }
    }
//
//    private void startWorkout(View view) {
//        mainActivity.onFragmentButtonClick(view.getId());
//    }

    private class       MyAdapter extends RecyclerView.Adapter< MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(DBListActivity.this);
            return new  MyViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder( MyViewHolder holder, int position) {
            holder.bind(position);
        }
        public int getItemCount() {
            return weatherNotese.size();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DBListActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
