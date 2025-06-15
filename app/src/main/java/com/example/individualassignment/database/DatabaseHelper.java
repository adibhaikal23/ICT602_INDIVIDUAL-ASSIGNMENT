package com.example.individualassignment.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electricity.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "bills";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT, " +
                "unit INTEGER, " +
                "total REAL, " +
                "rebate REAL, " +
                "final_cost REAL)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertBill(String month, int unit, double total, double rebate, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("month", month);
        values.put("unit", unit);
        values.put("total", total);
        values.put("rebate", rebate);
        values.put("final_cost", finalCost);
        db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllBills() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);
    }
}
