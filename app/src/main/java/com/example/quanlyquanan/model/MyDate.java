package com.example.quanlyquanan.model;

public class MyDate {
    private String month, year;

    public MyDate(String month, String year) {
        this.month = month;
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return month.length() == 1 ? "0" + month + "/" + year : "" + month + "/" + year;
    }
}
