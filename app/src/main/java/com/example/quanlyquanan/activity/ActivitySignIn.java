package com.example.quanlyquanan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyquanan.R;
import com.example.quanlyquanan.api.UserApi;
import com.example.quanlyquanan.hao.activity_food_menu;
import com.example.quanlyquanan.hao.activity_loadingApp;
import com.example.quanlyquanan.model.Session;
import com.example.quanlyquanan.model.User;
import com.example.quanlyquanan.response.ResponseUser;
import com.example.quanlyquanan.response.ResponseUserLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignIn extends AppCompatActivity {

    Button btnLogin;
    EditText InputUserName,InputPassword;
    TextView labelErrorLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setcontrol();
        setevent();
    }

    private void setevent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyAccount();
            }
        });
    }

    private void VerifyAccount() {
        User user1 = new User("",InputUserName.getText().toString().trim(),InputPassword.getText().toString().trim(),"","","","","","","");
        Log.d("LOGINVERIFY", "VerifyAccount: "+user1);
        UserApi.userApi.LoginUser(user1).enqueue(new Callback<ResponseUserLogin>() {
            @Override
            public void onResponse(Call<ResponseUserLogin> call, Response<ResponseUserLogin> response) {
                Log.d("Login", "Response Login");
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("Success")) {
                        Toast.makeText(ActivitySignIn.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        User user = response.body().getUser();
                        Session.user = user;
                        Intent intent = new Intent(ActivitySignIn.this, MainActivity.class);
                        ActivitySignIn.this.startActivity(intent);
                    }else if(response.body().getStatus().equals("Failed")){
                        labelErrorLogin.setText(response.body().getError().toString());
                    }
                }else{
                    Toast.makeText(ActivitySignIn.this, "Lỗi khi kết nối với Server", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<ResponseUserLogin> call, Throwable t) {
                Toast.makeText(ActivitySignIn.this, "Không thể kết nối với Server", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setcontrol() {
        btnLogin = findViewById(R.id.btnlogin);
        InputUserName = findViewById(R.id.InputUsername);
        InputPassword = findViewById(R.id.InputPassword);
        labelErrorLogin = findViewById(R.id.labelErrorLogin);
    }
}