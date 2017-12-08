package net.kdilla.wetharium;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.kdilla.wetharium.utils.PreferencesID;

import java.util.ArrayList;
import java.util.List;


public class OptionsActivity  extends AppCompatActivity{
    ListView listView;
    List<String> elements;
    ArrayAdapter<String> adapter;

    boolean isPressure;
    boolean isWind;
    boolean isSomething;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        listView = (ListView) findViewById(R.id.list_addition_info);
        elements = new ArrayList<>();
        String[] itemList = this.getResources().getStringArray(R.array.addition_list);
        for (int i = 0; i < itemList.length; i++) {
            elements.add(itemList[i]);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, elements);
        listView.setAdapter(adapter);
      //  registerForContextMenu(listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                onBackPressed();

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);

    }

    private List<Integer> getSelectedElements() {
        List<Integer> selectedFiles = new ArrayList<Integer>();
        SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            if (sparseBooleanArray.valueAt(i)) {
                selectedFiles.add(sparseBooleanArray.keyAt(i));
            }
        }

        List<Boolean> selected = new ArrayList<Boolean>();

        return selectedFiles;
    }


    @Override
    public void onBackPressed() {
         SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
        isPressure=false;  isWind=false; isSomething=false;
        if (sparseBooleanArray!=null && sparseBooleanArray.size()>0) {
            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                if (sparseBooleanArray.valueAt(i)) {
                    if (sparseBooleanArray.keyAt(i) == 0) {
                        isPressure = true;
                    } else if (sparseBooleanArray.keyAt(i) == 1) {
                        isWind = true;
                    } else if (sparseBooleanArray.keyAt(i) == 2) {
                        isSomething = true;
                    } else {
                        Log.e("ERROR", "wrong keyAt sparseBooleanArray");
                    }
                }
            }
        }

        // создаем Intent
        Intent returnIntent = new Intent();
        // заполняем переменной currentWeather с ключем SAVED_COUNTRY_WEATHER
         returnIntent.putExtra(PreferencesID.ADD_PRESSURE, isPressure);
        returnIntent.putExtra(PreferencesID.ADD_WIND, isWind);
        returnIntent.putExtra(PreferencesID.ADD_STORM, isSomething);
        // сохраняем значения для отправки
        setResult(RESULT_OK, returnIntent);
        // завершаем работу активити
        finish();
        super.onBackPressed();
    }
}
