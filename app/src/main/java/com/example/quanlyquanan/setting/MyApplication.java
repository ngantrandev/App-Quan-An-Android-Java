package com.example.quanlyquanan.setting;

import android.app.Application;

public class MyApplication extends Application {
    public static final String BASE_API_URL = "http://192.168.1.3:8080";
    public static final String MESSAGE_TOAST_CREATEFOOD_SUCCESS ="Thêm món ăn thành công";
    public static final String MESSAGE_TOAST_CREATEFOOD_FAILED ="Thêm món ăn thất bại";
    public static final String MESSAGE_TOAST_SERVER_NOTRESPONSE = "Máy chủ không thể phản hồi";
    public static final String MESSAGE_DELETE_FOOD_SUCCESSFUL = "Xóa món ăn thành công";
    public static final String MESSAGE_DIALOG_CREATE_FOOD = "Nhấn \"Xác nhận\" để xác nhận thêm món ăn";
}
