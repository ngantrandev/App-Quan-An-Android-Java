package com.example.quanlyquanan.hao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlyquanan.R;

public class activity_loadingApp extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_app);
        StartApp();
    }

    public void StartApp() {
        Manager.CreateDataTest();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity_loadingApp.this, activity_table_menu.class);
                startActivity(intent);
                finish();
            }
        },250);
    }
}