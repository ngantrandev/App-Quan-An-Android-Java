package com.example.quanlyquanan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.api.FoodApi;
import com.example.quanlyquanan.response.ResponseFood;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button  btnShowFood, btnShowCategory, btnShowTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("CHECKACTIVITY", "MainActivity On create");

        setControl();
        setEvent();
//        getData();
    }



    private void setEvent() {

        btnShowFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityListFood.class);
                startActivity(intent);
            }
        });

        btnShowCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityListCategory.class);
                startActivity(intent);
            }
        });

        btnShowTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityListTable.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        btnShowCategory = findViewById(R.id.btn_show_category_mainactivity);
        btnShowFood = findViewById(R.id.btn_show_listfood_mainactivity);
        btnShowTable = findViewById(R.id.btn_show_tablelist_mainactivity);

    }

}