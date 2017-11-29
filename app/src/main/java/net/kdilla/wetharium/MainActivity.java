package net.kdilla.wetharium;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import net.kdilla.wetharium.fragments.CitySelectListener;
import net.kdilla.wetharium.fragments.WeatherDetailFragment;
import net.kdilla.wetharium.utils.PreferencesID;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CitySelectListener{
    private boolean isWind;
    private boolean isPressure;
    private boolean isSomething;
    private CheckBox chbPressure;
    private CheckBox chbWind;
    private CheckBox chbStorm;

    List<String> elements;
    ArrayAdapter<String> adapter;

private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initViews();
//        elements = new ArrayList<>();
//        String[] itemList = this.getResources().getStringArray(R.array.addition_list);
//        for (int i = 0; i < itemList.length; i++) {
//            elements.add(itemList[i]);
//        }
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, elements);
//        listView.setAdapter(adapter);
//
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
//
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
//                MenuInflater inflater = actionMode.getMenuInflater();
//                inflater.inflate(R.menu.context_menu, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//
//                // AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
//
//                switch (menuItem.getItemId()) {
//                    case R.id.menu_edit:
//                        editElement();
//                        return true;
//                    case R.id.menu_delete:
//                        deleteElement();
//                        return true;
//                    default:
//                        actionMode.finish();
//                        return false;
//                }
//
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode actionMode) {
//
//            }
//        });
//        WeatherDetailFragment detailFragment = new WeatherDetailFragment();
//        detailFragment.setCityId(1);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.fragment_container, detailFragment);
//        transaction.commit();
    }

    private void editElement() {
//    private void editElement(int id) {
        int a =  1+(int) (Math.random()*100);
        int b =  1+(int) (Math.random()*100);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(),
                "edit element a= "+a+", b= "+b+"\na/b="+a/b,
                duration);
        toast.show();
    }

    private void deleteElement() {
        int a =  1+(int) (Math.random()*100);
        int b =  1+(int) (Math.random()*100);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(),
                "delete element.\n a= "+a+", b= "+b+"\na-b="+(a-b),
                duration);
        toast.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }
    private void initViews() {
//        chbPressure = (CheckBox) findViewById(R.id.chb_pressure);
//        chbWind = (CheckBox) findViewById(R.id.chb_wind);
//        chbStorm = (CheckBox) findViewById(R.id.chb_something);
      //  listView = (ListView) findViewById(R.id.list);
    }

    @Override
    public void onListItemClick(int id) {
        View fragmentContainer = findViewById(R.id.fragment_container);
//        isWind = chbWind.isChecked();
//        isPressure = chbPressure.isChecked();
//        isSomething = chbStorm.isChecked();

        if (fragmentContainer != null) {
          // WeatherDetailFragment detailFragment =  WeatherDetailFragment.init(getIntent().getBundleExtra("key"));
            WeatherDetailFragment detailFragment = new WeatherDetailFragment();

            detailFragment.setCityId(id);
            detailFragment.setPressure(isPressure);
            detailFragment.setShowSomething(isSomething);
            detailFragment.setShowWind(isWind);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, detailFragment);
//            transaction.replace(R.id.fragment_container, detailFragment);

            transaction.commit();
        } else {
            Intent intent = new Intent(this, ShowWeatherActivity.class);
            intent.putExtra(PreferencesID.EXTRA_CITY_NOM, id);
            intent.putExtra(PreferencesID.ADD_PRESSURE, isPressure);
            intent.putExtra(PreferencesID.ADD_WIND, isWind);
            intent.putExtra(PreferencesID.ADD_STORM, isSomething);

            startActivity(intent);

        }

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
            if (requestCode == PreferencesID.REQUEST_CODE_WEATHER) {
                // созраняем возвращенное значение в переменную
                isPressure = data.getBooleanExtra(PreferencesID.ADD_PRESSURE, false);
                isWind = data.getBooleanExtra(PreferencesID.ADD_WIND, false);
                isSomething = data.getBooleanExtra(PreferencesID.ADD_STORM, false);
//                String returnString = data.getStringExtra(PreferencesID.SAVED_COUNTRY_WEATHER);
//                // инициализируем текстовое поле и выводим на экран
//            //    TextView infoTextView = (TextView) findViewById(R.id.textview_weather);
//                infoTextView.setVisibility(View.VISIBLE);
//                infoTextView.setText(returnString);
            }
        }

    }

}
