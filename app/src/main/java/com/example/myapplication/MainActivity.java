package com.example.myapplication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_AUDIO;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

//import com.example.myapplication.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.myapplication.unit.DB;
//import com.example.myapplication.about.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {//,ColorDialog.OnColorSelectedListener {

    private final int REQUEST_PERMISSION_CODE = 111;
    private String[] PERMISSION_STORAGE = {
            READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private boolean havePermission = false;
    private AlertDialog dialog;
    private ImageButton add_user_Btn;
    private EditText search_edit;
    private ImageButton search_img_btn;
    private Button update_Btn;
    private Button setBG_Btn;
    private androidx.constraintlayout.widget.ConstraintLayout _list_;
    //列表数据定义
    private SwipeMenuListView   sql_List_view;
    public ArrayList<AppInfo> dataList;
    private ListViewAdapter adapter;
    private SimpleListViewAdapter simple_adapter;
    private SwipeMenuCreator creator;
    private PasswordDAO pwdDAOmain;
    private String[] books = { "奥特曼","阿童木","哪吒","钢铁侠","蜘蛛侠","美国队长"};

    private static final int REQUEST_IMAGE_CODE = 2;
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String[] PERMISSIONS = {READ_EXTERNAL_STORAGE};
    private  static int Account_count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 检查是否已经获得设备读写权限
        checkPermission();

        //初始化
        initView();
//        sql_List_view =  findViewById(R.id.sql_list);
//        pwdDAOmain = new PasswordDAO(this);
//        pwdDAOmain.queryPasswordAll();
//        simple_adapter = new SimpleListViewAdapter(this,pwdDAOmain.sql_dataList);
//        sql_List_view.setAdapter(simple_adapter);
    }

    /**
     * 动态申请权限
     */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSION_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }
    /**
     * 加载菜单条目
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.db_menu_layout, menu);
        return true;
    }

    /**
     * 功能：app菜单的item显示icon
     **/

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 实现菜单的点击事件，导出数据和导入数据的实现
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        //获取app的sqlite文件
        File dbFile = getDatabasePath("codebook.db");
        // 获取当前日期和时间
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 定义日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日-HH：mm：ss");

        // 格式化日期时间
        String formattedDateTime = currentDateTime.format(formatter);
        Log.d("datebase","currentDate:"+formattedDateTime);
        //导出数据
        if(itemId == R.id.export_db_id){
            File exportDir = new File(Environment.getExternalStorageDirectory(), "haoj的密码本");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            try {
                File exportFile = new File(exportDir, "export_codebook.db");
                File exportFile_Data = new File(exportDir, "export["+formattedDateTime+"].db");
                Log.d("datebase", "导出路径："+exportFile_Data.toString());
                FileInputStream fis = new FileInputStream(dbFile);
                FileOutputStream fos = new FileOutputStream(exportFile);
                FileInputStream fis_Data = new FileInputStream(dbFile);
                FileOutputStream fos_Data = new FileOutputStream(exportFile_Data);
                FileUtils.copy(fis,fos);
                FileUtils.copy(fis_Data,fos_Data);
                fis.close();
                fos.close();
                fos_Data.close();
                fis_Data.close();
                Toast.makeText(MainActivity.this,"数据成功导出，请及时备份",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.d("datebase", "导出失败: ");
                Toast.makeText(MainActivity.this,"数据导出失败",Toast.LENGTH_SHORT).show();
            }
        }
        //导入数据
        else if (itemId == R.id.import_db_id){
            File importFile = new File(Environment.getExternalStorageDirectory() + "/haoj的密码本/export_codebook.db");
            Log.d("datebase", "导入路径："+importFile.toString());
            try {
                FileInputStream fis = new FileInputStream(importFile);
                FileOutputStream fos = new FileOutputStream(dbFile);
                FileUtils.copy(fis, fos);
                fis.close();
                fos.close();
                Toast.makeText(getApplicationContext(), "导入成功", Toast.LENGTH_SHORT).show();
                updateList();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("datebase", "导入失败: ");
                Toast.makeText(getApplicationContext(), "导入失败" +
                        "\n若无法解决请联系黄管理", Toast.LENGTH_SHORT).show();
            }
        }
        else if(itemId == R.id.charge_bg_id){
            chooseImage();
        }
        else if(itemId == R.id.detail_id){
            AlertDialog dialog_datail;
            PackageManager manager = this.getPackageManager();
            String appName = null;
            String versionName = null;
            String sdkVersion = null;
            String PackageName = null;
            int versionCode = 0;
            try {
                PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                appName = manager.getApplicationLabel(this.getApplicationInfo()).toString();
                versionName = info.versionName;
                versionCode = info.versionCode;
//                sdkVersion = info.;
                PackageName = info.packageName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            dialog_datail = new AlertDialog.Builder(this)
                    .setTitle("About")//设置标题
                    .setMessage("app:"+appName+"\n版本号:"+versionName+"\nSdkVersion:"+sdkVersion+
                            "\nPackageName:"+PackageName+"\nauthor：Jason\n当前记录："+Account_count+
                            "\n(By https://github.com/huangAoJiong/SecurityBookAPP)")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog_datail.show();
        }
        else if (itemId == R.id.model_B_W_id){
            showDarkorLightAlertDialog();
        } else if (itemId == R.id.item_explain) {
            // 读取a.txt文件内容
            String fileContent = readFileFromAssets("a.txt");
            // 显示文件内容在对话框中
            showDialogWithFileContent(fileContent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        _list_ = findViewById(R.id.ConstraintLayout_id);

        /* 删除了该按钮setBG_Btn = (Button)findViewById(R.id.set_bg_btn);*/
        add_user_Btn = (ImageButton)findViewById(R.id.add_button);
//        update_Btn = (Button)findViewById(R.id.updata_button);
        search_edit = findViewById(R.id.edit_search_mainText);
        search_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
//                    Log.e("MainActivity", "onKey: 按下回车键");
                    String string = search_edit.getText().toString();
                    pwdDAOmain = new PasswordDAO(MainActivity.this);
                    pwdDAOmain.querySingleCursor_catItem(string);
                    adapter = new ListViewAdapter(MainActivity.this, pwdDAOmain.sql_dataList);
                    sql_List_view.setAdapter(adapter);
                    return true;
                }
                return false;
            }
        });
        search_img_btn = (ImageButton)findViewById(R.id.search_img_btn);
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
//        update_Btn.setOnClickListener(this);
        search_img_btn.setOnClickListener(this);
       /* 删除了该按钮setBG_Btn.setOnClickListener(this);*/
        updateList();
        InitListBackGround();
    }

    /**
     * app背景图片设置初始化
     */
    void InitListBackGround(){
        File dir = new File(Environment.getExternalStorageDirectory(), "haoj的密码本");
        File file = new File(dir, "infoBG.txt");
        try {
            // 读取文件内容
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            // 获取读取的文本内容
            String text = stringBuilder.toString();
            Log.d("readbg", text);
            // 在这里使用读取的文本内容
            setListbackground(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建策划的Menu，左滑菜单操作
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
                catNote(position);
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
        finish();
    }
    //查看密码api
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
    //查看备注api
    private void catNote(int position){
        AppInfo item_clicked  = (AppInfo)sql_List_view.getItemAtPosition(position);
        pwdDAOmain = new PasswordDAO(MainActivity.this);
        String pwdStr = pwdDAOmain.querySingleNote_catItem(item_clicked.getTitleStr());
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
                Toast.makeText( MainActivity.this,"取消删除操作", Toast.LENGTH_SHORT).show();
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
            finish();
        }
//        if(id == R.id.updata_button)
//            updateList();
        if(id == R.id.search_img_btn){
            String string = search_edit.getText().toString();
            pwdDAOmain = new PasswordDAO(this);
            pwdDAOmain.querySingleCursor_catItem(string);
            adapter = new ListViewAdapter(this, pwdDAOmain.sql_dataList);
            sql_List_view.setAdapter(adapter);
        }
        /*
        //删除了该按钮
        if(id==R.id.set_bg_btn){
            chooseImage();
        }*/

    }
    public void updateList(){
        Context c;
        search_edit.setText("");
        pwdDAOmain = new PasswordDAO(this);
        pwdDAOmain.queryPasswordAll();
        adapter = new ListViewAdapter(this, pwdDAOmain.sql_dataList);
        Account_count = pwdDAOmain.sql_dataList.size();
        sql_List_view.setAdapter(adapter);
    }

    //******************************选择图片背景
    // 处理权限请求的结果
    // 在 onCreate 方法中调用该方法
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }
    }





    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            Log.d("readbg*****", imageUri.toString());
            try {
                // 使用 ContentResolver 打开图片输入流
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                // 创建 Bitmap 对象
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                // 关闭输入流
                inputStream.close();
                // 创建 BitmapDrawable 对象并设置为背景
                BitmapDrawable backgroundDrawable = new BitmapDrawable(getResources(), bitmap);
                _list_.setBackground(backgroundDrawable);
                Log.d("readbg", imageUri.toString()+"\t"+imageUri.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //保存读取图像的路径
            File dir = new File(Environment.getExternalStorageDirectory(), "haoj的密码本");
            if (!dir.exists()) {
                Log.d("saveBG", "没有文件路径，");
                dir.mkdir();
            }
            File file = new File(dir, "infoBG.txt");
            try {
                // 写入文件
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(imageUri.toString().getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    void setListbackground(String img_str){
        Uri imageUri = Uri.parse(img_str);
        Log.d("readbg****", img_str);
        try {
            // 使用 ContentResolver 打开图片输入流
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            // 创建 Bitmap 对象
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            // 关闭输入流
            inputStream.close();
            // 创建 BitmapDrawable 对象并设置为背景
            BitmapDrawable backgroundDrawable = new BitmapDrawable(getResources(), bitmap);
            _list_.setBackground(backgroundDrawable);
        }catch (IOException e) {
            e.printStackTrace();
            Log.d("readbg", "错误设置，");
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    havePermission = true;
                    Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
                } else {
                    havePermission = false;
                    Toast.makeText(this, "授权被拒绝！", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")//设置标题
                        .setMessage("请开启文件访问权限，否则无法正常使用本应用！")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivity(intent);
                            }
                        }).create();
                dialog.show();
            } else {
                havePermission = true;
                Log.i("swyLog", "Android 11以上，当前已有权限");
            }
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    dialog = new AlertDialog.Builder(this)
                            .setTitle("提示")//设置标题
                            .setMessage("请开启文件访问权限，否则无法正常使用本应用！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                                }
                            }).create();
                    dialog.show();
                } else {
                    havePermission = true;
                    Log.i("swyLog", "Android 6.0以上，11以下，当前已有权限");
                }
            } else {
                havePermission = true;
                Log.i("swyLog", "Android 6.0以下，已获取权限");
            }
        }
    }
    private AlertDialog alertDialog2 = null; //单选框
    private int chooModel = 2;//0:dark   1 : light    2 : auto
    public void showDarkorLightAlertDialog() {

        UiModeManager mUiModeManager = (UiModeManager) this.getSystemService(Context.UI_MODE_SERVICE);
        final String[] items = {"暗色", "亮色", "自动"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("暗色模式");
        alertBuilder.setSingleChoiceItems(items, chooModel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chooModel = i;
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(MainActivity.this, ""+chooModel, Toast.LENGTH_SHORT).show();
                if(chooModel == 0){
                    //mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES); // 深色模式（黑暗模式）
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else if (chooModel ==1) {
                    // mUiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO); // 普通模式
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (chooModel == 2) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }

                alertDialog2.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog2.dismiss();
            }
        });

        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
    }


    private String readFileFromAssets(String filename) {
        StringBuilder fileContent = new StringBuilder();

        try {
            InputStream inputStream = getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("explains",  e.toString());
        }

        return fileContent.toString();
    }

    private void showDialogWithFileContent(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("说明");
        builder.setMessage(content);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}


