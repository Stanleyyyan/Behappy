package com.stanley.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.androidadvance.androidsurvey.SurveyActivity;
import com.stanley.myapplication.Locations.LocationDaily;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    MySQLiteLocHelper mySQLiteLocHelper;
    private int userId = 1;

    private double distanceAll;
    private double rangeAll;
    private double durationAll;

    @Override
    public void onReceive(Context context, Intent intent) {
        distanceAll = 0;
        rangeAll = 0;
        durationAll = 0;

        Toast.makeText(context, "I'm running 1", Toast.LENGTH_SHORT).show();

        mySQLiteLocHelper = new MySQLiteLocHelper(context);

        //get the data 1 and 2
        List<LocationDaily> list = mySQLiteLocHelper.getInfoLoc(userId);

        Log.d(TAG, "size of list: " + list.size());

        //sum 1 and 2 separately
        int size = list.size();


        for (int i = 0; i < size; i++) {
            LocationDaily locationDaily = list.get(i);
            distanceAll = distanceAll + locationDaily.getDistance();
            rangeAll = rangeAll + locationDaily.getRange();
            durationAll = durationAll + locationDaily.getDuration();

        }

        Log.d(TAG, "result after adding all " + distanceAll + " " + rangeAll + " " + durationAll);

        LocationDaily locationDaily = new LocationDaily();
        locationDaily.setUserId(userId);
        locationDaily.setDate(new Date().getTime());
        locationDaily.setDistance(192754);
        locationDaily.setRange(650);
        locationDaily.setDuration(2345812);

        //put it back in one table
        mySQLiteLocHelper.insertDailyUpload(locationDaily);

        //save app usage daily
        Intent i = new Intent();
        i.setClassName("com.stanley.myapplication", "com.stanley.myapplication.AppUsageActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        //save contact
        Intent i2 = new Intent();
        i2.setClassName("com.stanley.myapplication", "com.stanley.myapplication.contactrecord.ContactrecordActivity");
        i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i2);


    }


}
