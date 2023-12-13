package com.example.quanlyquanan.setting;

import android.Manifest;
import android.app.Application;

public class MyApplication extends Application {
    public static final String BASE_API_URL = "http://10.251.3.163:8080";
    public static final String MESSAGE_TOAST_CREATEFOOD_SUCCESS ="Thêm món ăn thành công";
    public static final String MESSAGE_TOAST_CREATEFOOD_FAILED ="Thêm món ăn thất bại";
    public static final String MESSAGE_TOAST_SERVER_NOTRESPONSE = "Máy chủ không thể phản hồi";
    public static final String MESSAGE_DELETE_FOOD_SUCCESSFUL = "Xóa món ăn thành công";
    public static final String MESSAGE_DIALOG_CREATE_FOOD = "Nhấn \"Xác nhận\" để xác nhận thêm món ăn";
    public static final String MESSAGE_LOAD_DATA_SUCCESSFUL = "Tải dữ liệu thành công";

    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES;
    public static final int PERMISSION_REQ_CODE = 1250;
    public static final int REQUEST_CODE_CHOOSE_FILE=101;
}
