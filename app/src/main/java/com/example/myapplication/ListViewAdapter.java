package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
/*
public class ListViewAdapter extends BaseAdapter {

    private Context mcontext;
    private int mresource;
    private ArrayList<AppInfo> dataList;

    public ListViewAdapter(@NonNull Context context, @NonNull ArrayList<AppInfo> objects) {
        this.dataList = objects;
        this.mcontext = context;
//        this.mresource = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        View row = convertView;
//        convertView = LayoutInflater.from(this.mcontext).inflate(this.mresource, parent,false);
        //为每一个子项加载设定的布局
//        View view=LayoutInflater.from(getContext()).inflate(R.layout.item_data_list,parent,false);

        AppInfo data = dataList.get(position);//得到当前项的 Fruit 实例
        //分别获取 image view 和 textview 的实例
        TextView item_text_num = convertView.findViewById(R.id.item_text_num);
        TextView item_text_auth = convertView.findViewById(R.id.item_text_auth);
        TextView item_text_user = convertView.findViewById(R.id.item_text_user);
        TextView item_text_pwd = convertView.findViewById(R.id.item_text_pwd);
        TextView item_text_note = convertView.findViewById(R.id.item_text_note);

        item_text_num.setText(data.getNumber());
        item_text_auth.setText(data.getTitleStr());
        item_text_user.setText(data.getUserStr());
        item_text_pwd.setText(data.getPwdStr());
        item_text_note.setText(data.getNoteStr());


        return convertView;
    }
}
*/
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AppInfo> dataList;

    public ListViewAdapter(Context context, ArrayList<AppInfo> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_data_list, parent, false);
            viewHolder = new ViewHolder();
            //分别获取 image view 和 textview 的实例
//            viewHolder.item_text_num = convertView.findViewById(R.id.item_text_num);
            viewHolder.item_text_auth = convertView.findViewById(R.id.item_text_auth);
            viewHolder.item_text_user = convertView.findViewById(R.id.item_text_user);
            viewHolder.item_text_pwd = convertView.findViewById(R.id.item_text_pwd);
            viewHolder.item_text_note = convertView.findViewById(R.id.item_text_note);
            viewHolder.item_text_date = convertView.findViewById(R.id.item_text_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AppInfo data = dataList.get(position);
//        viewHolder.item_text_num.setText(data.getNumber());
        viewHolder.item_text_auth.setText(data.getTitleStr());
        viewHolder.item_text_user.setText(data.getUserStr());
        viewHolder.item_text_pwd.setText(data.getPwdStr());
        viewHolder.item_text_note.setText(data.getNoteStr());
        viewHolder.item_text_date.setText(data.getDateStr());
        return convertView;
    }

    static class ViewHolder {
//        TextView item_text_num;
        TextView item_text_auth;
        TextView item_text_user;
        TextView item_text_pwd;
        TextView item_text_note;
        TextView item_text_date;
    }
}
