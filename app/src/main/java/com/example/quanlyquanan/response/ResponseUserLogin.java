package com.example.quanlyquanan.response;

import android.util.Log;
import android.widget.Toast;

import com.example.quanlyquanan.model.User;
import com.google.gson.annotations.SerializedName;

public class ResponseUserLogin {
    private String message, status, error,access_token;
    @SerializedName("user")
    private User user;
    public ResponseUserLogin(String message, String status, String error,String access_token,User user) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.access_token = access_token;
        this.user = user;
    }
//    public ResponseUserLogin(String message, String status, String error,String access_token) {
//        this.message = message;
//        this.status = status;
//        this.error = error;
//        Log.d("LOGINVERIFY", "Khong co user");
//        this.access_token = access_token;
//    }
    public User getUser(){
        return user;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getAccess_token() {
        return access_token;
    }

}
