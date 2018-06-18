package com.example.chetanmuliya.entrancetest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.chetanmuliya.entrancetest.R;
import com.example.chetanmuliya.entrancetest.helper.SQLiteLoginHandler;
import com.example.chetanmuliya.entrancetest.helper.SessionManager;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    private TextView username,userEmail,performanceView,scoreView;
    private SessionManager session;
    private SQLiteLoginHandler db;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("D A S H B O A R D");
        getSupportActionBar();
        username=(TextView)findViewById(R.id.userName);
        userEmail=(TextView)findViewById(R.id.userEmail);
        performanceView=(TextView)findViewById(R.id.performanceView1);
        scoreView=(TextView)findViewById(R.id.scoreView1);

        //sqlite
        db=new SQLiteLoginHandler(getApplicationContext());
        session=new SessionManager(getApplicationContext());
        if(!session.isLoggedIn()){
            logoutUser();
        }
        HashMap<String,String> user=db.getUserDetails();
        String name=user.get("name");
        String email=user.get("email");

        username.setText("Name : "+name);
        userEmail.setText("Email : "+email);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(Dashboard.this,
                LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void startTest(View view) {
        Intent intent = new Intent(Dashboard.this,
                MainActivity.class);
        startActivity(intent);
    }

    public void signOut(View view) {
        logoutUser();
    }
}
