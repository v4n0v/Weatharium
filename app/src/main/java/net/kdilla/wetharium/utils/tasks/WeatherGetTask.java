package net.kdilla.wetharium.utils.tasks;

import android.content.Context;

import android.os.AsyncTask;

import net.kdilla.wetharium.utils.WeatherDataLoader;

import org.json.JSONObject;

/**
 * Created by avetc on 23.12.2017.
 */

public class WeatherGetTask extends AsyncTask<String, Void, JSONObject> {
    Context context;

    public WeatherGetTask(Context context) {
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        final JSONObject jsonObject = WeatherDataLoader.getJSONData(context, strings[0]);

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

    }

}
