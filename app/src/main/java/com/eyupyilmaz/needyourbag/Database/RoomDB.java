package com.eyupyilmaz.needyourbag.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.eyupyilmaz.needyourbag.Dao.ItemsDao;
import com.eyupyilmaz.needyourbag.Models.Items;

@Database(entities = Items.class,version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB database;
    private static String DATABASE_NAME = "MyDb";

    public synchronized static RoomDB getInstance(Context context){
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
            RoomDB.class,DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract ItemsDao mainDao();

}
