package com.example.quanlyquanan.response;

import com.example.quanlyquanan.model.Food;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseFood {
    private String message, status, error;
    private int count;

    @SerializedName("foods")
    private List<Food> foodList;

    public ResponseFood(String message, String status, String error, int count, List<Food> foodList) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.count = count;
        this.foodList = foodList;
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

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }
}
