package net.kdilla.weatharium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               showNext();
            }
        };
        timer.start();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.img_logo || id == R.id.textview_header) {
            showNext();
        }

    }

    private void showNext(){
        Log.d("SplashActivity","Show next");
        Intent intent = new Intent(SplashActivity.this, CitySelectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        startActivity(intent);
    }
}
