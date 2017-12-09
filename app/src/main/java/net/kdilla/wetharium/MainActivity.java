package net.kdilla.wetharium;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.kdilla.wetharium.fragments.WeatherInfoFragment;
import net.kdilla.wetharium.gson.WeatherGSon;
import net.kdilla.wetharium.gson.WeatherGsonDeserializer;
import net.kdilla.wetharium.gson.WeatherMain;
import net.kdilla.wetharium.gson.WeatherMainDeserializer;
import net.kdilla.wetharium.utils.PreferencesID;
import net.kdilla.wetharium.utils.WeatherDataLoader;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "myAvatar.png";
    private boolean isWind;
    private boolean isPressure;
    private boolean isHumidity;
    private String city;

    private final Handler handler = new Handler();

    // private TextView texts;
    SharedPreferences preferences;
    FragmentManager myFragmentManager;
    String cityText;
    String temperature;
    String additionalInfo;


    Button loadAva;
    Button saveAva;
    Button deleteAva;
    Button changeAva;


    String jsonX;
    ImageView imageAvatar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadAva = (Button)findViewById(R.id.ava_load);
        saveAva = (Button)findViewById(R.id.ava_save);
        deleteAva = (Button)findViewById(R.id.ava_delete);
        changeAva=(Button)findViewById(R.id.ava_change);


        loadAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAvatar();
            }
        });

        saveAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap avatar = ((BitmapDrawable) imageAvatar.getDrawable()).getBitmap();
                saveAvatar(avatar);
            }
        });

        deleteAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              deleteAvatar();
            }
        });
        changeAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable ava1 = getResources().getDrawable(R.drawable.ava_fry);
                Drawable ava2 = getResources().getDrawable(R.drawable.ava_pepe);
                Drawable avaCurrent = imageAvatar.getDrawable();

                Drawable.ConstantState stateAva1 = ava1.getConstantState();
                Drawable.ConstantState stateAva2 = ava2.getConstantState();
                Drawable.ConstantState stateAvaCurrent = avaCurrent.getConstantState();

                if (stateAvaCurrent!=null) {
                    if (stateAvaCurrent.equals(stateAva1))
                        imageAvatar.setImageDrawable(ava2);
                    else if (stateAvaCurrent.equals(stateAva2))
                        imageAvatar.setImageDrawable(ava1);
                    else Toast.makeText(MainActivity.this, "Wrong ava", Toast.LENGTH_SHORT).show();
                } else imageAvatar.setImageDrawable(ava1);
            }
        });

        imageAvatar = (ImageView) findViewById(R.id.iv_avatar);
        // нажимая на картинку, открываем галерею
        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, PreferencesID.REQUEST_CODE_IMAGE);
            }
        });

        // нажимая долго на картинку, сохраняем ее в хранилище
        imageAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bitmap avatar = ((BitmapDrawable) imageAvatar.getDrawable()).getBitmap();

                saveAvatar(avatar);

                return true;
            }
        });

        loadPreferences();

        //   texts = (TextView) findViewById(R.id.tv_texts);
        getWeather(city);

    }

    private void saveAvatar(Bitmap bitmap) {
        try {

            File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILENAME);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(), bitmap, file.getName(), file.getName()); // регистрация в фотоальбоме
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this, "Saved in storage as " + FILENAME, Toast.LENGTH_SHORT).show();
    }

    private void deleteAvatar() {
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILENAME);
        if (file.exists()) {
            file.delete();
            Toast.makeText(MainActivity.this, "Ava deleted  from storage", Toast.LENGTH_SHORT).show();
        }

    }

    private Bitmap loadAvatar() {
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), FILENAME);
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    private void loadPreferences() {


        preferences = getPreferences(MODE_PRIVATE);
        String savedCity = preferences.getString(PreferencesID.SAVED_CITY, "Moscow");
        isPressure = preferences.getBoolean(PreferencesID.SAVED_PRESSURE, false);
        isWind = preferences.getBoolean(PreferencesID.SAVED_WIND, false);
        isHumidity = preferences.getBoolean(PreferencesID.SAVED_HUMIDITY, false);
        if (savedCity.equals("") || savedCity == null) savedCity = "Moscow";
        city = savedCity;


        Bitmap loadedAvatar = loadAvatar();

        // если ава была сохранена, тогда грузим ее, если нет, тогда друзим дефолтного пепе
        if (loadedAvatar != null) {
            imageAvatar.setImageBitmap(loadedAvatar);
            Toast.makeText(MainActivity.this, "Ava loaded from storage", Toast.LENGTH_SHORT).show();
        } else {
            imageAvatar.setImageDrawable(getResources().getDrawable(R.drawable.ava_fry));
            Toast.makeText(MainActivity.this, "Ava loaded from resources", Toast.LENGTH_SHORT).show();
        }

    }


    private void getWeather(final String city) {
        new Thread() {
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(getApplicationContext(), city);

//                final PostManager postManager = new PostManager();
//                final String jsonStr =jsonObject.toString();
//                try {
//                    String response = postManager.post("https://jsonplaceholder.typicode.com/posts", jsonStr);
//                    Log.d("POST", response);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                Log.d("TAG", jsonObject.toString());
                if (jsonObject != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(jsonObject);

                        }
                    });
                }
            }
        }.start();
    }

    private void savePreferences() {



        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(PreferencesID.SAVED_CITY, city);
        ed.putBoolean(PreferencesID.SAVED_PRESSURE, isPressure);
        ed.putBoolean(PreferencesID.SAVED_WIND, isWind);
        ed.putBoolean(PreferencesID.SAVED_HUMIDITY, isHumidity);

        ed.commit();
    }


    public void showInputDialog(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.change_city_dialog));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(city);
        builder.setView(input);
        builder.setPositiveButton("Show me the weather", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                city = input.getText().toString();
                getWeather(city);
            }
        });
        builder.show();
    }

    private void renderWeather(JSONObject json) {
        renderWeatherJSON(json);
        //      texts.setText(json.toString() + "\n" + additionalInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }


    public void showOptions(MenuItem item) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivityForResult(intent, PreferencesID.REQUEST_CODE_WEATHER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // проверяем есть ли результат
        if (resultCode == RESULT_OK) {
            // если код тот же, что мы отправили
            switch (requestCode) {
                case PreferencesID.REQUEST_CODE_WEATHER:
                    isPressure = data.getBooleanExtra(PreferencesID.ADD_PRESSURE, false);
                    isWind = data.getBooleanExtra(PreferencesID.ADD_WIND, false);
                    isHumidity = data.getBooleanExtra(PreferencesID.ADD_STORM, false);
                    break;

                case PreferencesID.REQUEST_CODE_IMAGE:
                    final Uri imageUri = data.getData();
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageAvatar.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }

            getWeather(city);
        }

    }
    String description;
    private void renderWeatherJSON(JSONObject json) {
        try {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(WeatherGSon.class, new WeatherGsonDeserializer())
                    .registerTypeAdapter(WeatherMain.class, new WeatherMainDeserializer())

                    .create();
            WeatherGSon weatherForGSon = gson.fromJson(json.toString(), WeatherGSon.class);
//
//            cityText = json.getString("name").toUpperCase(Locale.US) + ", "
//                    + json.getJSONObject("sys").getString("country");
//            additionalInfo="";
//            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
//            JSONObject main = json.getJSONObject("main");
//            JSONObject wind = json.getJSONObject("wind");
//            if (isPressure) {
//                additionalInfo += "Pressure: " + main.getString("pressure") + " " + getString(R.string.pressure_dim) + "\n";
//            }
//            if (isWind) {
//                additionalInfo += "Wind: " + wind.getString("speed") + " " + getString(R.string.wind_dim) + "\n";
//            }
//            if (isHumidity) {
//                additionalInfo += "Humidity: " + main.getString("humidity") + " " + getString(R.string.humidity_dim) + "\n";
//            }
//
//            temperature = main.getString("temp");
//            Drawable weatherIcon = getWeatherIcon(details.getInt("id"));
            additionalInfo = "";
            cityText = weatherForGSon.getCity();
            if (isPressure) {
                additionalInfo += "Pressure: " + weatherForGSon.getPressure() + " " + getString(R.string.pressure_dim) + "\n";
            }
            if (isWind) {
                additionalInfo += "Wind: " + weatherForGSon.getWind() + " " + getString(R.string.wind_dim) + "\n";
            }
            if (isHumidity) {
                additionalInfo += "Humidity: " + weatherForGSon.getHumidity() + " " + getString(R.string.humidity_dim) + "\n";
            }

            temperature = weatherForGSon.getTemperature();
            description=weatherForGSon.getDescription();
            Drawable weatherIcon = getWeatherIcon(weatherForGSon.getId());
            WeatherInfoFragment weatherInfoFragment = new WeatherInfoFragment();
            weatherInfoFragment.setParams(cityText, temperature, additionalInfo, description, weatherIcon);


            Log.i("GSON", gson.toJson(weatherForGSon));


            myFragmentManager = getFragmentManager();
            //if (getIntent().getBundleExtra()==null){
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.info_container, weatherInfoFragment, PreferencesID.TAG_1);
            fragmentTransaction.commit();
            //}

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Drawable getWeatherIcon(int actualId) {
        int id = actualId / 100;


        switch (id) {
            case 2:
                return getResources().getDrawable(R.drawable.thunder);

            case 3:
                return getResources().getDrawable(R.drawable.drizzle);

            case 5:
                return getResources().getDrawable(R.drawable.rainy);

            case 6:
                return getResources().getDrawable(R.drawable.snowie);

            case 7:
                return getResources().getDrawable(R.drawable.foggy);

            case 8:
                return getResources().getDrawable(R.drawable.cloudly);

            default:
                return getResources().getDrawable(R.drawable.sunny);

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
        super.onDestroy();

    }
}
