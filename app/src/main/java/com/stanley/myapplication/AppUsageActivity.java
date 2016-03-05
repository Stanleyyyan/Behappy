package com.stanley.myapplication;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppUsageActivity extends AppCompatActivity {

    private static final String TAG = "AppUsage";
    UsageStatsManager mUsageStatsManager;
    private TextView statistics;

    private MySQLiteLocHelper mySQLiteLocHelper;
    private int userId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage);
        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        statistics = (TextView) findViewById(R.id.tv_app_usage);
        UsageEvents events = getUsageStatistics();
        updateAppsList(events);
        finish();
    }

    public UsageEvents getUsageStatistics() {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        long start = System.currentTimeMillis() - (long)86400000;
        long end = System.currentTimeMillis();

        UsageEvents events = mUsageStatsManager.queryEvents(start, end);

        if (!events.hasNextEvent()) {
            Log.i(TAG, "The user may not allow the access to apps usage. ");
            Toast.makeText(this, "fail",
                    Toast.LENGTH_LONG).show();
        }
        return events;
    }

    void updateAppsList(UsageEvents events) {
        if (!events.hasNextEvent()) {
            return;
        }
        String s = "";
        UsageEvents.Event eventOut = new UsageEvents.Event();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        while (events.hasNextEvent()) {
            events.getNextEvent(eventOut);
            Date date = new Date(eventOut.getTimeStamp());
            s += eventOut.getPackageName() + ": " + String.valueOf(sdf.format(date)) + "\t";

            mySQLiteLocHelper = new MySQLiteLocHelper(AppUsageActivity.this);
            mySQLiteLocHelper.appInsert(userId, eventOut.getPackageName(), eventOut.getTimeStamp());



        }
        statistics.setText(s);
    }
}
