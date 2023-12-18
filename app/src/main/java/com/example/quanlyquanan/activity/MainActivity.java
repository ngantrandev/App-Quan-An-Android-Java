package com.example.quanlyquanan.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.hao.activity_table_menu;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnShowFood, btnShowCategory, btnShowTable, btnThongKe, btnDatban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityChart.class);
                startActivity(intent);
            }
        });

        btnDatban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: btndatban");
                try{
                    Intent intent = new Intent(MainActivity.this, activity_table_menu.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("TAG", "onClick: btndatban" + e);
                }
            }
        });

        findViewById(R.id.btn_show_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewBottomSheet = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_info_user, null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(viewBottomSheet);
                ImageView btnBack = viewBottomSheet.findViewById(R.id.ic_back_layout_userinfo);

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.show();
                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) viewBottomSheet.getParent());
                bottomSheetBehavior.setPeekHeight(1000);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setControl() {
        btnShowCategory = findViewById(R.id.btn_show_category_mainactivity);
        btnShowFood = findViewById(R.id.btn_showfood_activitymain);
        btnShowTable = findViewById(R.id.btn_show_tablelist_mainactivity);
        btnThongKe = findViewById(R.id.btn_thongke_mainactivity);
        btnDatban = findViewById(R.id.btnDatbanactivityMain);
    }

}