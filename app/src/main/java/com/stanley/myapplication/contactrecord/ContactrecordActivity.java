package com.stanley.myapplication.contactrecord;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;

import com.stanley.myapplication.MySQLiteHelper;
import com.stanley.myapplication.MySQLiteLocHelper;
import com.stanley.myapplication.contactlist.ContactActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XieDugu on 2016/2/13.
 */
public class ContactrecordActivity extends Activity {
    private AsyncQueryHandler asyncQuery;
    private List<ContactRecord> callLogs;

    private MySQLiteLocHelper mySQLiteLocHelper;
    public MySQLiteHelper myHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncQuery = new MyAsyncQueryHandler(getContentResolver());
        init();
        finish();
    }

    private void init() {
        Uri uri = android.provider.CallLog.Calls.CONTENT_URI;
        // 查询的列
        String[] projection = {
                CallLog.Calls._ID, // id
                CallLog.Calls.CACHED_NAME, // 名字
                CallLog.Calls.NUMBER, // 号码
                CallLog.Calls.DATE, // 日期
                CallLog.Calls.TYPE, // 类型
                CallLog.Calls.DURATION // 通话时长
        };
        asyncQuery.startQuery(0, null, uri, projection, null, null,
                CallLog.Calls.DEFAULT_SORT_ORDER);
    }

    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                callLogs = new ArrayList<ContactRecord>();
                SimpleDateFormat sfd = new SimpleDateFormat("MM-dd hh:mm");
                Date date;
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    date = new Date(cursor.getLong(cursor
                            .getColumnIndex(CallLog.Calls.DATE)));
                    int type = cursor.getInt(cursor
                            .getColumnIndex(CallLog.Calls.TYPE));
                    String cachedName = cursor.getString(cursor
                            .getColumnIndex(CallLog.Calls.CACHED_NAME));// 缓存的名称，如存在
                    String number = cursor.getString(cursor
                            .getColumnIndex(CallLog.Calls.NUMBER));
                    int id = cursor.getInt(cursor
                            .getColumnIndex(CallLog.Calls._ID));
                    long duration = cursor.getLong(cursor
                            .getColumnIndex(CallLog.Calls.DURATION));
                    ContactRecord contrcd = new ContactRecord();
                    contrcd.setContrcdId(id);
                    contrcd.setContactName(cachedName);
                    contrcd.setContrcdDateTime(sfd.format(date));
                    contrcd.setContrcdType(type);
                    contrcd.setDuration(duration);
                    if (null == cachedName || "".equals(cachedName)) {
                        contrcd.setContactName(number);
                    }
                    callLogs.add(contrcd);
                }
                if (callLogs.size() > 0) {
                    myHelper = new MySQLiteHelper(ContactrecordActivity.this, "my.db", null, 1);
                    SQLiteDatabase dbr = myHelper.getReadableDatabase();
                    Cursor dbcursor;
                    int categoryTag = 0;
                    for(int i=0;i<callLogs.size();i++){
                        categoryTag = 0;
                        ContactRecord each = callLogs.get(i);
                        String tempname = each.getContactName();
                        dbcursor = dbr.query("ContactList", new String[]{"categoryTag"}, "contactName=?", new String[]{tempname}, null, null, null);
                        //System.out.println(dbcursor.moveToFirst());

                        if(dbcursor.moveToFirst()){
                            categoryTag = dbcursor.getInt(0);
                            //System.out.println("dbcategory: "+categoryTag);
                        }



//                        System.out.println("id: "+each.getContrcdId()
//                                +"\nname: "+each.getContactName()
//                                +"\ndate: "+each.getContrcdDateTime()
//                                +"\ntype: "+each.getContrcdType()
//                                +each.getDuration()
//                                +"\ncategory: "+categoryTag);

                        mySQLiteLocHelper = new MySQLiteLocHelper(ContactrecordActivity.this);
                        mySQLiteLocHelper.insertContact(1, each.getContrcdId(), each.getContactName(), each.getContrcdDateTime(),
                                each.getContrcdType(), each.getDuration(), categoryTag);


                    }
                }
            }
            super.onQueryComplete(token, cookie, cursor);
        }
    }


}
