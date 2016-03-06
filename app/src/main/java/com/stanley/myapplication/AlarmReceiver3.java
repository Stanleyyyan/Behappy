package com.stanley.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by OhIris on 3/5/16.
 */
public class AlarmReceiver3 extends BroadcastReceiver {
    MySQLiteLocHelper mySQLiteLocHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        mySQLiteLocHelper = new MySQLiteLocHelper(context);

    }
}
