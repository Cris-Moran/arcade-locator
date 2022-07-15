package com.example.arcadefinder;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = RoomGameLocation.class, version = 1)
public abstract class GameLocationDatabase extends RoomDatabase {

    private static GameLocationDatabase instance;

    public abstract GameLocationDao gameLocationDao();

    public static synchronized GameLocationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GameLocationDatabase.class, "game_location_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
