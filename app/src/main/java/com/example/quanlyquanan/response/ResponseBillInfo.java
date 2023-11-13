package com.example.quanlyquanan.response;

import com.example.quanlyquanan.model.BillInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBillInfo {

    private String message, status, error;
    private int count;

    @SerializedName("billInfos")
    List<BillInfo> billInfoList;

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

    public List<BillInfo> getBillInfoList() {
        return billInfoList;
    }

    public void setBillInfoList(List<BillInfo> billInfoList) {
        this.billInfoList = billInfoList;
    }
}
