package com.stanley.myapplication.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.stanley.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by XieDugu on 2016/2/12.
 */
public class ContactAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ContactList> list;
    private HashMap<String, Integer> alphaIndexer; // 字母索引
    private String[] sections; // 存储每个章节
    private Context ctx; // 上下文

    public ContactAdapter(Context context, List<ContactList> list) {
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.alphaIndexer = new HashMap<String, Integer>();
        this.sections = new String[list.size()];


        Set<String> sectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(sectionList); // 根据首字母进行排序
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.contactName);
            holder.category = (Spinner) convertView.findViewById(R.id.categoryTag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactList contact = list.get(position);
        final String name = contact.getContactName();
        int category = contact.getCategoryTag();
        holder.name.setText(name);
        holder.category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                System.out.println(name);
                SQLiteDatabase db = ContactActivity.myHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("contactName",name);
                contentValues.put("categoryTag",arg2);
                db.update("ContactList", contentValues, "contactName=?", new String[]{name});
                /*--test update
                SQLiteDatabase dbr = ContactActivity.myHelper.getReadableDatabase();
                Cursor cursor = dbr.query("ContactList", new String[]{"contactId","contactName","categoryTag"},"contactName=?", new String[]{name}, null, null, null);
                if(cursor.moveToFirst()) {
                    for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                        //int tempid = cursor.getInt(cursor.getColumnIndex("contactId"));
                        String tempname = cursor.getString(cursor.getColumnIndex("contactName"));
                        int temptag = cursor.getInt(cursor.getColumnIndex("categoryTag"));
                        System.out.println("name: " + tempname + "\nCategoryTag: " + temptag + "\n");
                    }
                }
                //--test end--*/
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        Spinner category;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {
        list.remove(position);
    }
}
