package com.example.quanlyquanan.model;

import com.google.gson.annotations.SerializedName;

public class DayAmount {
    @SerializedName("total_food_amount")
    private int totalFoodAmount;
    @SerializedName("total_tips")
    private int totalTips;
    private int date, dathanhtoan, chuathanhtoan;

    public int getTotalFoodAmount() {
        return totalFoodAmount;
    }

    public void setTotalFoodAmount(int totalFoodAmount) {
        this.totalFoodAmount = totalFoodAmount;
    }

    public int getTotalTips() {
        return totalTips;
    }

    public void setTotalTips(int totalTips) {
        this.totalTips = totalTips;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDathanhtoan() {
        return dathanhtoan;
    }

    public void setDathanhtoan(int dathanhtoan) {
        this.dathanhtoan = dathanhtoan;
    }

    public int getChuathanhtoan() {
        return chuathanhtoan;
    }

    public void setChuathanhtoan(int chuathanhtoan) {
        this.chuathanhtoan = chuathanhtoan;
    }
}
