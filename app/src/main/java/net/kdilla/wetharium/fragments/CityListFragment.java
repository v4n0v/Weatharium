package net.kdilla.wetharium.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kdilla.wetharium.R;
import net.kdilla.wetharium.utils.GetWeatherFromRes;
import net.kdilla.wetharium.utils.PreferencesID;

/**
 * Created by avetc on 15.11.2017.
 */

public class CityListFragment extends Fragment{

    //private final static int VERTICAL = 1;

    private CitySelectListener mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city_list, container, false);
        RecyclerView citySelectRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_city_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(PreferencesID.VERTICAL);
        citySelectRecyclerView.setLayoutManager(layoutManager);
        citySelectRecyclerView.setAdapter(new MyAdapter());
        return rootView;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cityTextView;

        MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_city_list, parent, false));
            itemView.setOnClickListener(this);
            cityTextView = (TextView) itemView.findViewById(R.id.text_view_city);
        }

        void bind(int position) {
            //   String category = CityWeather.cityWeatherList[position].getCity();
            //String category = City.cities[position].getName();

            String category =  GetWeatherFromRes.getCity(getActivity(), position);
            cityTextView.setText(category);

        }

        @Override
        public void onClick(View view) {
                showWeatherScreen(this.getLayoutPosition());
            //getActivity().onItemClick(this.getLayoutPosition());
        }
    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new MyViewHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(position);
        }


        public int getItemCount() {
         //   return City.cities.length;
            return GetWeatherFromRes.getWeatherList(getActivity()).length;
        }
    }
    @Override
    public void onAttach(Context context) {
        mainActivity = (CitySelectListener) context;
        super.onAttach(context);
    }

     private void showWeatherScreen(int id){
         mainActivity.onListItemClick(id);
     }
}
