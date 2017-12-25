package net.kdilla.wetharium.utils.tasks;

import android.os.AsyncTask;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by avetc on 25.12.2017.
 */

public class LastUpdateTask extends AsyncTask<Long, Void, String> {
    Timer timer;
    TextView textView;
    long updated;
    long currentTime;
    TimerTask timerTask;
    private final int INTERVAL = 60_000;
    int mins=0;
    public LastUpdateTask(TextView textView) {
        this.textView = textView;
    }


    @Override
    protected String doInBackground(Long... longs) {
        final String[] updText = {null};
        final StringBuilder sb = new StringBuilder();
        updated = longs[0];
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mins++;
                sb.append("last upd:");
                sb.append(mins);
                sb.append(" ago");
              //  updText[0] ="last upd:"+mins+" ago";
            }
        };
        timer.schedule(timerTask, 1000, INTERVAL);
//        return updText[0];
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        textView.setText(s);
    }
}
