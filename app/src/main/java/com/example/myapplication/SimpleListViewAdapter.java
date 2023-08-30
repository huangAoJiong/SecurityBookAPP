package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class SimpleListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AppInfo> dataList;

    public SimpleListViewAdapter(Context context, ArrayList<AppInfo> dataList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_data_simple_list, parent, false);
            viewHolder = new ViewHolder();
            //分别获取 image view 和 textview 的实例
            viewHolder.simple_title = convertView.findViewById(R.id.text_title_item_simple);
            viewHolder.simple_note = convertView.findViewById(R.id.text_note_item_simple);
            viewHolder.simple_img = convertView.findViewById(R.id.img_view_simple);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AppInfo data = dataList.get(position);
        viewHolder.simple_title.setText(data.getTitleStr());
        viewHolder.simple_note.setText(data.getNoteStr());
        viewHolder.simple_img.setImageAlpha(R.drawable.lock);

        return convertView;
    }

    static class ViewHolder {
        TextView simple_title;
        TextView simple_note;
        ImageView simple_img;
    }
}

