package com.example.roomtodo;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//（ア）@Databseアノテーション中にentitiesでEntityのリストを記述する必要がある
@Database(entities = MainData.class, version = 1, exportSchema = false)

//（イ）@DatabaseはRoomDatabase を継承した抽象クラスにする必要がある
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
//（ウ）@Daoアノテーションで定義されたinterface中のメソッド
//      にアクセスするための抽象メソッドを定義
    public abstract MainDao mainDao();
}

// @Database では、（ア）（イ）（ウ）の条件を満たす必要がある