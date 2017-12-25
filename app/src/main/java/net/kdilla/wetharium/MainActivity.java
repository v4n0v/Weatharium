package net.kdilla.wetharium;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import net.kdilla.wetharium.DB.WeatherDataSource;
import net.kdilla.wetharium.DB.WeatherNote;

import net.kdilla.wetharium.fragments.OnFragmentClickListener;
import net.kdilla.wetharium.fragments.SplashFragment;
import net.kdilla.wetharium.fragments.WeatherInfoFragment;
import net.kdilla.wetharium.services.ServiceWeather;
import net.kdilla.wetharium.utils.FileManager;
import net.kdilla.wetharium.utils.FlickrSearch;
import net.kdilla.wetharium.utils.GoogleSearchThread;

import net.kdilla.wetharium.utils.Preferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentClickListener {

    List<WeatherNote> elements;
    WeatherDataSource notesDataSource;


    public void setWind(boolean wind) {
        isWind = wind;
    }

    public void setPressure(boolean pressure) {
        isPressure = pressure;
    }

    public void setHumidity(boolean humidity) {
        isHumidity = humidity;
    }

    private boolean isWind = true;
    private boolean isPressure = true;
    private boolean isHumidity = true;

    String additionalInfo;
    String city;
    SharedPreferences preferences;

    WeatherInfoFragment weatherInfoFragment;
    SplashFragment splashFragment;

    Typeface font;
    ImageView toolbarImage;
    FlickrSearch flickrImage;
    ArrayList<Bitmap> cityImages;
    private final Handler handler = new Handler();
    GoogleSearchThread imageSearch;


    BroadcastReceiver br;
    int temperature;
    int pressure;
    int wind;
    int humidity;
    long lastUpd;
    Toolbar toolbar;
    ServiceConnection sConn;
    boolean bind;
    ServiceWeather serviceWeather;
    CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.deleteDatabase("weather.db");

        //serviceWeather=new ServiceWeather();

//        splashFragment=new SplashFragment();
//        fillFragment(splashFragment);

//        // получаем информацию из интента
//        isHumidity = savedInstanceState.getBoolean(Preferences.ADD_IS_HUMIDITY);
//        isWind = savedInstanceState.getBoolean(Preferences.ADD_IS_WIND);
//        isPressure = savedInstanceState.getBoolean(Preferences.ADD_IS_PRESSURE);
//        city = savedInstanceState.getString(Preferences.ADD_CITY);
//        bind = savedInstanceState.getBoolean(Preferences.ADD_IS_BIND);
        weatherInfoFragment = new WeatherInfoFragment();

        date = new Date();

        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Toast.makeText(getBaseContext(), "Service connected", Toast.LENGTH_SHORT).show();
                Log.d("ServiceWeather", "Service connected");
                serviceWeather = ((ServiceWeather.WeatherBinder) service).getService();
                serviceWeather.changeCity(city);
                bind = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bind = false;
            }
        };

        Intent intent = new Intent(getBaseContext(), ServiceWeather.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);


        toolbarImage = (ImageView) findViewById(R.id.header);


        notesDataSource = new WeatherDataSource(getApplicationContext());

        notesDataSource.open();

        elements = notesDataSource.getAllNotes();

//        String jsonImg = GoogleSearchThread.getAndSetImage("moscow city");

        // так не получается запустить
//        new Thread() {
//            public void run() {
//                GoogleSearch.getAndSetImage("moscow city");
//            }
//        }.start();

        // так получается

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

//
//        br = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                int temp = intent.getIntExtra(Preferences.ADD_TEMP, 0);
//                int hum = intent.getIntExtra(Preferences.ADD_HUMIDITY, 0);
//                int wind = intent.getIntExtra(Preferences.ADD_WIND, 0);
//                int press = intent.getIntExtra(Preferences.ADD_PRESSURE, 0);
//                String decription = intent.getStringExtra(Preferences.ADD_DESCRIPTION);
//                String additionalInfo = "";
//
//                if (isPressure) {
//                    additionalInfo += "Pressure: " + press + " " + getString(R.string.pressure_dim) + "\n";
//                }
//                if (isWind) {
//                    additionalInfo += "Wind: " + wind + " " + getString(R.string.wind_dim) + "\n";
//                }
//                if (isHumidity) {
//                    additionalInfo += "Humidity: " + hum + " " + getString(R.string.humidity_dim) + "\n";
//                }
//
//
//                weatherInfoFragment.setCity(city);
//                weatherInfoFragment.setAdditionalParams(additionalInfo);
//                weatherInfoFragment.setTemperature(temp);
//                weatherInfoFragment.setDescription(decription);
//            }
//        };
//
//
//        IntentFilter intFilt = new IntentFilter(Preferences.BROADCAST_ACTION);
//        // регистрируем (включаем) BroadcastReceiver
//        registerReceiver(br, intFilt);


        weatherInfoFragment.setCityImageView(toolbarImage);
        weatherInfoFragment.setToolbarLayout(toolbarLayout);
        weatherInfoFragment.setIsPressure(isPressure);
        weatherInfoFragment.setIsWind(isWind);
        weatherInfoFragment.setIsHumidity(isHumidity);
        weatherInfoFragment.setCity(city);

        //  serviceWeather.setCity(city);


        loadPreferences();


        fillFragment(weatherInfoFragment);

    }

    public void reloadPicture(MenuItem item) {
        reloadPicture();
    }

    public void reloadPicture() {
        FileManager.deleteBitmap(getApplicationContext(), city);
        weatherInfoFragment.getAindSetToolbarImage();
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
//                LastShownFragment lastFragment = new LastShownFragment();
//                lastFragment.setElements(elements);
//                fillFragment(lastFragment);
                Intent intent = new Intent(MainActivity.this, DBListActivity.class);
                startActivity(intent);

                break;
            case R.id.nav_options:
                Toast.makeText(MainActivity.this, "Options", Toast.LENGTH_SHORT).show();

                break;
        }
        //fillFragment(fragment);
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
                city = input.getText().toString();
                //  Intent intent = new Intent(Preferences.BROADCAST_ACTION);
                weatherInfoFragment.setCity(city);

                if (elements.size() != 0) {
                    for (int i = 0; i < elements.size(); i++) {
                        if (elements.get(i).getCity().equals(city)) {
                            weatherInfoFragment.setLastDate(elements.get(i).getDate());
                            Log.d("DATE", "Last date = " + elements.get(i).getDate());
                        }
                    }
                }
                lastUpd = System.currentTimeMillis();

                // weatherInfoFragment.setDate(lastUpd);
                Log.d("DATE", "Current date = " + lastUpd);

                serviceWeather.changeCity(city);

                fillFragment(weatherInfoFragment);
                //  dbUpdate(elements, city, lastUpd);

                elements.clear();
                elements = notesDataSource.getAllNotes();
                Log.d("dbUpdate", "Elements count " + elements.size());
                savePreferences();
            }
        });
        builder.show();
    }

    Date date;

    void dbUpdate(List<WeatherNote> elements, String city, long dateTime) {
        boolean isExist = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM' at ' HH:mm:ss ");

        String time = dateFormat.format(System.currentTimeMillis());

        if (elements.size() != 0) {
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getCity().equals(city)) {

                    long id = elements.get(i).getId();
                    notesDataSource.editNote(id,
                            city,
                            weatherInfoFragment.getTemperature(),
                            weatherInfoFragment.getPressure(),
                            weatherInfoFragment.getHumidity(),

                            weatherInfoFragment.getWind(),
                            time,
                            dateTime,
                            weatherInfoFragment.getWeatherId()

                    );

                    isExist = true;
                    Toast.makeText(MainActivity.this, "Exist and edited", Toast.LENGTH_SHORT).show();
                    Log.d("dbUpdate", "Exist and edited " + city);
                    // изменил базу ,прерываю цикл
                    break;
                }

            }
            if (!isExist) {
                notesDataSource.addNote(weatherInfoFragment.getCity(),
                        weatherInfoFragment.getTemperature(),
                        weatherInfoFragment.getPressure(),
                        weatherInfoFragment.getHumidity(),
                        weatherInfoFragment.getWind(),
                        time,
                        dateTime,
                        weatherInfoFragment.getWeatherId()
                );
                Toast.makeText(MainActivity.this, "Not exist, created new", Toast.LENGTH_SHORT).show();
                Log.d("dbUpdate", "Not exist, created new " + city);
            }

        } else {
            notesDataSource.addNote(weatherInfoFragment.getCity(),
                    weatherInfoFragment.getTemperature(),
                    weatherInfoFragment.getPressure(),
                    weatherInfoFragment.getHumidity(),
                    weatherInfoFragment.getWind(),
                    time,
                    dateTime,
                    weatherInfoFragment.getWeatherId()
            );
            Toast.makeText(MainActivity.this, "Table is empty, created new note", Toast.LENGTH_SHORT).show();
            Log.d("dbUpdate", "Table is empty, created new note " + city);
        }

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
                //Toast.makeText(MainActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                weatherInfoFragment.setOptions(isWind, isPressure, isHumidity);
                //   weatherInfoFragment.setCity(weatherInfoFragment.getCity());
                serviceWeather.changeCity(weatherInfoFragment.getCity());

                // fillFragment(weatherInfoFragment);
            }
        });
        builder.show();
    }


    private void loadPreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        String savedCity = preferences.getString(Preferences.SAVED_CITY, "Moscow");

        weatherInfoFragment.setIsPressure(preferences.getBoolean(Preferences.SAVED_PRESSURE, true));
        weatherInfoFragment.setIsWind(preferences.getBoolean(Preferences.SAVED_WIND, true));
        weatherInfoFragment.setIsHumidity(preferences.getBoolean(Preferences.SAVED_HUMIDITY, true));
        // weatherInfoFragment.setLastDate(preferences.getLong(Preferences.SAVED_LAST_UPD, 0));

        if (savedCity.equals("") || savedCity == null) savedCity = "Moscow";
        city = savedCity;

        if (elements.size() > 0) {
            WeatherNote note = getNoteByName(city);
            if (note != null) {
                weatherInfoFragment.setLastDate(note.getDate());
                weatherInfoFragment.setDate(System.currentTimeMillis());
            }
        }

        // обновляю базу, обновляю время текущего элемента
        // dbUpdate(elements, city, System.currentTimeMillis());
        toolbarLayout.setTitle(city);
        weatherInfoFragment.setCity(city);
    }

    private void savePreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(Preferences.SAVED_CITY, weatherInfoFragment.getCity());
        ed.putBoolean(Preferences.SAVED_PRESSURE, weatherInfoFragment.isPressure());
        ed.putBoolean(Preferences.SAVED_WIND, weatherInfoFragment.isWind());
        ed.putBoolean(Preferences.SAVED_HUMIDITY, weatherInfoFragment.isHumidity());
        ed.putLong(Preferences.SAVED_LAST_UPD, lastUpd);
        ed.commit();
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
        switch (id) {
            case 1:
                //   Toast.makeText(MainActivity.this, "Fragment Navigation 1", Toast.LENGTH_SHORT).show();
                serviceWeather.changeCity(city);
                fillFragment(weatherInfoFragment);
                Log.d("Splash", "Splash end");
                break;

        }
    }

    @Override
    public void onDbUpdateWeatherID(String city, int temp, int pressure, int hum, int wind, long date, int weatherId) {
        boolean isExist = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM' at ' HH:mm:ss ");

        String time = dateFormat.format(System.currentTimeMillis());

        if (elements.size() != 0) {
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getCity().equals(city)) {
                    long id = elements.get(i).getId();
                    notesDataSource.editNote(id, city, temp, pressure,hum, wind, time,date,weatherId);
                    isExist = true;
                    Log.d("dbUpdate", "Exist and edited " + city);
                    // изменил базу ,прерываю цикл
                    break;
                }

            }
            if (!isExist) {
                notesDataSource.addNote(city, temp, pressure,hum, wind, time,date,weatherId);
                Log.d("dbUpdate", "Not exist, created new " + city);
            }

        } else {
            notesDataSource.addNote(city, temp, pressure,hum, wind, time,date,weatherId);
            Log.d("dbUpdate", "Table is empty, created new note " + city);
        }

    }


    private WeatherNote getNoteByName(String name) {
        if (elements.size() != 0) {
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getCity().equals(city)) {
                    return elements.get(i);
                }
            }
        }
        return null;
    }


}
