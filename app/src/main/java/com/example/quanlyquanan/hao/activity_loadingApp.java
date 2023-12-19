package com.example.quanlyquanan.hao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.activity.ActivitySignIn;
import com.example.quanlyquanan.activity.StartActivity;
import com.example.quanlyquanan.api.FoodApi;
import com.example.quanlyquanan.api.UserApi;
import com.example.quanlyquanan.response.ResponseFood;
import com.example.quanlyquanan.response.ResponseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_loadingApp extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_app);
        StartApp();
    }

    public void StartApp() {
//        Manager.CreateDataTest();
//        handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(activity_loadingApp.this, activity_table_menu.class);
//                startActivity(intent);
//                finish();
//            }
//        },250);
        UserApi.userApi.getUsers().enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("Success")) {
                        Toast.makeText(activity_loadingApp.this, "Kết nối thành công", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(activity_loadingApp.this, ActivitySignIn.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(activity_loadingApp.this, "Không thể kết nối với Server", Toast.LENGTH_LONG).show();
                StartApp();
            }
        });
    }
}