package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

//import com.example.myapplication.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.myapplication.unit.DB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button add_user_Btn;
    private Button update_Btn;
    //列表数据定义
    private SwipeMenuListView   sql_List_view;
//    private ListView   sql_List_view;
    public ArrayList<AppInfo> dataList;
    private ListViewAdapter adapter;
    private SwipeMenuCreator creator;
    private PasswordDAO pwdDAOmain;
    private String[] books = { "奥特曼","阿童木","哪吒","钢铁侠","蜘蛛侠","美国队长"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化

        initView();

    }

    private void initView() {

        add_user_Btn = (Button)findViewById(R.id.add_button);
        update_Btn = (Button)findViewById(R.id.updata_button);
//        dataList = new ArrayList<AppInfo>();
//        dataList.add(new AppInfo("a","亚马逊","root","passwd","null"));
//        //初始化ListView
        sql_List_view =  findViewById(R.id.sql_list);
        pwdDAOmain = new PasswordDAO(this);
        pwdDAOmain.queryPasswordAll();
        adapter = new ListViewAdapter(this,pwdDAOmain.sql_dataList);
        sql_List_view.setAdapter(adapter);

        creatSwipeMenu();
        sql_List_view.setMenuCreator(creator);
        onClickListener(sql_List_view);

//        //设置案件监听
        add_user_Btn.setOnClickListener(this);
        update_Btn.setOnClickListener(this);
        updateList();
    }

    /**
     * 创建策划的Menu
     */
    private void creatSwipeMenu() {
        creator = new SwipeMenuCreator() {

            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0xbf,0xff)));
                // set item width
                openItem.setWidth(dp2px(70));
                // set item title
                openItem.setTitle("查看密码");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
                SwipeMenuItem editItem = new SwipeMenuItem((getApplicationContext()));
                editItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0xA5,0x00)));
                // set item width
                editItem.setWidth(dp2px(55));
                // set item title
                editItem.setTitle("修改");
                // set item title fontsize
                editItem.setTitleSize(18);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(55));
                // set a icon
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
//                deleteItem.setIcon(R.drawable.);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
    }

    /**
     * 点击SwipMenu触发的事件
     *
     * @param swipeMenuListView
     */
    private void onClickListener(SwipeMenuListView swipeMenuListView) {
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                ApplicationInfo item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // open
                        catPwd(position);
                        break;
                    case 1:
                        // 修改
                        updateItem(position);
//                        Toast.makeText(MainActivity.this,"还未开发此功能",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        deleteMessageBox(position);
                        break;
                }
                return false;
            }
        });
        sql_List_view.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                catPwd(position);
            }
        });
        // set SwipeListener 左滑、右滑监听 ; 打开关闭菜单监听 ; 长按item监听
        /*
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
                Toast.makeText(getApplicationContext(), position + " swipe start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
                Toast.makeText(getApplicationContext(), position + " swipe end", Toast.LENGTH_SHORT).show();
            }
        });



        // set MenuStateChangeListener 打开关闭菜单监听
        swipeMenuListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
                Toast.makeText(getApplicationContext(), position + " onMenuOpen", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClose(int position) {
                Toast.makeText(getApplicationContext(), position + " onMenuClose", Toast.LENGTH_SHORT).show();
            }
        });


        // test item long click 长按item监听
        swipeMenuListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

         */
    }

    private void updateItem(int position){
        AppInfo item_clicked  = (AppInfo)sql_List_view.getItemAtPosition(position);
        pwdDAOmain = new PasswordDAO(MainActivity.this);
        Bundle bundle = new Bundle();
        bundle.putString("UPtitle",item_clicked.getTitleStr());
        bundle.putString("UPuser",item_clicked.getUserStr());
        bundle.putString("UPpwd", pwdDAOmain.querySinglePwd_catItem(item_clicked.getTitleStr()));
        bundle.putString("UPnote",item_clicked.getNoteStr());
        Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void catPwd(int position){
        AppInfo item_clicked  = (AppInfo)sql_List_view.getItemAtPosition(position);
        pwdDAOmain = new PasswordDAO(MainActivity.this);
        String pwdStr = pwdDAOmain.querySinglePwd_catItem(item_clicked.getTitleStr());
        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this)
                .setMessage(pwdStr);
        builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {            }
        } );
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void deleteMessageBox(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this)
                .setTitle("💢警告!")//设置对话框 标题
                .setMessage("确定要删除吗？");
        builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppInfo item_clicked  = (AppInfo)sql_List_view.getItemAtPosition(position);
                pwdDAOmain = new PasswordDAO(MainActivity.this);
                pwdDAOmain.deleteItemData(item_clicked.getTitleStr());
                updateList();
            }
        } );
        builder.setNegativeButton( "取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText( MainActivity.this,"点击了取消按钮", Toast.LENGTH_SHORT).show();
            }
        } );
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.add_button == id){
            Intent intent = new Intent(MainActivity.this,AddNewData.class);
            startActivity(intent);
        }
        if(id == R.id.updata_button){
            updateList();
//            Toast.makeText(this,String.valueOf(pwdDAO.sql_dataList.size()),Toast.LENGTH_SHORT).show();
        }

    }
    public void updateList(){
        Context c;
        pwdDAOmain = new PasswordDAO(this);
        pwdDAOmain.queryPasswordAll();
        adapter = new ListViewAdapter(this, pwdDAOmain.sql_dataList);
        sql_List_view.setAdapter(adapter);
    }
}


