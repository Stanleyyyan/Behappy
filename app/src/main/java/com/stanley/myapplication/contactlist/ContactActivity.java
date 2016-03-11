package com.stanley.myapplication.contactlist;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.stanley.myapplication.Main2Activity;
import com.stanley.myapplication.MySQLiteHelper;
import com.stanley.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by XieDugu on 2016/2/12.
 */
public class ContactActivity extends Activity{
    private ContactAdapter adapter;
    private ListView ContactListView;
    private List<ContactList> list;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
    private int userId;

    private Button btn_save;

    private HashMap<String, ContactList> contactIdMap = null;

    public static MySQLiteHelper myHelper;
    public SQLiteDatabase dbw, dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contact_manage);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            userId = 1;

        } else {
            userId = extras.getInt("userId");
        }


        myHelper = new MySQLiteHelper(this, "my.db", null, 1);
        dbw = myHelper.getWritableDatabase();
        dbr = myHelper.getReadableDatabase();
        ContactListView = (ListView) findViewById(R.id.ContactListView);
        // 实例化

        asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
        init();

        btn_save = (Button) findViewById(R.id.btn_save_contact);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ContactActivity.this, Main2Activity.class);
                intent.putExtra("hint", false);
                intent.putExtra("userId", userId);
                startActivity(intent);

            }
        });
    }

    //---------------初始化数据库查询参数--------------//
    private void init() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
        // 按照sort_key升序查詢
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");
    }

    private class MyAsyncQueryHandler extends AsyncQueryHandler {
        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                contactIdMap = new HashMap<String, ContactList>();
                list = new ArrayList<ContactList>();
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0, contactId = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    if (contactIdMap.containsKey(name)) {// 无操作
                    } else {
                        // 创建联系人对象
                        ContactList contact = new ContactList();
                        contact.setContactName(name);
                        contact.setContactId(contactId);
                        contactId++;
                        list.add(contact);
                        contactIdMap.put(name, contact);
                    }
                }
                if (list.size() > 0) {
                    setAdapter(list);
                }
            }

            super.onQueryComplete(token, cookie, cursor);
        }
    }

    private void setAdapter(List<ContactList> list) {


        //--create table ContactList
        dbw.execSQL("create table if not exists ContactList("
                + "ContactId integer,"
                + "contactName varchar primary key,"
                + "categoryTag integer)");

        Boolean ifExist = false;
        Cursor cursor = dbr.query("ContactList", null, null, null, null, null, "contactName asc");
        int nameIndex = cursor.getColumnIndex("contactName");
        int tagIndex = cursor.getColumnIndex("categoryTag");
        //--check if ContactList table is already exist
        if(cursor.moveToFirst()){
            System.out.println("update");
            //--exist, insert not recorded
            for (int i = 0; i < list.size(); i++) {
                ContactList each = list.get(i);
                String contactName = each.getContactName();
                //System.out.println("contactName: "+ contactName);
                for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                    String tempname = cursor.getString(nameIndex);
                    if(tempname.toString().equals(contactName)){
                        each.setCategoryTag(cursor.getInt(tagIndex));
                        list.set(i, each);
                        ifExist = true;break;
                    }
                }
                if(!ifExist){
                    dbw.execSQL("insert into ContactList(ContactId, contactName, categoryTag) values("
                            + each.getContactId() + ",'"+each.getContactName() + "'," + each.getCategoryTag() + ")");
                }
                //System.out.println("ifExist: "+ ifExist);
                ifExist = false;
            }
        }else{
            //--input all
            System.out.println("input all");
            for (int i = 0; i < list.size(); i++){
                ContactList each = list.get(i);
                dbw.execSQL("insert into ContactList(ContactId, contactName, categoryTag) values("
                        + each.getContactId() + ",'"+each.getContactName() + "'," + each.getCategoryTag() + ")");
            }
        }
        cursor.close();
        /*--test if input successfully--
        Cursor cursortest = dbr.query("ContactList", null, null, null, null, null, "contactName asc");
        int idIndex = cursortest.getColumnIndex("ContactId");
        nameIndex = cursortest.getColumnIndex("contactName");
        int tagIndex = cursortest.getColumnIndex("categoryTag");
        String tempname;
        int tempid, temptag;
        for (cursortest.moveToFirst(); !(cursortest.isAfterLast()); cursortest.moveToNext()) {
            tempid = cursortest.getInt(idIndex);
            tempname = cursortest.getString(nameIndex);
            temptag = cursortest.getInt(tagIndex);
            System.out.println("contactId: " + tempid + "\nname: " + tempname + "\nCategoryTag: " + temptag + "\n");
        }
        cursortest.close();
        //--test end--*/
        adapter = new ContactAdapter(this, list);
        ContactListView.setAdapter(adapter);
    }

}
