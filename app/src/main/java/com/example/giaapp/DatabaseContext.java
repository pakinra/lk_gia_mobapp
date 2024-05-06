package com.example.giaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class DatabaseContext extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gia.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных

    public DatabaseContext(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS app_properties (property_name TEXT, property_value TEXT)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS app_properties");
        onCreate(db);
    }
}

