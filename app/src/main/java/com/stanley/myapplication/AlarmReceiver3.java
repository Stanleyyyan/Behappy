package com.stanley.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by OhIris on 3/5/16.
 */
public class AlarmReceiver3 extends BroadcastReceiver {
    MySQLiteLocHelper mySQLiteLocHelper;
    DatabaseHelper databaseHelper;


    @Override
    public void onReceive(Context context, Intent intent) {
        mySQLiteLocHelper = new MySQLiteLocHelper(context);

        List<Integer> pre = mySQLiteLocHelper.getPreDailyRecord();

        //get previous num
        int item = 0;
        int numRecord_pre = 0;
        int numApp_pre = 0;
        int numContact_pre = 0;
        int numSurvey_pre = 0;
        int numDaily_pre = 0;

        if (pre.get(0) != null) {
            numRecord_pre = pre.get(1);
            numApp_pre = pre.get(2);
            numContact_pre = pre.get(3);
            numSurvey_pre = pre.get(4);
            numDaily_pre = pre.get(5);
        }


        mySQLiteLocHelper.sendSurvey(numSurvey_pre);
        mySQLiteLocHelper.sendRecordLoc(numRecord_pre);
        mySQLiteLocHelper.sendLocation(numDaily_pre);
        mySQLiteLocHelper.sendApp(numApp_pre);
        mySQLiteLocHelper.sendContact(numContact_pre);

    }
}
