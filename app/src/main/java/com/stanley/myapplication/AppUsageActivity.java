package com.stanley.myapplication;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AppUsageActivity extends AppCompatActivity {

//    private static final String TAG = "AppUsage";
//    UsageStatsManager mUsageStatsManager;
//    private TextView statistics;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_app_usage);
//        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
//        statistics = (TextView) findViewById(R.id.tv_app_usage);
//        UsageEvents events = getUsageStatistics();
//        updateAppsList(events);
//    }
//
//    public UsageEvents getUsageStatistics() {
//        // Get the app statistics since one year ago from the current time.
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.YEAR, -1);
//
//        long start = System.currentTimeMillis() - (long)1200000;
//        long end = System.currentTimeMillis();
//
//        UsageEvents events = mUsageStatsManager.queryEvents(start, end);
//
//        if (!events.hasNextEvent()) {
//            Log.i(TAG, "The user may not allow the access to apps usage. ");
//            Toast.makeText(this, "fail",
//                    Toast.LENGTH_LONG).show();
//        }
//        return events;
//    }
//
//    void updateAppsList(UsageEvents events) {
//        if (!events.hasNextEvent()) {
//            return;
//        }
//        String s = "";
//        UsageEvents.Event eventOut = new UsageEvents.Event();
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
//        while (events.hasNextEvent()) {
//            events.getNextEvent(eventOut);
//            Date date = new Date(eventOut.getTimeStamp());
//            s += eventOut.getPackageName() + ": " + String.valueOf(sdf.format(date)) + "\t";
//            if (eventOut.getEventType() == 7) {
//                //Date date = new Date(eventOut.getTimeStamp());
//                //s += eventOut.getPackageName() + String.valueOf(sdf.format(date)) + "\t";
//            }
//        }
//        statistics.setText(s);
//    }
}
