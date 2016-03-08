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

    private final static String TABLE_NAME_SURVEY = "Behappy_survey";
    private final static String TABLE_NAME_LOCATION_UPLOAD = "Behappy_location_upload";
    private final static String TABLE_NAME_RECORD_LOCATION = "Behappy_record_location";
    private final static String TABLE_NAME_APP = "Behappy_app";
    private final static String TABLE_NAME_CONTACT = "Behappy_contact";


    public DatabaseHelper() {

        connectToServer(3306);
    }


    private static void connectToServer(int portNum) {

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
            session.setPortForwardingL(portNum, "127.0.0.1", portNum);

            Log.d(TAG, "session Connected");

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void connectToDB_sendSurvey(int userId, long date, String answers) {

        //connectToServer(3306);
        Connection conn;

        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            String s = "INSERT INTO " + TABLE_NAME_SURVEY + " VALUES(?,?,?)";

            conn = (Connection) DriverManager.getConnection(url, username, password);

            if (conn != null) {
                PreparedStatement tem = conn.prepareStatement(s);

                //things need to input
                tem.setInt(1, userId);
                tem.setLong(2, date);
                tem.setString(3, answers);
                Log.d(TAG, "insert survey success");

                tem.executeUpdate();

                tem.close();
                conn.close();

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void connectToDB_insertApp(int userId, List<String> appNameList, List<Long> timeList) {

        //connectToServer(3306);
        Connection conn;

        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            String s = "INSERT INTO " + TABLE_NAME_APP + " VALUES(?,?,?)";

            conn = (Connection) DriverManager.getConnection(url, username, password);
            Log.d(TAG, "app size: " + appNameList.size());

            for (int i = 0; i < appNameList.size(); i++) {
                if (conn != null) {
                    PreparedStatement tem = conn.prepareStatement(s);
                    Log.d(TAG, "insert app ...");
                    //things need to input
                    tem.setInt(1, userId);
                    tem.setString(2, appNameList.get(i));
                    tem.setLong(3, timeList.get(i));
                    Log.d(TAG, "insert app success");

                    tem.executeUpdate();

                    tem.close();
                }
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public void connectToDB_sendLocation(int userId, long date, double distance, double range, double duration) {

        //connectToServer(3306);
        Connection conn;

        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            String s = "INSERT INTO " + TABLE_NAME_LOCATION_UPLOAD + " VALUES(?,?,?,?,?)";

            conn = (Connection) DriverManager.getConnection(url, username, password);

            if (conn != null) {
                PreparedStatement tem = conn.prepareStatement(s);

                //things need to input
                tem.setInt(1, userId);
                tem.setLong(2, date);
                tem.setDouble(3, distance);
                tem.setDouble(4, range);
                tem.setDouble(5, duration);
                Log.d(TAG, "insert locations success");

                tem.executeUpdate();

                tem.close();
                conn.close();

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void connectToDB_insertRecord(int userId, List<Integer> typeList, List<Long> timeList, List<Double> durationList) {

        //connectToServer(3306);
        Connection conn;

        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            String s = "INSERT INTO " + TABLE_NAME_RECORD_LOCATION + " VALUES(?,?,?,?)";

            conn = (Connection) DriverManager.getConnection(url, username, password);

            for (int i = 0; i < typeList.size(); i++) {
                if (conn != null) {
                    PreparedStatement tem = conn.prepareStatement(s);

                    //things need to input
                    tem.setInt(1, userId);
                    tem.setInt(2, typeList.get(i));
                    tem.setLong(3, timeList.get(i));
                    tem.setDouble(4, durationList.get(i));

                    Log.d(TAG, "insert record success");

                    tem.executeUpdate();

                    tem.close();

                }
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /*
    * final int userIdDB = userId;
        final List<Integer> contrcdIdDB = contrcdId;
        final List<String> contactNameDB = contactName;
        final List<String> contrcdDateTimeDB = contrcdDateTime;
        final List<Integer> contrcdTypeDB =contrcdType;
        final List<Long> durationDB = duration;
        final List<Integer> tagDB = tag;
    * **/

    public void connectToDB_insertContact(int userId, List<Integer> contrcdId, List<String> contactName,
                                          List<String> contrcdDateTime, List<Integer> contrcdType,List<Long> duration, List<Integer> tag) {


        //connectToServer(3306);
        Connection conn;

        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            String s = "INSERT INTO " + TABLE_NAME_CONTACT + " VALUES(?,?,?,?,?,?,?)";

            conn = (Connection) DriverManager.getConnection(url, username, password);

            for (int i = 0; i < contrcdId.size(); i++) {
                if (conn != null) {
                    PreparedStatement tem = conn.prepareStatement(s);

                    //things need to input
                    tem.setInt(1, userId);
                    tem.setInt(2, contrcdId.get(i));
                    tem.setString(3, contactName.get(i));
                    tem.setString(4, contrcdDateTime.get(i));
                    tem.setInt(5, contrcdType.get(i));
                    tem.setLong(6, duration.get(i));
                    tem.setInt(7, tag.get(i));

                    Log.d(TAG, "insert contact success");

                    tem.executeUpdate();

                    tem.close();


                }
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

//    public void connectToDB_insertLocation(int userId, String name, long time){
//        //connectToServer();
//        Connection conn;
//
//        try {
//            String driverName = "com.mysql.jdbc.Driver";
//            Class.forName(driverName);
//            String s = "INSERT INTO " + TABLE_NAME_APP + " VALUES(?,?,?)";
//
//            conn = (Connection) DriverManager.getConnection(url, username, password);
//
//            if (conn != null) {
//                PreparedStatement tem = conn.prepareStatement(s);
//
//                //things need to input
//                tem.setInt(1, userId);
//                tem.setString(2, name);
//                tem.setLong(3, time);
//                Log.d(TAG, "insert user success");
//
//                tem.executeUpdate();
//
//                tem.close();
//                conn.close();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//    }
//
//    public void connectToDB_insertAnswers(int id, long date, final String answers){
//        //connectToServer();
//        Connection conn;
//
//        try {
//            String driverName = "com.mysql.jdbc.Driver";
//            Class.forName(driverName);
//            String s = "INSERT INTO " + TABLE_NAME_SURVEY + " VALUES(?,?,?)";
//
//            conn = (Connection) DriverManager.getConnection(url, username, password);
//
//            if (conn != null) {
//                PreparedStatement tem = conn.prepareStatement(s);
//
//                //things need to input
//                tem.setInt(1, id);
//                tem.setLong(2, date);
//                tem.setString(3, answers);
//                Log.d(TAG, "insert user success");
//
//                tem.executeUpdate();
//
//                tem.close();
//                conn.close();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//    }
}
