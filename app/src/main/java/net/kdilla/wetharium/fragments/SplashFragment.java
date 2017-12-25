package net.kdilla.wetharium.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kdilla.wetharium.R;

public class SplashFragment extends Fragment {

    private OnFragmentClickListener mainActivity;
    @Override
    public void onAttach(Context context) {
        mainActivity = (OnFragmentClickListener) context;
        super.onAttach(context);
    }
    boolean isReady;


    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_splash, container, false);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showNext(1);
            }
        };
        timer.start();

        return view;
    }


    private void showNext(int id) {
        mainActivity.onFragmentItemClick(id);
    }
}
