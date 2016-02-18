package com.stanley.myapplication.contactrecord;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncQuery = new MyAsyncQueryHandler(getContentResolver());
        init();
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
                    if (null == cachedName || "".equals(cachedName)) {
                        contrcd.setContactName(number);
                    }
                    callLogs.add(contrcd);
                }
                if (callLogs.size() > 0) {
                    for(int i=0;i<callLogs.size();i++){
                        ContactRecord each = callLogs.get(i);
                        System.out.println("id: "+each.getContrcdId()
                        +"\nname: "+each.getContactName()
                        +"\ndate: "+each.getContrcdDateTime()
                        +"\ntype: "+each.getContrcdType()
                        +"\n");
                    }
                }
            }
            super.onQueryComplete(token, cookie, cursor);
        }
    }

}
