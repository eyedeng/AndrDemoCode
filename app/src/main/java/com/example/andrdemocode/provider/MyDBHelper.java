package com.example.andrdemocode.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author dengyan
 * @date 2024/3/12
 * @desc
 */
public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context, int version) {
        super(context, "mydb.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE my_table (_id INTEGER PRIMARY KEY, name TEXT, value TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 这里处理数据库的版本更新
    }
}
