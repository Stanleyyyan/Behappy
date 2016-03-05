package com.stanley.myapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.stanley.myapplication.Locations.LocationDaily;
import com.stanley.myapplication.Locations.MySQLiteLocHelper;

import java.util.Date;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver{
    private static final String TAG = "AlarmReceiver";

    MySQLiteLocHelper mySQLiteLocHelper;
    private int userId = 1;

    private double distanceAll;
    private double rangeAll;
    private double durationAll;
    private double durationAllForSpec;

    @Override
    public void onReceive(Context context, Intent intent) {
        distanceAll = 0;
        rangeAll = 0;
        durationAll = 0;
        durationAllForSpec = 0;

        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

        mySQLiteLocHelper = new MySQLiteLocHelper(context);

        //get the data 1 and 2
        List<LocationDaily> list =  mySQLiteLocHelper.getInfoLoc(userId);

        Log.d(TAG, "size of list: " + list.size());

        //sum 1 and 2 separately
        int size = list.size();


        for (int i = 0; i < size; i++) {
            LocationDaily locationDaily = list.get(i);
            if (locationDaily.getType()==0){
                distanceAll = distanceAll + locationDaily.getDistance();
                rangeAll = rangeAll + locationDaily.getRange();
                durationAll = durationAll + locationDaily.getDuration();
            }

            if (locationDaily.getType() == 1) {
                durationAllForSpec = durationAllForSpec + locationDaily.getDurationSpec();
            }
        }

        Log.d(TAG, "0: " + distanceAll + " " + rangeAll + " " + durationAll);
        Log.d(TAG, "1: " + durationAllForSpec);

        LocationDaily locationDaily = new LocationDaily();
        locationDaily.setDistance(distanceAll);
        locationDaily.setRange(rangeAll);
        locationDaily.setDuration(durationAll);
        locationDaily.setDurationSpec(durationAllForSpec);

        //put it back in one table
        mySQLiteLocHelper.insertDailyUpload(userId, new Date().getTime(), locationDaily);

    }
}
