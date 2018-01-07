package net.kdilla.wetharium;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.kdilla.wetharium.DB.WeatherDataSource;
import net.kdilla.wetharium.DB.WeatherNote;
import net.kdilla.wetharium.fragments.OnFragmentListener;
import net.kdilla.wetharium.fragments.WeatherInfoFragment;
import net.kdilla.wetharium.services.ServiceWeather;
import net.kdilla.wetharium.utils.FileManager;
import net.kdilla.wetharium.utils.Preferences;
import net.kdilla.wetharium.widget.WidgetWeather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentListener {

    List<WeatherNote> elements;
    WeatherDataSource notesDataSource;

    private boolean isWind = true;
    private boolean isPressure = true;
    private boolean isHumidity = true;


    String city;
    SharedPreferences preferences;

    WeatherInfoFragment weatherInfoFragment;
    ImageView toolbarImage;


    int temperature;
    int pressure;
    int wind;
    int humidity;
    boolean bind;
    String additionalInfo;
    String description;
    int weatherId;
    int tempMin;
    int tempMax;
    long lastUpd;
    Toolbar toolbar;
    ServiceConnection sConn;

    ServiceWeather serviceWeather;
    CollapsingToolbarLayout toolbarLayout;

    TextView loadingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // this.deleteDatabase("weather.db");
        bind = false;
        // получаем информацию из интента
        weatherInfoFragment = new WeatherInfoFragment();
        Intent intent = getIntent();

        loadingTextView = (TextView) findViewById(R.id.toolbar_loading);
        // получаем интент
        if (intent != null) {
            String source = intent.getStringExtra(Preferences.SOURCE);
            if (source != null) {

                // если интент пердан SplashActivity, то все службы уже подгружены, данные поучены, остается передать их во фрагмент
                if (source.equals(Preferences.SPLASH)) {
                    isHumidity = intent.getBooleanExtra(Preferences.ADD_IS_HUMIDITY, true);
                    isWind = intent.getBooleanExtra(Preferences.ADD_IS_WIND, true);
                    isPressure = intent.getBooleanExtra(Preferences.ADD_IS_PRESSURE, true);
                    city = intent.getStringExtra(Preferences.ADD_CITY);
                    tempMax = intent.getIntExtra(Preferences.ADD_TEMP_MAX, 0);
                    tempMin = intent.getIntExtra(Preferences.ADD_TEMP_MIN, 0);
                    //   bind = intent.getBooleanExtra(Preferences.ADD_IS_BIND, true);
                    temperature = intent.getIntExtra(Preferences.ADD_TEMP, 0);
                    pressure = intent.getIntExtra(Preferences.ADD_PRESSURE, 0);
                    humidity = intent.getIntExtra(Preferences.ADD_HUMIDITY, 0);
                    wind = intent.getIntExtra(Preferences.ADD_WIND, 0);
                    pressure = intent.getIntExtra(Preferences.ADD_PRESSURE, 0);
                    //  additionalInfo = intent.getStringExtra(Preferences.ADD_ADDITION);
                    description = intent.getStringExtra(Preferences.ADD_DESCRIPTION);
                    weatherId = intent.getIntExtra(Preferences.ADD_IMAGE_ID, 0);
                    // если сз DBListActivity, то возвращааем сохраненные данные
                } else if (source.equals(Preferences.DB_LIST)) {

                    loadPreferences();

                } else {
                    Log.d(Preferences.DEBUG_KEY, "Wrong SOURCE ");
                }
                // если приложение открыто после закрытия, то загружаем сохранные данные
            } else {
                Log.d(Preferences.DEBUG_KEY,  "MAIN SOURCE = null");
                loadPreferences();

            }
        }

        // заполняем фрагмент
        weatherInfoFragment.configure(city, temperature, pressure, wind, humidity,
                tempMin, tempMax, description, weatherId, formatAdditionInfoString());
        weatherInfoFragment.setOptions(isHumidity, isWind, isPressure);

        date = new Date();


        // устанавливаем соежинение с сервисом
        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //  Toast.makeText(getBaseContext(), "Service connected", Toast.LENGTH_SHORT).show();
                Log.d(Preferences.DEBUG_KEY, "MAIN Service connected");
                serviceWeather = ((ServiceWeather.WeatherBinder) service).getService();
                bind = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bind = false;
            }
        };

        intent = new Intent(getBaseContext(), ServiceWeather.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);

        toolbarImage = (ImageView) findViewById(R.id.header);

        notesDataSource = new WeatherDataSource(getApplicationContext());
        notesDataSource.open();
        elements = notesDataSource.getAllNotes();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                char c = 167;
                String share = "Temperature in " + weatherInfoFragment.getCity() + " is " + weatherInfoFragment.getTemperature() + c + "\n";
                share += "Powered by Weatharium";

                intent.putExtra(Intent.EXTRA_TEXT, share);
                Intent chosenIntent = Intent.createChooser(intent, "Select app");
                startActivity(chosenIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        weatherInfoFragment.setCityImageView(toolbarImage);
        weatherInfoFragment.setToolbarLayout(toolbarLayout);

        fillFragment(weatherInfoFragment);

        //     loadPreferences();
    }

    public void reloadPicture(MenuItem item) {
        reloadPicture();
    }

    private String formatAdditionInfoString() {
        String additionalInfo = "";
        if (isPressure) {
            additionalInfo += "Pressure: " + pressure + " " + getString(R.string.pressure_dim) + "\n";
        }
        if (isWind) {
            additionalInfo += "Wind: " + wind + " " + getString(R.string.wind_dim) + "\n";
        }
        if (isHumidity) {
            additionalInfo += "Humidity: " + humidity + " " + getString(R.string.humidity_dim) + "\n";
        }
        return additionalInfo;
    }

    public void reloadPicture() {
        Toast.makeText(MainActivity.this, "Loading image", Toast.LENGTH_LONG).show();

        FileManager.deleteBitmap(getApplicationContext(), city);
        weatherInfoFragment.getAndSetToolbarImage();
    }




    public void reloadPicture(View view) {
        reloadPicture();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void clearBase(MenuItem item) {
        notesDataSource.deleteAll();
        elements.clear();
        Toast.makeText(MainActivity.this, "Base cleared", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        switch (id) {
            case (R.id.nav_show):
                fillFragment(weatherInfoFragment);
//                loadPreferences();
                break;

            case R.id.nav_last_weather:
                Intent intent = new Intent(MainActivity.this, DBListActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_options:
                Toast.makeText(MainActivity.this, "Options", Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fillFragment(Fragment fragment) {
        if (fragment != null) {// && changeFragment){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void showInputDialog(MenuItem item) {
        selectCityDialog();
    }

    private void selectCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_city));
        final EditText input = new EditText(this);
        //input.setTypeface(Preferences.fontOswaldLight(MainActivity.this.getAssets()));
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(city);
        builder.setView(input);
        builder.setPositiveButton("Show me the weather", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                city = cityTextFormat(input.getText().toString());

                Log.d(Preferences.DEBUG_KEY, "Current date = " + lastUpd);

                serviceWeather.changeCity(city);
                //   fillFragment(weatherInfoFragment);

            }
        });
        builder.show();
    }

    Date date;


    public void updateWeather(MenuItem item){
        serviceWeather.changeCity(city);
        Toast.makeText(this, "Updating", Toast.LENGTH_SHORT).show();
    }

    public void showOptions(MenuItem item) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(this);
        builder.setTitle("Options");
        final View additionView = li.inflate(R.layout.dialog_addition_info_select, null);
        final CheckBox pressureCheckBox = additionView.findViewById(R.id.chb_pressure);
        final CheckBox windCheckBox = additionView.findViewById(R.id.chb_wind);
        final CheckBox humidityCheckBox = additionView.findViewById(R.id.chb_humidity);
        pressureCheckBox.setChecked(isPressure);
        windCheckBox.setChecked(isWind);
        humidityCheckBox.setChecked(isHumidity);
        //  final Button applyButton = additionView.findViewById(R.id.btn_apply);
        builder.setView(additionView);
        builder.setCancelable(true);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // weatherInfoFragment.setCity(input.getText().toString());
                if (pressureCheckBox.isChecked()) isPressure = true;
                else isPressure = false;
                if (windCheckBox.isChecked()) isWind = true;
                else isWind = false;
                if (humidityCheckBox.isChecked()) isHumidity = true;
                else isHumidity = false;

                weatherInfoFragment.setOptions(isHumidity, isWind, isPressure);
                weatherInfoFragment.setAdditionalParams(formatAdditionInfoString());
                weatherInfoFragment.updateAdditionInfo(formatAdditionInfoString());

            }
        });
        builder.show();
    }


    private void loadPreferences() {
//        preferences = getPreferences(MODE_PRIVATE);
        preferences = getSharedPreferences(Preferences.APP_PREFERENCES, Context.MODE_PRIVATE);
        isHumidity = preferences.getBoolean(Preferences.ADD_IS_HUMIDITY, true);
        isWind = preferences.getBoolean(Preferences.ADD_IS_WIND, true);
        isPressure = preferences.getBoolean(Preferences.ADD_IS_PRESSURE, true);

        city = preferences.getString(Preferences.ADD_CITY, "Moscow");
        temperature = preferences.getInt(Preferences.ADD_TEMP, 0);
        pressure = preferences.getInt(Preferences.ADD_PRESSURE, 0);
        humidity = preferences.getInt(Preferences.ADD_HUMIDITY, 0);
        wind = preferences.getInt(Preferences.ADD_WIND, 0);
        pressure = preferences.getInt(Preferences.ADD_PRESSURE, 0);
        additionalInfo = preferences.getString(Preferences.ADD_ADDITION, "");
        description = preferences.getString(Preferences.ADD_DESCRIPTION, "");
        weatherId = preferences.getInt(Preferences.ADD_IMAGE_ID, 0);

        tempMin = preferences.getInt(Preferences.ADD_TEMP_MIN, 0);
        tempMax = preferences.getInt(Preferences.ADD_TEMP_MAX, 0);


    }

    private void savePreferences() {
//        preferences = getPreferences(MODE_PRIVATE);
        preferences = getSharedPreferences(Preferences.APP_PREFERENCES, Context.MODE_PRIVATE);
        WeatherNote note = Preferences.getNoteByName(city, elements);
        if (note != null) {
            SharedPreferences.Editor ed = preferences.edit();

            ed.putString(Preferences.ADD_CITY, city);
            ed.putBoolean(Preferences.ADD_IS_PRESSURE, isPressure);
            ed.putBoolean(Preferences.ADD_IS_WIND, isWind);
            ed.putBoolean(Preferences.ADD_IS_WIND, isHumidity);
            ed.putLong(Preferences.SAVED_LAST_UPD, note.getDate());

            ed.putInt(Preferences.ADD_TEMP, note.getTemperature());
            ed.putInt(Preferences.ADD_PRESSURE, note.getPressure());
            ed.putInt(Preferences.ADD_WIND, note.getWind());
            ed.putInt(Preferences.ADD_HUMIDITY, note.getHumidity());
            ed.putInt(Preferences.ADD_IMAGE_ID, note.getWeatherID());
            ed.putString(Preferences.ADD_ADDITION, additionalInfo);
            ed.putString(Preferences.ADD_DESCRIPTION, note.getDescription());

            ed.putInt(Preferences.ADD_TEMP_MAX, tempMax);
            ed.putInt(Preferences.ADD_TEMP_MIN, tempMin);

            ed.commit();
        }
    }


    @Override
    protected void onStop() {
        savePreferences();
        super.onStop();
    }

    @Override
    protected void onPause() {
        savePreferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        savePreferences();
        notesDataSource.close();
        super.onDestroy();

    }


    @Override
    public void onFragmentItemClick(int id) {
//        switch (id) {
//            case 1:
//                //   Toast.makeText(MainActivity.this, "Fragment Navigation 1", Toast.LENGTH_SHORT).show();
//                serviceWeather.changeCity(city);
//                fillFragment(weatherInfoFragment);
//                Log.d("DEBUGGG", "Splash end");
//                break;
//
//        }
    }


    private String cityTextFormat(String word) {
        if (word == null || word.isEmpty()) return "";
        // делаем первую букву заглавной
        word = word.substring(0, 1).toUpperCase() + word.substring(1);

        // удаляю лишние пробелы
        String[] words = word.split(" ");
        String resultWord="";
        for (int i = 0; i < words.length; i++) {
            if (!words[i].equals(""))
                resultWord+=words[i];
            if (i<words.length-1) resultWord+=" ";
        }
        return resultWord;
    }

    // обновляем базу
    @Override
    public void onDbUpdateWeatherID(String city, int temp, int pressure, int hum, int wind, long date, int weatherId) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM' at ' HH:mm");
        String time = dateFormat.format(System.currentTimeMillis());
        // TODO прилетает неверный city

        // получаем элемент по имени города
        WeatherNote note = getNoteByName(city);
        // если элемент не существует и список пустой, создаем новую запись
        if (note == null || elements.size() == 0) {
            notesDataSource.addNote(city, temp, description, pressure, hum, wind, time, date, weatherId);
            Log.d(Preferences.DEBUG_KEY, "Not exist, created new " + city);
            // иначе редактируем существующую
        } else {
            long id = note.getId();
            notesDataSource.editNote(id, city, temp, description, pressure, hum, wind, time, date, weatherId);
            Log.d(Preferences.DEBUG_KEY, "Exist and edited " + city);
        }

        // очищаем и обновляем список элментов
        elements.clear();
        elements = notesDataSource.getAllNotes();
        Log.d(Preferences.DEBUG_KEY, "Elements count " + elements.size());
    }

    @Override
    public void onTitleUpdate(String title) {
        toolbarLayout.setTitle(title);
    }

    @Override
    public void onFragmentRefresh() {
        fillFragment(weatherInfoFragment);
    }


    private WeatherNote getNoteByName(String name) {
        if (elements.size() != 0) {
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getCity().equals(name)) {
                    return elements.get(i);
                }
            }
        }
        return null;
    }

    protected void onWidgetChangeCity(String city){
        Intent intent_city_update = new  Intent(getApplicationContext(), WidgetWeather.class);
        intent_city_update.putExtra(WidgetWeather.UPDATE_WIDGET_CITY, city);
        sendBroadcast(intent_city_update);
    }

    protected void onMessage() {
        Intent intent_meeting_update = new  Intent(getApplicationContext(), WidgetWeather.class);
        intent_meeting_update.setAction(WidgetWeather.UPDATE_WIDGET_ACTION);
        sendBroadcast(intent_meeting_update);
    }

}
