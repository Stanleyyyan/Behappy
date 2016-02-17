package com.stanley.myapplication;

import android.annotation.TargetApi;
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

    private static final String TAG = "AppUsage";
    UsageStatsManager mUsageStatsManager;
    private TextView statistics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage);
        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        statistics = (TextView) findViewById(R.id.tv_app_usage);
        List<UsageStats> usageStatsList = getUsageStatistics(4);
        updateAppsList(usageStatsList);
    }

    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        long start = System.currentTimeMillis() - (long)600000;
        long end = System.currentTimeMillis();

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(UsageStatsManager.INTERVAL_BEST, start, end);

        if (queryUsageStats.size() == 0) {
            Log.i(TAG, "The user may not allow the access to apps usage. ");
            Toast.makeText(this, "fail",
                    Toast.LENGTH_LONG).show();
        }
        return queryUsageStats;
    }

    void updateAppsList(List<UsageStats> usageStatsList) {
        if (usageStatsList == null) {
            return;
        }
        String s = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        for (int i = 0; i < usageStatsList.size(); i++) {
            Date date = new Date(usageStatsList.get(i).getLastTimeUsed());
            s += usageStatsList.get(i).getPackageName() + String.valueOf(sdf.format(date)) + "\t";
        }
        statistics.setText(s);
    }
}
