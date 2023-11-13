package com.example.quanlyquanan.response;

import com.example.quanlyquanan.model.Category;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseCategories {
    private String message, status, error;
    private int count;

    @SerializedName("categories")
    private List<Category> categoryList;

    public ResponseCategories(String message, String status, String error, int count, List<Category> categoryList) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.count = count;
        this.categoryList = categoryList;
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

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
