package net.kdilla.wetharium.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db"; // название бд
    public static final int DATABASE_VERSION =1; // версия базы данных
    public static final String TABLE_NOTES = "weather_info"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_WIND = "wind";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_WEATHER_ID = "weatherId";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NOTES);
        db.execSQL("CREATE TABLE " + TABLE_NOTES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
                + COLUMN_CITY + " TEXT,"
                + COLUMN_TEMPERATURE + " INTEGER,"
                + COLUMN_PRESSURE + " INTEGER,"
                + COLUMN_HUMIDITY + " INTEGER,"
                + COLUMN_WIND + " INTEGER,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_DATE + " LONG,"
                + COLUMN_WEATHER_ID + " INTEGER" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if ((oldVersion == 1) && (newVersion == 2)) {
                String upgradeQuery = "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_DATE + " LONG DEFAULT 0";
                db.execSQL(upgradeQuery);
            }
        if ((oldVersion == 2) && (newVersion == 3)) {
            String upgradeQuery = "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_WEATHER_ID + " INTEGER DEFAULT 0";
            db.execSQL(upgradeQuery);
        }
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
//            onCreate(db);
//         }
    }
}
