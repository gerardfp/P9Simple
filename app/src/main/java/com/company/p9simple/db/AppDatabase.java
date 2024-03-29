package com.company.p9simple.db;

import android.content.Context;

import com.company.p9simple.model.Usuario;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Usuario.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AppDao dao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "app-db")
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }
}
