package com.example.quanlyquanan.response;

import com.example.quanlyquanan.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUser {
    private String message, status, error;
    private int count;

    @SerializedName("users")
    private List<User> userList;

    public ResponseUser(String message, String status, String error, int count, List<User> userList) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.count = count;
        this.userList = userList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
