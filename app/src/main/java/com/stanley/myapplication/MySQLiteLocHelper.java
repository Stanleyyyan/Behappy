package com.stanley.myapplication;

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
import com.stanley.myapplication.Locations.LocationBehappy;
import com.stanley.myapplication.Locations.LocationDaily;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteLocHelper extends SQLiteOpenHelper {
    private final static String TAG = "MySQLiteLocHelper";

    private final static int DB_VERSION = 10;
    private final static String DATABASE_NAME = "locations.db";


    private final static String CREATE_TABLE_LOCATIONS = "create table locations (userId Integer, date Integer," +
            "distance real, range real, duration real)";

    //upload
    private final static String CREATE_TABLE_RECORDLOCATIONS = "create table recordlocations (item Integer primary key autoincrement not null, " +
            "userId Integer, type Integer, " +
            "date Integer, duration real)";//0 -- button; 1,2,3 -- special

    private final static String CREATE_TABLE_SPECLOCATIONS = "create table speclocations (userId Integer, id Integer, " +
            "latitude real, longitude real, type Integer)";//1 -- home, 2 -- family friends, 3 -- business
    //upload
    private final static String CREATE_TABLE_SURVEY = "create table survey (item Integer primary key autoincrement not null, " +
            "userId Integer, date Integer, answers text)";
    //uplad
    private final static String CREATE_TABLE_APP = "create table app (item Integer primary key autoincrement not null, " +
            "userId Integer, appname text, time Integer)";

    //upload
    private final static String CREATE_TABLE_DAILY_UPLOAD = "create table upload (item Integer primary key autoincrement not null, " +
            "userId Integer, date Integer, " +
            "distance real, range real, duration real)";

    //upload
    private final static String CREATE_TABLE_CONTACT = "create table contact (item Integer primary key autoincrement not null, " +
            "userId Integer, contrcdId Integer, contactName text, " +
            "contrcdDateTime text, contrcdType Integer, duration Integer, tag Integer)";

    private final static String CREATE_TABLE_RECORD = "create table record (item Integer primary key autoincrement not null, " +
        "numrecord Integer, numapp Integer, numcontact Integer, numsurvey Integer, numdaily Integer)";

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
        sqLiteDatabase.execSQL(CREATE_TABLE_CONTACT);
        sqLiteDatabase.execSQL(CREATE_TABLE_RECORD);


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
                sqLiteDatabase.execSQL(CREATE_TABLE_CONTACT);
                sqLiteDatabase.execSQL(CREATE_TABLE_RECORD);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**************************************
     * record
     *
     * "create table record (item Integer primary key autoincrement not null, " +
     "numrecord Integer, numapp Integer, numcontact Integer, numsurvey Integer, numdaily Integer)"
     ****************************************/

    public void insertDailyRecord(int record, int app, int contact, int survey, int daily){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();

        initialValues.put("numrecord", record);
        initialValues.put("numapp", app);
        initialValues.put("numcontact", contact);
        initialValues.put("numsurvey", survey);
        initialValues.put("numdaily", daily);

        Log.d(TAG, "daily record : " + initialValues.toString());
        db.insert("record", null, initialValues);

        int count = testForRecor();
        Log.d(TAG, "get test result: " + count);
//        Log.d(TAG, "get test result: " + testForRecorapp());
//        Log.d(TAG, "get test result: " + testForRecordaily());
//        Log.d(TAG, "get test result: " + testForRecorcontact());

        db.close();
    }

    public int testForRecor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount = db.rawQuery("select MAX(item) from record", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        db.close();
        return count;
    }

//    public int testForRecorapp() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor mCount = db.rawQuery("select numapp from record where item = '0'", null);
//        mCount.moveToFirst();
//        int count = mCount.getInt(0);
//        mCount.close();
//        db.close();
//
//        return count;
//    }
//
//    public int testForRecordaily() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor mCount = db.rawQuery("select numdaily from record where item = '0'", null);
//        mCount.moveToFirst();
//        int count = mCount.getInt(0);
//        mCount.close();
//        db.close();
//
//        return count;
//    }
//    public int testForRecorcontact() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor mCount = db.rawQuery("select numcontact from record where item = '0'", null);
//        mCount.moveToFirst();
//        int count = mCount.getInt(0);
//        mCount.close();
//        db.close();
//
//        return count;
//    }

    public List<Integer> getPreDailyRecord(){
        int count = testForRecor();

        SQLiteDatabase db = this.getReadableDatabase();


        Log.d(TAG, "count: " + count);
        String request = "select * from record where item = '" + count + "'";
        Cursor cursor = db.rawQuery(request, new String[]{});

        List<Integer> list = new ArrayList<Integer>();

        Log.d(TAG, "cursor.getCount: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                list.add(cursor.getInt(0));
                list.add(cursor.getInt(1));
                list.add(cursor.getInt(2));
                list.add(cursor.getInt(3));
                list.add(cursor.getInt(4));
                list.add(cursor.getInt(5));

                //Log.d(TAG, "num : " + i  + " " + cursor.getInt(i));
            }
        }

        Log.d(TAG, "list size: " + list.size());

        cursor.close();
        db.close();

        return list;
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

        Log.d(TAG, "daily upload locations: " + initialValues.toString());
        db.insert("upload", null, initialValues);

        db.close();

    }

    public void sendLocation (int num) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from upload where item = '" + num + "'", null);

        int userId = 0;
        long date = 0;
        double distance = 0.0;
        double range = 0.0;
        double duration = 0.0;

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                userId = cursor.getInt(1);
                date = cursor.getLong(2);
                distance = cursor.getDouble(3);
                range = cursor.getDouble(4);
                duration = cursor.getDouble(5);
            }
        }

        cursor.close();

        db.close();

        final int userIdDB = userId;
        final long dateDB = date;
        final double distanceDB = distance;
        final double rangeDB = range;
        final double durationDB = duration;

        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    databaseHelper = new DatabaseHelper();
                    databaseHelper.connectToDB_sendLocation(userIdDB, dateDB, distanceDB, rangeDB, durationDB);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);


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

    public int getNumDaily(int num){
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor mCount = db.rawQuery("select count(*) from app where item > '" + num + "'", null);
        Cursor mCount = db.rawQuery("select MAX(item) from upload", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        db.close();
        return count;
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

        Log.d(TAG, "locations: " + initialValues.toString());
        db.insert("locations", null, initialValues);
        db.close();
        int count = testForLocation();

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




//
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

        clearLocations(userId);

        return list;

    }

    public void clearLocations(int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM locations where userId = '" + userId + "'");
        db.close();
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

        Log.d(TAG, "recorlocations: " + initialValues.toString());
        db.insert("recordlocations", null, initialValues);

        db.close();

    }

    public void clearrecordLocations(int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM recorlocations where userId = '" + userId + "'");
        db.close();
    }

    public void sendRecordLoc(int num) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from recordlocations where item > '" + num + "'", null);

        int userId = 0;
        List<Integer> typeList = new ArrayList<Integer>();
        List<Long> timeList = new ArrayList<Long>();
        List<Double> durationList = new ArrayList<Double>();

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                userId = cursor.getInt(1);
                typeList.add(cursor.getInt(2));
                timeList.add(cursor.getLong(3));
                durationList.add(cursor.getDouble(4));
            }
        }

        cursor.close();
        db.close();

        final int userIdDB = userId;
        final List<Integer> typeListDB = typeList;
        final List<Long> timeListDB = timeList;
        final List<Double> durationListDB = durationList;

        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {

                    databaseHelper = new DatabaseHelper();
                    databaseHelper.connectToDB_insertRecord(userIdDB, typeListDB, timeListDB, durationListDB);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);

    }

    public int getNumRecord(int num){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCount = db.rawQuery("select count(*) from recordlocations where item > '" + num + "'", null);
        //Cursor mCount = db.rawQuery("select MAX(item) from recordlocations", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        db.close();
        return count;
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

        Log.d(TAG, "specialocations: " + initialValues.toString());
        db.insert("speclocations", null, initialValues);

        db.close();
        return id++;
    }



    public int testForSpecialLoc() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount = db.rawQuery("select count(*) from speclocations where id > '0'", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        return count;
    }

    public int checkType(int num) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount = db.rawQuery("select type from speclocations where id = '" + num + "'", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        db.close();

        return count;
    }

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
            }
        }

        Log.d(TAG, "read number of : " + list.size());

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

    public void clearsurvey(int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM survey where userId = '" + userId + "'");
        db.close();
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

    public int getNumSurvey(int num){
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor mCount = db.rawQuery("select count(*) from app where item > '" + num + "'", null);
        Cursor mCount = db.rawQuery("select MAX(item) from survey", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        db.close();
        return count;
    }


    //int id, long date, final String answers
    public void sendSurvey(int num){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from survey where item = '" + num + "'", null);

        int userId = 0;
        long date = 0;
        String answers = "";

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                userId = cursor.getInt(1);
                date = cursor.getLong(2);
                answers = cursor.getString(3);
            }
        }

        cursor.close();

        db.close();

        final int userIdDB = 1;
        final long dateDB = date;
        final String answersDB = answers;

        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {
                    databaseHelper = new DatabaseHelper();
                    databaseHelper.connectToDB_sendSurvey(userIdDB, dateDB, answersDB);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);

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

        //Log.d(TAG, "app: " + initialValues.toString());
        db.insert("app", null, initialValues);
        db.close();

        int count = testForApp();
        Log.d(TAG, "app insert number: " + count);
        return count;

    }

    public void clearapp(int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM app where userId = '" + userId + "'");
        db.close();
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

    public int getNumApp(int num){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCount = db.rawQuery("select count(*) from app where item > '" + num + "'", null);
        //Cursor mCount = db.rawQuery("select MAX(item) from recordlocations", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();

        db.close();
        return count;
    }

    public void sendApp(int num) {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d(TAG, "send app: " + num);
        Cursor cursor = db.rawQuery("select * from app where item > '" + num + "'", null);

        int userId = 0;
        List<String> appnameList = new ArrayList<String>();
        List<Long> timeList = new ArrayList<Long>();

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                userId = cursor.getInt(1);
                appnameList.add(cursor.getString(2));
                timeList.add(cursor.getLong(3));
            }
        }

        cursor.close();
        db.close();

        final int userIdDB = userId;
        final List<String> appnameListDB = appnameList;
        final List<Long> timeListDB = timeList;

        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {

                    databaseHelper = new DatabaseHelper();
                    databaseHelper.connectToDB_insertApp(userIdDB, appnameListDB, timeListDB);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);

    }


    /**************************************
     * contact
     *
     *  "create table contact (userId Integer, contrcdId Integer, contactName text, " +
     "contrcdDateTime text, contrcdType Integer, duration Integer, tag Integer)";
     ****************************************/
    public void insertContact(int userid, int id, String name, String time, int type, long duration, int tag){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put("userId", userid);
        initialValues.put("contrcdId", id);
        initialValues.put("contactName", name);
        initialValues.put("contrcdDateTime", time);
        initialValues.put("contrcdType", type);
        initialValues.put("duration", duration);
        initialValues.put("tag", tag);

        Log.d(TAG, "contact: " + initialValues.toString());
        db.insert("contact", null, initialValues);
        db.close();
    }

    public void sendContact(int num) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from contact where item > '" + num + "'", null);

        int userId = 0;
        List<Integer> contrcdId = new ArrayList<Integer>();
        List<String> contactName = new ArrayList<String>();
        List<String> contrcdDateTime = new ArrayList<String>();
        List<Integer> contrcdType = new ArrayList<Integer>();
        List<Long> duration = new ArrayList<Long>();
        List<Integer> tag = new ArrayList<Integer>();


        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                userId = cursor.getInt(1);
                contrcdId.add(cursor.getInt(2));
                contactName.add(cursor.getString(3));
                contrcdDateTime.add(cursor.getString(4));
                contrcdType.add(cursor.getInt(5));
                duration.add(cursor.getLong(6));
                tag.add(cursor.getInt(7));
            }
        }

        cursor.close();
        db.close();

        final int userIdDB = userId;
        final List<Integer> contrcdIdDB = contrcdId;
        final List<String> contactNameDB = contactName;
        final List<String> contrcdDateTimeDB = contrcdDateTime;
        final List<Integer> contrcdTypeDB =contrcdType;
        final List<Long> durationDB = duration;
        final List<Integer> tagDB = tag;

        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                try {

                    databaseHelper = new DatabaseHelper();
                    databaseHelper.connectToDB_insertContact(userIdDB, contrcdIdDB, contactNameDB,
                            contrcdDateTimeDB,contrcdTypeDB,durationDB,tagDB);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(1);
    }

    public int getNumContact(int num){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCount = db.rawQuery("select count(*) from contact where item > '" + num + "'", null);
        //Cursor mCount = db.rawQuery("select MAX(item) from recordlocations", null);
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
