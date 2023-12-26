package com.example.quanlyquanan.response;

import com.example.quanlyquanan.model.BillInfo;
import com.google.gson.annotations.SerializedName;

public class ResponseBillInfoById {
    private String message, status, error;
    @SerializedName("billInfo")
    BillInfo billInfo;

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

    public BillInfo getBillInfo() {
        return billInfo;
    }

    public void setBillInfo(BillInfo billInfo) {
        this.billInfo = billInfo;
    }
}
