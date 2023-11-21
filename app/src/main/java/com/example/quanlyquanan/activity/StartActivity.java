package com.example.quanlyquanan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.quanlyquanan.R;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.RotatingCircle;

public class StartActivity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        progressBar = findViewById(R.id.progressbar_activitystart);

        checkAuthentication();
    }


    private void checkAuthentication() {
        progressBar.setIndeterminateDrawable(new Circle());


        // check if true
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        progressBar.setVisibility(View.GONE);
        startActivity(intent);


        // check if false
    }
}