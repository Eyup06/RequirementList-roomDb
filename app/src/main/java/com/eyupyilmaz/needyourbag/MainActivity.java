package com.eyupyilmaz.needyourbag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.eyupyilmaz.needyourbag.Adapter.Adapter;
import com.eyupyilmaz.needyourbag.Constants.MyConstants;
import com.eyupyilmaz.needyourbag.Data.AppData;
import com.eyupyilmaz.needyourbag.Database.RoomDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView);

        addAllTitles();
        addAllImages();
        persistAppData();
        database = RoomDB.getInstance(this);
        System.out.println("---------------------------------> "+database.mainDao().getAllSelected(false).get(0).getItemname());

        adapter = new Adapter(this,titles,images,MainActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    private static final int TIME_INTERVAL = 2000;

    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed+TIME_INTERVAL>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    private void persistAppData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        database = RoomDB.getInstance(this);
        AppData appData = new AppData(database);
        int last = prefs.getInt(AppData.LAST_VERSION, 0);
        if (!prefs.getBoolean(MyConstants.FIRST_TIME_CAMEL_CASE,false)){
            appData.persistAllData();
            editor.putBoolean(MyConstants.FIRST_TIME_CAMEL_CASE,true);
            editor.commit();
        } else if (last < AppData.NEW_VERSION) {
            database.mainDao().deleteAllSystemItems(MyConstants.SYSTEM_SMALL);
            appData.persistAllData();
            editor.putInt(AppData.LAST_VERSION,AppData.NEW_VERSION);
            editor.commit();
        }
    }

    private void addAllTitles(){

        titles = new ArrayList<>();
        titles.add(MyConstants.BASIC_NEEDS_CAMEL_CASE);
        titles.add(MyConstants.CLOTHING_CAMEL_CASE);
        titles.add(MyConstants.PERSONAL_CARE_CAMEL_CASE);
        titles.add(MyConstants.BABY_NEEDS_CAMEL_CASE);
        titles.add(MyConstants.HEALTH_CAMEL_CASE);
        titles.add(MyConstants.TECHNOLOGY_CAMEL_CASE);
        titles.add(MyConstants.FOOD_CAMEL_CASE);
        titles.add(MyConstants.BEACH_SUPPLIES_CAMEL_CASE);
        titles.add(MyConstants.CAR_SUPPLIES_CAMEL_CASE);
        titles.add(MyConstants.NEEDS_CAMEL_CASE);
        titles.add(MyConstants.MY_LIST_CAMEL_CASE);
        titles.add(MyConstants.MY_SELECTIONS_CAMEL_CASE);
    }

    private void addAllImages(){
        images = new ArrayList<>();
        images.add(R.drawable.basicneeds_ic);
        images.add(R.drawable.clothes_ic);
        images.add(R.drawable.personel_care_ic);
        images.add(R.drawable.baby_need_ic);
        images.add(R.drawable.healthcare_ic);
        images.add(R.drawable.tech_need_ic);
        images.add(R.drawable.food_need_ic);
        images.add(R.drawable.beach_need);
        images.add(R.drawable.car_ic);
        images.add(R.drawable.needs_ic);
        images.add(R.drawable.mylist_ic);
        images.add(R.drawable.my_selection_ic);
    }
}