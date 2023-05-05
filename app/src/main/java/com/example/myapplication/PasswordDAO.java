package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PasswordDAO {
    private SQLiteDatabase db;
    public static int mainId;
    public static String mainTitle;
    public static String mainUser;
    public static String mainPwd;
    public static String mainNote;
    public static String mainDate;
    public ArrayList<AppInfo> sql_dataList  = new ArrayList<>();;
    private Context tmpcontext;
    public PasswordDAO(Context context) {
        this.tmpcontext=context;
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public String addPassword(Password password) {
//        db = DBHelper.getWritableDatabase();
        String results="ok!";
        try {
            ContentValues values = new ContentValues();
            values.put("title", password.getTitle());
            values.put("username", password.getUsername());
            values.put("password", password.getPassword());
            values.put("note", password.getNote());
            values.put("date",new SimpleDateFormat("yyyy-MM-dd \n HH:mm:ss").format(new Date(System.currentTimeMillis())));
            long result =db.insert("passwords", null, values);
            if(result == -1)
                results = "\t\t\t插入数据异常!\n请注意\"官网名字\"是否重复";
        }
        catch (SQLException e){
            e.printStackTrace();
            results += "插入数据异常!" + e.getMessage();
        }
        return results;

    }

    public String updatePassword(Password password) {

        ContentValues values = new ContentValues();
//        values.put("title", password.getUsername());
        values.put("username", password.getUsername());
        values.put("password", password.getPassword());
        values.put("note", password.getNote());
        values.put("date",password.getDate());
        db.update("passwords", values, "title = ?", new String[]{password.getTitle()});
//        db.update("passwords", values, null, null );
        return "更新成功！";

    }
    @SuppressLint("Range")
    public Password querySingleCursor_catItem(String title){
        Password ps = new Password();
        String sql = "select * from passwords where title = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{title},null);

        Log.d("my2query", "cursor!=null ->"+String.valueOf(cursor!=null)+"  "+String.valueOf(cursor.getCount()));
        if(cursor!=null) {
            cursor.moveToFirst();
            ps.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            ps.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            ps.setNote(cursor.getString(cursor.getColumnIndex("note")));
            ps.setDate(new SimpleDateFormat("yyyy-MM-dd \n HH:mm:ss").format(new Date(System.currentTimeMillis())));
        }
        cursor.close();
        return ps;
    }
    @SuppressLint("Range")
    public String querySinglePwd_catItem(String title){
        String results = "-1";
        String sql = "select * from passwords where title = ?";
        try {
            Cursor cursor = db.rawQuery(sql,new String[]{title},null);

            Log.d("my2query", "cursor!=null ->"+String.valueOf(cursor!=null)+"  "+String.valueOf(cursor.getCount()));
            if(cursor!=null) {
                cursor.moveToFirst();
                results = cursor.getString(cursor.getColumnIndex("password"));
            }
            cursor.close();
            return results;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            results += "查询数据异常!" + e.getMessage();
        }
        Log.d("my2query",results);
        return results;
    }
    @SuppressLint("Range")
    public void queryPasswordAll(){
//        Cursor cursor = db.query("passwords",null,"title=?", new String[]{stringtitle},
        Cursor cursor = db.query("passwords",null,null, null,
                null, null, null, null);
        if(cursor!=null){
            if(!cursor.moveToLast()){
                Log.d("myquery","空的");
                return;
            }
//            cursor.moveToLast();
            int cflag =0;
            {
                cflag++;
                mainTitle = cursor.getString(cursor.getColumnIndex("title"));
                mainUser = cursor.getString(cursor.getColumnIndex("username"));
//                mainPwd = cursor.getString(cursor.getColumnIndex("password"));
                mainPwd = "********";
                mainNote = cursor.getString(cursor.getColumnIndex("note"));
                mainDate = cursor.getString(cursor.getColumnIndex("date"));
                //准备数据
                Log.d("myquery", "id:"+String.valueOf(mainId));
                sql_dataList.add(new AppInfo(mainTitle,mainUser,mainPwd,mainNote,mainDate));
            }
            while (cursor.moveToPrevious())
//            while (cursor.moveToNext())
            {
                cflag++;
                mainId = cursor.getInt(0); // 直接通过索引获取字段值
                mainTitle = cursor.getString(cursor.getColumnIndex("title"));
                mainUser = cursor.getString(cursor.getColumnIndex("username"));
//                mainPwd = cursor.getString(cursor.getColumnIndex("password"));
                mainPwd = "********";
                mainNote = cursor.getString(cursor.getColumnIndex("note"));
                mainDate = cursor.getString(cursor.getColumnIndex("date"));
                //准备数据
                sql_dataList.add(new AppInfo(mainTitle,mainUser,mainPwd,mainNote,mainDate));
                Log.d("myquery", "id:"+String.valueOf(mainId));
            }
            Log.d("myquery", "表格长度:"+String.valueOf(cflag));
        }
        cursor.close();
    }
/**/
    public String deleteItemData(String title){
        String results = "";
        String sql = "delete from passwords where title = ?";
        try {
                results = "1";
            db.execSQL(sql,new String[]{title});
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            results += "查询数据异常!" + e.getMessage();
        }
        return results;


    }


}