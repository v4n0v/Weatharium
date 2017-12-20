package net.kdilla.wetharium;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

import com.squareup.picasso.Picasso;

import net.kdilla.wetharium.DB.WeatherDataSource;
import net.kdilla.wetharium.DB.WeatherNote;
import net.kdilla.wetharium.fragments.LastShownFragment;
import net.kdilla.wetharium.fragments.LatShownInterface;
import net.kdilla.wetharium.fragments.WeatherInfoFragment;
import net.kdilla.wetharium.utils.GoogleSearchThread;
import net.kdilla.wetharium.utils.Preferences;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<WeatherNote> elements;
    WeatherDataSource notesDataSource;

    LatShownInterface lisetener;

    LastShownFragment lastShownFragment;

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
    Typeface font;
    ImageView toolbarImage;


    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarImage = (ImageView) findViewById(R.id.header);

        //  this.deleteDatabase("weather.db");
        notesDataSource = new WeatherDataSource(getApplicationContext());

        notesDataSource.open();

        elements = notesDataSource.getAllNotes();

//        String jsonImg = GoogleSearchThread.getJson("moscow city");

        // так не получается запустить
//        new Thread() {
//            public void run() {
//                GoogleSearch.getJson("moscow city");
//            }
//        }.start();

        // так получается


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, weatherInfoFragment.getCity() + " " + weatherInfoFragment.getTemperature());

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
        weatherInfoFragment = new

                WeatherInfoFragment();

        loadPreferences();
        // создаю экземпляк класса поиска картиноки возвращаю список из 5 картинок города
        GoogleSearchThread imageSearch = new GoogleSearchThread(city);
        ArrayList<String> images = null;
        // костыль для ожидания потока
        while (images == null) {
            images = imageSearch.getImageLinks();
        }
 //       Picasso.with(getApplicationContext()).load(images.get(0)).into(toolbarImage);
        String firstLink = images.get(2);
        setCityImageToolbar(firstLink);

        fillFragment(weatherInfoFragment);

    }

    private void setCityImageToolbar(final String url){

        final Handler handler = new Handler();
        new Thread() {
            @Override
            public void run() {
                try                {
                    URL newUrl = new URL(url);
                    //final Bitmap mIcon_val = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());
                    Log.d("BITMAP", newUrl.toString());
                    InputStream in = newUrl.openStream();
                    if (in==null){
                        Log.d("BITMAP", "Wrong InputStream link");
                    }
                    final Bitmap mIcon11 = BitmapFactory.decodeStream(in);
                    if (mIcon11==null){
                        Log.d("BITMAP", "Wrong bitmap link");
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            toolbarImage.setImageBitmap(mIcon11);
                        }
                    });

                } catch (Exception e){
                    Log.e("BITMAP", e.getMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Bitmap getBmp(final String url){
        final Bitmap[] bmp = {null};
        synchronized (this) {

            try {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL newUrl = new URL(url);
                            //final Bitmap mIcon_val = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());
                            Log.d("BITMAP", newUrl.toString());
                            InputStream in = newUrl.openStream();
                            if (in == null) {
                                Log.d("BITMAP", "Wrong InputStream link");
                            }
                            final Bitmap mIcon11 = BitmapFactory.decodeStream(in);
                            if (mIcon11 == null) {
                                Log.d("BITMAP", "Wrong bitmap link");
                            }

//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
                            bmp[0] = mIcon11;
//                            }
//                        });

                        } catch (Exception e) {
                            Log.e("BITMAP", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bmp[0];
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

                weatherInfoFragment.getWeather(input.getText().toString());

                GoogleSearchThread imageSearch = new GoogleSearchThread(input.getText().toString());
                ArrayList<String> images = null;
                // костыль для ожидания потока
                while (images == null) {
                    images = imageSearch.getImageLinks();
                }

                setCityImageToolbar(images.get(3));
                fillFragment(weatherInfoFragment);
                dbUpdate(elements, input.getText().toString());

                elements.clear();
                elements = notesDataSource.getAllNotes();
                Log.d("dbUpdate", "Elements count " + elements.size());

            }
        });
        builder.show();
    }

    void dbUpdate(List<WeatherNote> elements, String city) {
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
                            time

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
                        time
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
                    time
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
                weatherInfoFragment.setAdditionalParams(isWind, isPressure, isHumidity);
                weatherInfoFragment.getWeather(weatherInfoFragment.getCity());

                // fillFragment(weatherInfoFragment);
            }
        });
        builder.show();
    }


    private void loadPreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        String savedCity = preferences.getString(Preferences.SAVED_CITY, "Moscow");

        weatherInfoFragment.setPressure(preferences.getBoolean(Preferences.SAVED_PRESSURE, true));
        weatherInfoFragment.setWind(preferences.getBoolean(Preferences.SAVED_WIND, true));
        weatherInfoFragment.setHumidity(preferences.getBoolean(Preferences.SAVED_HUMIDITY, true));
        if (savedCity.equals("") || savedCity == null) savedCity = "Moscow";
        city = savedCity;
        weatherInfoFragment.setCity(city);
    }

    private void savePreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(Preferences.SAVED_CITY, weatherInfoFragment.getCity());
        ed.putBoolean(Preferences.SAVED_PRESSURE, weatherInfoFragment.isPressure());
        ed.putBoolean(Preferences.SAVED_WIND, weatherInfoFragment.isWind());
        ed.putBoolean(Preferences.SAVED_HUMIDITY, weatherInfoFragment.isHumidity());

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

}
