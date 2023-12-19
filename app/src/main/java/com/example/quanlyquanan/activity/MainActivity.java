package com.example.quanlyquanan.activity;

import androidx.annotation.LongDef;
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
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.api.UserApi;
import com.example.quanlyquanan.hao.activity_loadingApp;
import com.example.quanlyquanan.hao.activity_table_menu;
import com.example.quanlyquanan.model.Session;
import com.example.quanlyquanan.model.User;
import com.example.quanlyquanan.response.ResponseUser;
import com.example.quanlyquanan.response.ResponseUserLogin;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnShowFood, btnShowCategory, btnShowTable, btnThongKe, btnDatban;
    private TextView tv_user_fullname_activitymain;
    private  User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Session.user==null){
            Intent intent = new Intent(MainActivity.this, ActivitySignIn.class);
            MainActivity.this.startActivity(intent);
            MainActivity.this.finish();
        }else{
            user = Session.user;
        }
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
                TextView labelUName = viewBottomSheet.findViewById(R.id.LabelUName);
                TextView labelUBirth = viewBottomSheet.findViewById(R.id.LabelUBirth);
                TextView labelUEmail = viewBottomSheet.findViewById(R.id.LabelUEmail);
                TextView labelUPhone = viewBottomSheet.findViewById(R.id.LabelUPhone);
                TextView LabelUSex = viewBottomSheet.findViewById(R.id.LabelUSex);
                TextView labelName = viewBottomSheet.findViewById(R.id.LabelNameUser);
                TextView labelRole = viewBottomSheet.findViewById(R.id.LabelRoleUser);
                labelUName.setText(user.getFirstName().equals("null")?"":user.getFirstName()+" "+ (user.getLastName().equals("null")?"Trống":user.getLastName()));
                labelUBirth.setText(user.getBirthDay().equals("null")?"Trống":user.getBirthDay());
                labelUEmail.setText(user.getEmail().equals("null")?"Trống":user.getEmail());
                labelUPhone.setText(user.getSdt().equals("null")?"Trống":user.getSdt());
                LabelUSex.setText(user.getSex().equals("null")?"Trống":user.getSex());
                labelUName.setText(user.getFirstName().equals("null")?"":user.getFirstName()+" "+ (user.getLastName().equals("null")?"Trống":user.getLastName()));
                if(user.getFirstName().equals("null")&& user.getLastName().equals("null")){
                    labelName.setText("Chưa có tên");
                }
                labelRole.setText(user.getRole().toString());
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
        tv_user_fullname_activitymain = findViewById(R.id.tv_user_fullname_activitymain);
        tv_user_fullname_activitymain.setText(user.getLastName());
        if(user.getFirstName().equals("null")&& user.getLastName().equals("null")){
            tv_user_fullname_activitymain.setText("Chưa có tên");
        }
    }

}