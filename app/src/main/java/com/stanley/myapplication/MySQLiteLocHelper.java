package com.stanley.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

public class MySQLiteLocHelper extends SQLiteOpenHelper{

    private final static int DB_VERSION = 10;
    private final static String DATABASE_NAME = "locations.db";
    private final static String CREATE_TABLE_USERS = "create table users (userId text primary key, password text)";
    private final static String CREATE_TABLE_LOCATIONS = "create table locations (locId Integer primary key autoincrement, " +
            " userId Integer, name text, latitude real, longitude real, times Integer, duration real)";

    public MySQLiteLocHelper (Context context){
        super(context, DATABASE_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_LOCATIONS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try{
            System.out.println("UPGRADE DB oldVersion="+oldVersion+" - newVersion="+newVersion);
            onCreate(sqLiteDatabase);

            if (oldVersion<10){
                sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
                sqLiteDatabase.execSQL(CREATE_TABLE_LOCATIONS);
            }
        }
        catch (Exception e){e.printStackTrace();}

    }

    public int insertLoc(Location location, int id, String name, int times, int duration) {
        SQLiteDatabase db = this.getWritableDatabase();

        // userId Integer, name text, latitude real, longitude real, times Integer, duration real

        ContentValues initialValues = new ContentValues();
        initialValues.put("userId",id);
        initialValues.put("name",name);
        initialValues.put("userId",id);
        initialValues.put("latitude", location.getLatitude());
        initialValues.put("longitude", location.getLongitude());
        initialValues.put("times",times);
        initialValues.put("duration",duration);
        Log.d("12222222", initialValues.toString());
        db.insert("locations", null, initialValues);

        int count = test();
        return count;
    }

    public int test(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount= db.rawQuery("select count(*) from locations", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        db.close();

        return count;
    }

}
