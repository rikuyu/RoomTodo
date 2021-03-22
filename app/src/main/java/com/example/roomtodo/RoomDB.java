package com.example.roomtodo;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = MainData.class, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    // インスタンス作成
    private static RoomDB database;
    // DB名を宣言
    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context) {
        // 接続チェック
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MainDao mainDao();
}
