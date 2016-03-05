package com.stanley.myapplication;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DatabaseHelper {

    private static final String TAG = "DatabaseHelper";

    //here
    private static String url = "jdbc:mysql://127.0.0.1:3306/mhealthplay";
    private static String username = "mhealth";
    private static String password = "mhealth";

    private final static String TABLE_NAME_APP= "loneliness_app_usage";
    private final static String TABLE_NAME_SURVEY = "loneliness_survey_answers";
    private final static String TABLE_NAME_DETAILS = "myroutesdetails";


    public DatabaseHelper() {

    }


    private static void connectToServer() {

        //here
        String user = "zwd753";
        String password = "Tsubasa530!";
        String host = "murphy.wot.eecs.northwestern.edu";


        int port = 22;

        try {
            Log.d(TAG, "connect to server");
            JSch jsch = new JSch();

            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);

            Properties p = new Properties();
            p.put("StrictHostKeyChecking", "no");
            session.setConfig(p);

            session.connect();
            session.setPortForwardingL(3306, "127.0.0.1", 3306);

            Log.d(TAG, "session Connected");

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void connectToDB_insertApp(int userId, String name, long time){
        //connectToServer();
        Connection conn;

        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            String s = "INSERT INTO " + TABLE_NAME_APP + " VALUES(?,?,?)";

            conn = (Connection) DriverManager.getConnection(url, username, password);

            if (conn != null) {
                PreparedStatement tem = conn.prepareStatement(s);

                //things need to input
                tem.setInt(1, userId);
                tem.setString(2, name);
                tem.setLong(3, time);
                Log.d(TAG, "insert user success");

                tem.executeUpdate();

                tem.close();
                conn.close();

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void connectToDB_insertAnswers(int id, long date, final String answers){
        //connectToServer();
        Connection conn;

        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            String s = "INSERT INTO " + TABLE_NAME_SURVEY + " VALUES(?,?,?)";

            conn = (Connection) DriverManager.getConnection(url, username, password);

            if (conn != null) {
                PreparedStatement tem = conn.prepareStatement(s);

                //things need to input
                tem.setInt(1, id);
                tem.setLong(2, date);
                tem.setString(3, answers);
                Log.d(TAG, "insert user success");

                tem.executeUpdate();

                tem.close();
                conn.close();

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
