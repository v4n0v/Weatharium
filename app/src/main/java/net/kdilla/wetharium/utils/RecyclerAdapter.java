package net.kdilla.wetharium.utils;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kdilla.wetharium.DB.WeatherNote;

import java.util.List;

/**
 * Created by avetc on 16.12.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<WeatherNote> elements;
    private Activity activity;

    public RecyclerAdapter(Activity activity,  List<WeatherNote>  elements) {
        this.elements = elements;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {

        viewHolder.item.setText(elements.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return (null != elements ? elements.size() : 0);
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item;

        public ViewHolder(View view) {
            super(view);
            item = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}