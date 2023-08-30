package com.example.myapplication.unit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/1/7/007.
 */

public class DB {

    private static MySQLITEhelper mySQLITEhelper;

    public static MySQLITEhelper getInstance(Context context) {
        if (mySQLITEhelper == null) {
            mySQLITEhelper = new MySQLITEhelper(context);
        }
        return mySQLITEhelper;
    }


    public static void setExcele(SQLiteDatabase db, String sql) {
        if (db != null) {
            if (sql != null && !"".equals(sql)) {
                db.execSQL(sql);
            }
        }
    }
}
