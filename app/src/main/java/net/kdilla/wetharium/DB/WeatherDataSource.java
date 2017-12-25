package net.kdilla.wetharium.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avetc on 12.12.2017.
 */

public class WeatherDataSource {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private String[] notesAllColumn = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_CITY,
            DBHelper.COLUMN_TEMPERATURE,
            DBHelper.COLUMN_PRESSURE,
            DBHelper.COLUMN_HUMIDITY,
            DBHelper.COLUMN_WIND,
            DBHelper.COLUMN_TIME,
            DBHelper.COLUMN_DATE,
            DBHelper.COLUMN_WEATHER_ID
    };

    public WeatherDataSource(Context context) {
        dbHelper = new DBHelper(context);

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public WeatherNote addNote(String city, int temperature,
                               int pressure, int humidity, int wind, String time, long date, int wId) {

        WeatherNote newNote = new WeatherNote();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CITY, city);
        values.put(DBHelper.COLUMN_TEMPERATURE, temperature);
        values.put(DBHelper.COLUMN_PRESSURE, pressure);
        values.put(DBHelper.COLUMN_HUMIDITY, humidity);
        values.put(DBHelper.COLUMN_WIND, wind);
        values.put(DBHelper.COLUMN_TIME, time);
        values.put(DBHelper.COLUMN_DATE, date);
        values.put(DBHelper.COLUMN_WEATHER_ID, wId);
        long insertId = database.insert(DBHelper.TABLE_NOTES, null,
                values);


        newNote.setCity(city);
        newNote.setTemperature(temperature);
        newNote.setPressure(pressure);
        newNote.setHumidity(humidity);
        newNote.setWind(wind);
        newNote.setTime(time);
        newNote.setDate(date);
        newNote.setWeatherID(wId);
        newNote.setId(insertId);

        return newNote;
    }


    public void editNote(long id, String city, int temperature, int pressure,
                         int humidity, int wind, String time, long date, int wId) {
        ContentValues editedNote = new ContentValues();
        editedNote.put(dbHelper.COLUMN_ID, id);

        editedNote.put(DBHelper.COLUMN_CITY, city);
        editedNote.put(DBHelper.COLUMN_TEMPERATURE, temperature);
        editedNote.put(DBHelper.COLUMN_PRESSURE, pressure);
        editedNote.put(DBHelper.COLUMN_HUMIDITY, humidity);
        editedNote.put(DBHelper.COLUMN_WIND, wind);
        editedNote.put(DBHelper.COLUMN_TIME, time);
        editedNote.put(DBHelper.COLUMN_DATE, date);
        editedNote.put(DBHelper.COLUMN_WEATHER_ID, wId);
        database.update(DBHelper.TABLE_NOTES,
                editedNote,
                DBHelper.COLUMN_ID + "=" + id,
                null);
    }

    public void deleteNote(WeatherNote note) {
        long id = note.getId();
        database.delete(DBHelper.TABLE_NOTES, DBHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAll() {
        database.delete(DBHelper.TABLE_NOTES, null, null);
    }

    public List<WeatherNote> getAllNotes() {
        List<WeatherNote> notes = new ArrayList<WeatherNote>();

        Cursor cursor = database.query(DBHelper.TABLE_NOTES,
                notesAllColumn, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WeatherNote note = cursorToNote(cursor);
            notes.add(note);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return notes;
    }

    private WeatherNote cursorToNote(Cursor cursor) {
        WeatherNote note = new WeatherNote();
        note.setId(cursor.getLong(0));
        note.setCity(cursor.getString(1));
        note.setTemperature(cursor.getInt(2));
        note.setPressure(cursor.getInt(3));
        note.setHumidity(cursor.getInt(4));
        note.setWind(cursor.getInt(5));
        note.setTime(cursor.getString(6));
        note.setDate(cursor.getLong(7));
        note.setWeatherID(cursor.getInt(8));
        return note;
    }
}
