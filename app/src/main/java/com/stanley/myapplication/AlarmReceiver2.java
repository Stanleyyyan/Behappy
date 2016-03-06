package com.stanley.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by OhIris on 3/5/16.
 */
public class AlarmReceiver2 extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver2";

    MySQLiteLocHelper mySQLiteLocHelper;
    private int userId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "I'm running 2", Toast.LENGTH_SHORT).show();

        mySQLiteLocHelper = new MySQLiteLocHelper(context);

        //get previous num
        int item = 0;
        int numRecord_pre = 0;
        int numApp_pre = 0;
        int numContact_pre = 0;
        int numSurvey_pre = 0;
        int numDaily_pre = 0;

        List<Integer> pre = mySQLiteLocHelper.getPreDailyRecord();

        if (pre.get(0) != null) {
            numRecord_pre = pre.get(1);
            numApp_pre = pre.get(2);
            numContact_pre = pre.get(3);
            numSurvey_pre = pre.get(4);
            numDaily_pre = pre.get(5);
        }

        //get all numbers
        int numRecord = mySQLiteLocHelper.getNumRecord(numRecord_pre);
        int numApp = mySQLiteLocHelper.getNumApp(numApp_pre);
        int numContact = mySQLiteLocHelper.getNumContact(numContact_pre);
        int numSurvey = mySQLiteLocHelper.getNumSurvey(numSurvey_pre);
        int numDaily = mySQLiteLocHelper.getNumDaily(numDaily_pre);

        //save in a special table
        mySQLiteLocHelper.insertDailyRecord(numRecord, numApp, numContact, numSurvey,numDaily);
        Log.d(TAG, "" + numRecord + " " + numApp + " " + numContact + " " + numSurvey + " " + numDaily);

    }


}
