package net.kdilla.wetharium.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import net.kdilla.wetharium.DB.WeatherDataSource;
import net.kdilla.wetharium.DB.WeatherNote;
import net.kdilla.wetharium.MainActivity;
import net.kdilla.wetharium.R;
import net.kdilla.wetharium.utils.Preferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by avetc on 29.12.2017.
 */

public class WidgetWeather extends AppWidgetProvider {
    public static final String UPDATE_WIDGET_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String ITEM_ON_CLICK_ACTION = "android.appwidget.action.ITEM_ON_CLICK";
    public static final String NOTE_TEXT = "note_text";
    public static final String UPDATE_WIDGET_CITY = "update city";
    private String city;
    private int temp;


    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    public static String TIME_WIDGET_RECEIVER = "TimenReceiverWidget";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
     //   super.onUpdate(context, appWidgetManager, appWidgetIds);

     //   updateAppWidget(context, appWidgetManager, appWidgetIds[0]);
        // получаем минуту, округляем ее в меньшую сторону, и прибавляем минуту, получаем слудущую
        // минуту, от коророй и включаем таймер на обновление через каждую минуту
//        long time = (long) Math.floor(System.currentTimeMillis()/60000);
//        time = time*60000 + 60000;
//        Intent intent = new Intent(context, WidgetWeather.class);
//        intent.setAction(TIME_WIDGET_RECEIVER);
//        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context
//                .getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC, time,
//                60000, pIntent);

        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }


    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int i) {
        setStartTimer(context);


        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.layout_widget);
        ComponentName watchWidget = new ComponentName(context, WidgetWeather.class);
        Intent configIntent = new Intent(context, MainActivity.class);
//        rv.setTextViewText(R.id.widget_city, city);
//        rv.setTextViewText(R.id.widget_temp, Preferences.temperatureFormat(temp));
        PendingIntent pIntent = PendingIntent.getActivity(context, i,
                configIntent, 0);
        rv.setOnClickPendingIntent(R.id.widget_clock, pIntent);

        appWidgetManager.updateAppWidget(watchWidget, rv);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = WeatherWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget);

        views.setTextViewText(R.id.widget_city, widgetText);
//
//        List<WeatherNote> elements;
//        WeatherDataSource notesDataSource;
//        notesDataSource = new WeatherDataSource(context);
//        notesDataSource.open();
//
//        elements = notesDataSource.getAllNotes();
//        Preferences.getNoteByName(widgetText, elements);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);final String action = intent.getAction();
        Log.d(Preferences.DEBUG_KEY, "widget started receiving");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, WidgetWeather.class);
//        if (ACTION_WIDGET_RECEIVER.equals(action)) {
        if (ACTION_WIDGET_RECEIVER.equals(action)) {
            String msg = "null";
            try {
                msg = intent.getStringExtra(Preferences.ADD_CITY);
                city = intent.getStringExtra(Preferences.ADD_CITY);
                temp = intent.getIntExtra(Preferences.ADD_TEMP, 0);

            } catch (NullPointerException e) {
                Log.e("Error", "msg = null");
            }
            remoteViews.setTextViewText(R.id.widget_city, city);
            remoteViews.setTextViewText(R.id.widget_temp, Preferences.temperatureFormat(temp)+Preferences.CELCIUM);
            writeDate(remoteViews, appWidgetManager, widget);
            appWidgetManager.updateAppWidget(widget, remoteViews);

            Log.d(Preferences.DEBUG_KEY, "ACTION_WIDGET_RECEIVER end");
        }
       else if (TIME_WIDGET_RECEIVER.equals(action)) {
//            Date dt = new Date();
//            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//
            writeDate(remoteViews, appWidgetManager, widget);
           // Toast.makeText(context, format.format(dt), Toast.LENGTH_SHORT).show();
            Log.d(Preferences.DEBUG_KEY, "TIME_WIDGET_RECEIVER end");
        }
    }

    private void writeDate(RemoteViews remoteViews, AppWidgetManager appWidgetManager, ComponentName watchWidget ){
        Date dt = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        remoteViews.setTextViewText(R.id.widget_clock, format.format(dt));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    private void setStartTimer(Context context){
        long time = (long) Math.floor(System.currentTimeMillis()/60000);
//        time = time*60000 + 60000;
        time = time*60000;
        Intent intent = new Intent(context, WidgetWeather.class);
        intent.setAction(TIME_WIDGET_RECEIVER);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, time,
                60_000, pIntent);

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        // получаем минуту, округляем ее в меньшую сторону, и прибавляем минуту, получаем слудущую
        // минуту, от коророй и включаем таймер на обновление через каждую минуту

        setStartTimer(context);

//        long time = (long) Math.floor(System.currentTimeMillis()/60000);
//        time = time*60000 + 60000;
//        Intent intent = new Intent(context, WidgetWeather.class);
//        intent.setAction(TIME_WIDGET_RECEIVER);
//        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context
//                .getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC, time,
//                60000, pIntent);
        Log.d(Preferences.DEBUG_KEY, "onEnabled finished");
    }


}
