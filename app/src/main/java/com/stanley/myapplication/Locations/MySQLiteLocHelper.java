package com.stanley.myapplication.Locations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.stanley.myapplication.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteLocHelper extends SQLiteOpenHelper {
    private final static String TAG = "MySQLiteLocHelper";

    private final static int DB_VERSION = 10;
    private final static String DATABASE_NAME = "locations.db";


    private final static String CREATE_TABLE_LOCATIONS = "create table locations (userId Integer, date Integer," +
            "distance real, range real, duration real)";

    //upload
    private final static String CREATE_TABLE_RECORDLOCATIONS = "create table recordlocations (userId Integer, type Integer, " +
            "date Integer, duration real)";//0 -- button; 1,2,3 -- special

    private final static String CREATE_TABLE_SPECLOCATIONS = "create table speclocations (userId Integer, id Integer, " +
            "latitude real, longitude real, type Integer)";//1 -- home, 2 -- family friends, 3 -- business
    //upload
    private final static String CREATE_TABLE_SURVEY = "create table survey (userId Integer, date Integer, answers text)";
    //uplad
    private final static String CREATE_TABLE_APP = "create table app (userId Integer, appname text, time Integer)";

    //upload
    private final static String CREATE_TABLE_DAILY_UPLOAD = "create table upload (userId Integer, date Integer, " +
            "distance real, range real, duration real)";

    private DatabaseHelper databaseHelper;

    public MySQLiteLocHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE_LOCATIONS);
        sqLiteDatabase.execSQL(CREATE_TABLE_SPECLOCATIONS);
        sqLiteDatabase.execSQL(CREATE_TABLE_RECORDLOCATIONS);
        sqLiteDatabase.execSQL(CREATE_TABLE_SURVEY);
        sqLiteDatabase.execSQL(CREATE_TABLE_APP);
        sqLiteDatabase.execSQL(CREATE_TABLE_DAILY_UPLOAD);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            System.out.println("UPGRADE DB oldVersion=" + oldVersion + " - newVersion=" + newVersion);
            onCreate(sqLiteDatabase);

            if (oldVersion < 10) {
                sqLiteDatabase.execSQL(CREATE_TABLE_LOCATIONS);
                sqLiteDatabase.execSQL(CREATE_TABLE_SPECLOCATIONS);
                sqLiteDatabase.execSQL(CREATE_TABLE_RECORDLOCATIONS);
                sqLiteDatabase.execSQL(CREATE_TABLE_SURVEY);
                sqLiteDatabase.execSQL(CREATE_TABLE_APP);
                sqLiteDatabase.execSQL(CREATE_TABLE_DAILY_UPLOAD);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**************************************
     * uplad daily
     ****************************************/

    public void insertDailyUpload(LocationDaily locationDaily) {
        //userId Integer, date Integer, " + "distance real, range real, duration real, durationSpec real

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put("userId", locationDaily.getUserId());
        initialValues.put("date", locationDaily.getDate());
        initialValues.put("distance", locationDaily.getDistance());
        initialValues.put("range", locationDaily.getRange());
        initialValues.put("duration", locationDaily.getDuration());

        Log.d(TAG, initialValues.toString());
        db.insert("upload", null, initialValues);

        db.close();

    }

    public LocationDaily getDailyUpload() {
        SQLiteDatabase db = this.getReadableDatabase();
        LocationDaily locationDaily = new LocationDaily();

        String request = "select * from upload";
        Cursor cursor = db.rawQuery(request, new String[]{});

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                locationDaily.setUserId(cursor.getInt(0));
                locationDaily.setDate(cursor.getLong(1));
                locationDaily.setDistance(cursor.getDouble(2));
                locationDaily.setRange(cursor.getDouble(3));
                locationDaily.setDuration(cursor.getDouble(4));

                Log.d(TAG, "get upload daily : " + cursor.getDouble(4));
            }
        }

        cursor.close();
        db.close();

        return locationDaily;

    }

    /**************************************
     * locations
     * <p>
     * "create table locations (userId Integer, date Integer," +
     * "distance real, range real, duration real)";
     ****************************************/

    public int insertLoc(int id, long date, double dist, double range, double duration) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put("userId", id);
        initialValues.put("date", date);
        initialValues.put("distance", dist);
        initialValues.put("range", range);
        initialValues.put("duration", duration);

        Log.d(TAG, initialValues.toString());
        db.insert("locations", null, initialValues);
        int count = testForLocation();
        db.close();
        return count;
    }

    public int testForLocation() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount = db.rawQuery("select count(*) from locations", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        db.close();

        return count;
    }

    public List<LocationDaily> getInfoLoc(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String request = "select date, distance, range, duration from locations where userId = '" + userId + "'";
        Cursor cursor = db.rawQuery(request, new String[]{});

        List<LocationDaily> list = new ArrayList<LocationDaily>();

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                LocationDaily locationDaily = new LocationDaily();
                locationDaily.setDate(cursor.getLong(0));
                locationDaily.setDistance(cursor.getDouble(1));
                locationDaily.setRange(cursor.getDouble(2));
                locationDaily.setDuration(cursor.getDouble(3));
                list.add(locationDaily);
                Log.d(TAG, "get location daily : " + cursor.getDouble(0) + " " + cursor.getDouble(1) +
                        " " + cursor.getDouble(2) + " " + cursor.getInt(3));
            }

        }

        cursor.close();
        db.close();
        return list;

    }

    /**************************************
     * Record Locations
     * <p>
     * "create table recordlocations (userId Integer, type Integer, " +
     * "date Integer, duration real)";//0 -- button; 1,2,3 -- special
     ****************************************/

    public void recordLocation(int userId, int type, long date, double duration) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put("userId", userId);
        initialValues.put("type", type);
        initialValues.put("date", date);
        initialValues.put("duration", duration);

        Log.d(TAG, initialValues.toString());
        db.insert("recordlocations", null, initialValues);

        db.close();

    }


    /**************************************
     * Special Locations
     * <p>
     * create table speclocations (userId Integer, id Integer, " +
     * "latitude real, longitude real, date Integer, type Integer)"
     ****************************************/

    public int specialLocInsert(int userId, LocationBehappy locationBehappy) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put("userId", userId);
        int id = testForSpecialLoc() + 1;
        initialValues.put("id", id);//start from 1
        initialValues.put("latitude", locationBehappy.getLatitude());
        initialValues.put("longitude", locationBehappy.getLongitude());
        initialValues.put("type", locationBehappy.getType());

        Log.d(TAG, initialValues.toString());
        db.insert("speclocations", null, initialValues);

        int count = testForSpecialLoc();
        db.close();
        return count;
    }

//    public void specialLocAddLonely(int userId, double la, double lon, long date, int type) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues initialValues = new ContentValues();
//        initialValues.put("userId", userId);
//        initialValues.put("id", 0);
//        initialValues.put("latitude", la);
//        initialValues.put("longitude", lon);
//        initialValues.put("date", date);
//        initialValues.put("type", type);
//
//        Log.d(TAG, initialValues.toString());
//        db.insert("speclocations", null, initialValues);
//
//        db.close();
//
//    }

    public int testForSpecialLoc() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount = db.rawQuery("select count(*) from speclocations where id > '0'", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        return count;
    }

//    public int testForSpecial() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor mCount = db.rawQuery("select count(*) from speclocations", null);
//        mCount.moveToFirst();
//        int count = mCount.getInt(0);
//        mCount.close();
//        db.close();
//
//        return count;
//    }

    public List<LatLng> readSpecial() {

        List<LatLng> list = new ArrayList<LatLng>();

        SQLiteDatabase db = this.getReadableDatabase();

        String request = "select latitude, longitude from speclocations where id > '0'";
        Cursor cursor = db.rawQuery(request, new String[]{});

        if (cursor.getCount() > 0) {

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                LatLng item = new LatLng(cursor.getDouble(0), cursor.getDouble(1));
                list.add(item);
                //Log.d(TAG, "read from sql la: "+cursor.getDouble(0));
            }
        }

        cursor.close();
        db.close();

        return list;
    }

    /*******************************
     * SURVEY
     ****************************************/

    public int surveyInsert(int id, long date, final String answers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put("userId", id);
        initialValues.put("date", date);
        initialValues.put("answers", answers);

        Log.d(TAG, initialValues.toString());
        db.insert("survey", null, initialValues);

//        final int idDB = id;
//        final long dateDB = date;
//        final String answersDB = answers;

//        new AsyncTask<Integer, Void, Void>() {
//            @Override
//            protected Void doInBackground(Integer... params) {
//                try {
//
//                    databaseHelper = new DatabaseHelper();
//                    databaseHelper.connectToDB_insertAnswers(idDB, dateDB, answersDB);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }.execute(1);
        db.close();

        int count = testForSurvey();
        return count;

    }

    public int testForSurvey() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount = db.rawQuery("select count(*) from survey", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        db.close();

        return count;
    }

    /**************************************
     * app
     ****************************************/
    public int appInsert(int id, String appName, long time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put("userId", id);
        initialValues.put("appname", appName);
        initialValues.put("time", time);

        Log.d(TAG, initialValues.toString());
        db.insert("app", null, initialValues);

//        final String appNameDB = appName;
//        final long timeDB = time;
//        final int userId = id;
//        new AsyncTask<Integer, Void, Void>() {
//            @Override
//            protected Void doInBackground(Integer... params) {
//                try {
//
//                    databaseHelper = new DatabaseHelper();
//                    databaseHelper.connectToDB_insertApp(userId, appNameDB, timeDB);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }.execute(1);
        db.close();

        int count = testForApp();
        return count;

    }

    public int testForApp() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount = db.rawQuery("select count(*) from app", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        db.close();

        return count;
    }


    public void deleteAll(Context context) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS users");
//        db.execSQL("DROP TABLE IF EXISTS routes");
//        db.execSQL("D");

        context.deleteDatabase(DATABASE_NAME);
    }

}
