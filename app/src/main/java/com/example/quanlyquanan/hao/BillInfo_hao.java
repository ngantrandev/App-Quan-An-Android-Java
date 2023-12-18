package com.example.quanlyquanan.hao;

import java.util.Date;
import java.util.List;

public class BillInfo_hao {
    private int id,id_table,total,status;
    private String Tip;
    private Date timeCheckIn,timeCheckOut;
    private List<FoodInfo> DsFood;

    public BillInfo_hao() {
    }

    public BillInfo_hao(int id, int id_table, int total, int status, String tip, Date timeCheckIn, List<FoodInfo> dsFood) {
        this.id = id;
        this.id_table = id_table;
        this.total = total;
        this.status = status;
        Tip = tip;
        this.timeCheckIn = timeCheckIn;
        DsFood = dsFood;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_table() {
        return id_table;
    }

    public void setId_table(int id_table) {
        this.id_table = id_table;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTip() {
        return Tip;
    }

    public void setTip(String tip) {
        Tip = tip;
    }

    public Date getTimeCheckIn() {
        return timeCheckIn;
    }

    public void setTimeCheckIn(Date timeCheckIn) {
        this.timeCheckIn = timeCheckIn;
    }

    public Date getTimeCheckOut() {
        return timeCheckOut;
    }

    public void setTimeCheckOut(Date timeCheckOut) {
        this.timeCheckOut = timeCheckOut;
    }

    public List<FoodInfo> getDsFood() {
        return DsFood;
    }

    public void setDsFood(List<FoodInfo> dsFood) {
        DsFood = dsFood;
    }
}
