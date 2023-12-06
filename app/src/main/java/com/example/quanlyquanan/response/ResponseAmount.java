package com.example.quanlyquanan.response;

import com.example.quanlyquanan.model.DayAmount;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseAmount {

    private String message, status, error;
    private int count;

    @SerializedName("list_bill_amount")
    private List<DayAmount> listBillAmount;

    public ResponseAmount(String message, String status, String error, int count, List<DayAmount> billamounts) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.count = count;
        this.listBillAmount = billamounts;
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

    public List<DayAmount> getListBillAmount() {
        return listBillAmount;
    }

    public void setListBillAmount(List<DayAmount> listBillAmount) {
        this.listBillAmount = listBillAmount;
    }
}
