package com.stanley.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.stanley.myapplication.Locations.LocationDaily;
import com.stanley.myapplication.Locations.MySQLiteLocHelper;

/**
 * Created by OhIris on 3/4/16.
 */
public class RepeatedAlarmReceiver extends BroadcastReceiver {
    private LocationDaily locationDaily;
    private MySQLiteLocHelper mySQLiteLocHelper;
    private DatabaseHelper databaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        mySQLiteLocHelper = new MySQLiteLocHelper(context);
        locationDaily = mySQLiteLocHelper.getDailyUpload();

        databaseHelper = new DatabaseHelper();




    }
}
