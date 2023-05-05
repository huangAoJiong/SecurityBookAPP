package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText upTitle;
    private EditText upUser;
    private EditText upPwd;
    private EditText upNote;
    private Button updateBtn;
    private Button updateBack;
    PasswordDAO pwdDAO;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        pwdDAO = new PasswordDAO(this);

        upUser = findViewById(R.id.editupdateUser);
        upPwd = findViewById(R.id.editupdatePwd);
        upNote = findViewById(R.id.editupdateNote);
        upTitle = findViewById(R.id.editupTitle);

        updateBtn = findViewById(R.id.subminupdateBtn);
        updateBack = findViewById(R.id.backupdateBtn);

        Bundle bb = this.getIntent().getExtras();
        upTitle.setText(bb.getString("UPtitle"));
        upUser.setText(bb.getString("UPuser"));
        upPwd.setText(bb.getString("UPpwd"));
        upNote.setText(bb.getString("UPnote"));

        updateBack.setOnClickListener(this);
        updateBtn.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.subminupdateBtn){
            String newtitleStr = upTitle.getText().toString();
            String newuserStr = upUser.getText().toString();
            String newpwdStr = upPwd.getText().toString();
            String newnoteStr = upNote.getText().toString();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd \n HH:mm:ss").format(new Date(System.currentTimeMillis()));
            Password ps = new Password(newtitleStr,newuserStr,newpwdStr,newnoteStr,dateStr);
            String results = pwdDAO.updatePassword(ps);
            Toast.makeText(this,results,Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.backupdateBtn){
            finish();
        }
    }
}