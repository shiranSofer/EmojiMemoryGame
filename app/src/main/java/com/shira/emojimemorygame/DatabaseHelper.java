package com.shira.emojimemorygame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Record.db";
    public static final String TABLE_NAME = "Records_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "TIME";
    public static final String COL_4 = "ADDRESS";
    public static final String KEY_GAME_LEVEL = "GAMELEVEL";

    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT NOT NULL, TIME INTEGER NOT NULL, ADDRESS TEXT, GAMELEVEL INTEGER NOT NULL)");
        this.database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, int time, String address, int gameLevel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, name);
        values.put(COL_3, time);
        values.put(COL_4, address);
        values.put(KEY_GAME_LEVEL, gameLevel);
        long result = db.insert(TABLE_NAME, null, values);
        if(result == -1)
            return  false;
        else
            return  true;
    }

    public Cursor getAllDataByDesc(int gameLevel){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_GAME_LEVEL +
                " = " + gameLevel + " ORDER BY " + COL_3 + " DESC", null);
        return  result;
    }

    public Cursor getAllDataByAsc(int gameLevel){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_GAME_LEVEL +
                " = " + gameLevel + " ORDER BY " + COL_3 + " ASC", null);
        return  result;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        return result;
    }
}
