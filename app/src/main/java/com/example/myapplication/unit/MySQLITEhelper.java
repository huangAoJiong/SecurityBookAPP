package com.example.myapplication.unit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/1/7/007.
 */

public class MySQLITEhelper extends SQLiteOpenHelper {
    private static final String TAG = "MySQLITEhelper";

    public MySQLITEhelper(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSON);
    }

    public MySQLITEhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate: ____________________");
        String sql = "create table " + Constant.TABLE_NAME + "(" + Constant._ID +
                " Integer primary key," + Constant.NAME + " varchar(10)," +
                Constant.AGE + " Integer)";
        db.execSQL(sql);

        /*   String sql = "create table " + Constant.TABLE_NAME + "(" + Constant._ID +
                " Integer primary key," + Constant.NAME + " varchar(10)," +
                Constant.AGE + " Integer)";*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "onUpgrade: ************************");

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.e(TAG, "onOpen: %%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }


}
