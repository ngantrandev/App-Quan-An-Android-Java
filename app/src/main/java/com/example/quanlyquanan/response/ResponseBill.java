package com.example.quanlyquanan.response;

import com.example.quanlyquanan.model.Bill;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBill {
    private String message, status, error;
    private int count;

    @SerializedName("bills")
    private List<Bill> billList;

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

    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }
}
