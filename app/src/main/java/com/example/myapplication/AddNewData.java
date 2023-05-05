package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewData extends AppCompatActivity implements View.OnClickListener {


    //新增内容组件
    private EditText newTitle;
    private EditText newUser;
    private EditText newPwd;
    private EditText newNote;
//    private EditText newDate;
    private Button newAddBtn;
    private Button backBtn;
    //创建数据库
    PasswordDAO pwdDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_data);

        newTitle = (EditText) findViewById(R.id.editnewTitle);
        newUser = (EditText) findViewById(R.id.editnewUser);
        newPwd = (EditText) findViewById(R.id.editnewPwd);
        newNote = (EditText) findViewById(R.id.editnewNote);
//        newDate = (EditText) findViewById(R.id.editnewDote);
        newAddBtn = (Button)findViewById(R.id.subminBtn);
        backBtn = (Button) findViewById(R.id.backBtn);

        newAddBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        pwdDAO = new PasswordDAO(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.subminBtn){

            String newtitleStr = newTitle.getText().toString();
            String newuserStr = newUser.getText().toString();
            String newpwdStr = newPwd.getText().toString();
            String newnoteStr = newNote.getText().toString();
            String dateStr = new SimpleDateFormat("YY-MM-DD 'at' HH:mm:ss").format(new Date(System.currentTimeMillis()));
            Password ps = new Password(newtitleStr,newuserStr,newpwdStr,newnoteStr,dateStr);
            String results = pwdDAO.addPassword(ps);
            Toast.makeText(this,results,Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.backBtn){
            finish();

        }
    }
}