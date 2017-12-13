package net.kdilla.wetharium.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kdilla.wetharium.DB.WeatherNote;
import net.kdilla.wetharium.R;
import net.kdilla.wetharium.utils.Preferences;

import java.util.List;

/**
 * Created by avetc on 12.12.2017.
 */

//@SuppressLint("ValidFragment")
public class LastShownFragment extends Fragment implements LatShownInterface{

//    @SuppressLint("ValidFragment")
//    public LastShownFragment(List<WeatherNote> elements) {
//        this.elements = elements;
//    }

    public void setElements(List<WeatherNote> elements) {
        this.elements = elements;
    }
    List<WeatherNote> elements;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_last_shown, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_last_shown);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(Preferences.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyAdapter());

        return view;
    }

    void updateView(){

    }

    @Override
    public void getElements(List<WeatherNote> elements) {
        this.elements=elements;
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
            String city = elements.get(position).getCity();
            String time = elements.get(position).getTime();
            int temp = elements.get(position).getTemperature();
            int pressure = elements.get(position).getPressure();
            cityTV.setText(city);
            tempTV.setText(String.valueOf(temp));
            pressureTV.setText(String.valueOf(pressure));
            timeTV.setText(time);
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

    private class MyAdapter extends RecyclerView.Adapter< MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new  MyViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder( MyViewHolder holder, int position) {
            holder.bind(position);
        }


        public int getItemCount() {

            return elements.size();
        }
    }
}
