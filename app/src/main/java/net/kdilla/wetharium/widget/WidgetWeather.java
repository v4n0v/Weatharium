package net.kdilla.wetharium.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import net.kdilla.wetharium.MainActivity;
import net.kdilla.wetharium.R;
import net.kdilla.wetharium.services.ServiceWeather;
import net.kdilla.wetharium.utils.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String description;

    private ServiceConnection sConn;

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
               // msg = intent.getStringExtra(Preferences.ADD_CITY);
                city = intent.getStringExtra(Preferences.ADD_CITY);
                temp = intent.getIntExtra(Preferences.ADD_TEMP, 0);
                description= intent.getStringExtra(Preferences.ADD_DESCRIPTION);
            } catch (NullPointerException e) {
                Log.e("Error", "msg = null");
            }
//            remoteViews.setTextViewText(R.id.widget_city, city);

            //TODO сделать форматирование города, сокращенеи слова и если неск слов то у всех, кроме последнего по 1ой букве в слове
            String weatherInfo = cityWidgetFormat(city)+" "+
                     description+" "+Preferences.temperatureFormat(temp)+Preferences.CELCIUM;

            remoteViews.setTextViewText(R.id.widget_temp,weatherInfo);
            writeDate(remoteViews, appWidgetManager, widget);
            appWidgetManager.updateAppWidget(widget, remoteViews);

            Log.d(Preferences.DEBUG_KEY, "ACTION_WIDGET_RECEIVER end");
        }
       else if (TIME_WIDGET_RECEIVER.equals(action)) {

            writeDate(remoteViews, appWidgetManager, widget);

            Log.d(Preferences.DEBUG_KEY, "TIME_WIDGET_RECEIVER end");
        }
    }

    private String cityWidgetFormat(String str){
        String[] lines = str.split(" ");
        // если слов больше 0
        if (lines.length>0){

            // если одно слово, возвращам его
            if (lines.length==1){
                return lines[0]; //.substring(0, 8);
            } else {
                // если больше, то сокращаем все все кроме последнего до
                String cityName="";
                for (int i = 0; i < lines.length; i++) {
                    if (i<lines.length-1){
                        lines[i] = lines[i].substring(0, 1).toUpperCase();
                    }
                    cityName+=lines[i];
                    if (i<lines.length-1){
                        lines[i]+=".";
                    }
                }
                if (cityName.length()>7){
                    cityName = cityName.substring(0,4)+".";
                  //  +cityName.substring(cityName.length()-3,1);
                }
                return cityName;
            }
        } else return "";

    }
    private void writeDate(RemoteViews remoteViews, AppWidgetManager appWidgetManager, ComponentName watchWidget ){
        Date dt = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDay = new SimpleDateFormat("dd MMM ccc");
        remoteViews.setTextViewText(R.id.widget_clock, format.format(dt));
        remoteViews.setTextViewText(R.id.widget_num, formatDay.format(dt));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }


    private void setStartTimer(Context context){
        long time = (long) Math.rint(System.currentTimeMillis()/60000);
        // получаем минуту, округляем, и прибавляем минуту, получаем слудущую
        // минуту, от коророй и включаем таймер на обновление через каждую минуту

        time = time*60000 + 60000;
//        time = time*60000;
        Intent intent = new Intent(context, WidgetWeather.class);
        intent.setAction(TIME_WIDGET_RECEIVER);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,
                60_000, pIntent);

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        setStartTimer(context);

        Log.d(Preferences.DEBUG_KEY, "onEnabled finished");
    }


}
