package com.stanley.myapplication;

/**
 * Created by XieDugu on 2016/2/9.
 */
import android.content.Context;	//ÒýÈëÏà¹ØÀà
import android.database.sqlite.SQLiteDatabase;	//ÒýÈëÏà¹ØÀà
import android.database.sqlite.SQLiteOpenHelper;	//ÒýÈëÏà¹ØÀà
import android.database.sqlite.SQLiteDatabase.CursorFactory;	//ÒýÈëÏà¹ØÀà

public class MySQLiteHelper extends SQLiteOpenHelper{
    public MySQLiteHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);		//µ÷ÓÃ¸¸ÀàµÄ¹¹ÔìÆ÷
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    @Override
    public void onOpen(SQLiteDatabase db){}
}
