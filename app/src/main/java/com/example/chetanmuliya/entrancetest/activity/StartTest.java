package com.example.chetanmuliya.entrancetest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.chetanmuliya.entrancetest.R;

public class StartTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
    }

    public void startTest(View view) {
        Intent intent=new Intent(StartTest.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
