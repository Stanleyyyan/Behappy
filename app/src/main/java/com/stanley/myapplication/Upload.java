package com.stanley.myapplication;


import android.content.Context;
import android.util.Log;

import java.util.List;

public class Upload {
    private static final String TAG = "Upload";
    MySQLiteLocHelper mySQLiteLocHelper;
    DatabaseHelper databaseHelper;

    private int type;
    private Context context;

    public Upload(int type, Context context){
        this.type = type;
        this.context = context;

    }


    public void doUpload(){
        mySQLiteLocHelper = new MySQLiteLocHelper(this.context);

        List<Integer> pre = mySQLiteLocHelper.getPreDailyRecord();

        //get previous num
        int item = 0;
        int numRecord_pre = 0;
        int numApp_pre = 0;
        int numContact_pre = 0;
        int numSurvey_pre = 0;
        int numDaily_pre = 0;

//        if (pre.get(0) != null) {
//            numRecord_pre = pre.get(1);
//            numApp_pre = pre.get(2);
//            Log.d(TAG, "app num: " + numApp_pre);
//            numContact_pre = pre.get(3);
//            numSurvey_pre = pre.get(4);
//            numDaily_pre = pre.get(5);
//        }

        switch (this.type){
            case 1:
                mySQLiteLocHelper.sendApp(numApp_pre);

                break;

            case 2:
                mySQLiteLocHelper.sendLocation(numDaily_pre);
                mySQLiteLocHelper.sendRecordLoc(numRecord_pre);
                break;

            case 3:
                mySQLiteLocHelper.sendSurvey(numSurvey_pre);
                mySQLiteLocHelper.sendContact(numContact_pre);
                break;

        }






    }
}
