package com.example.quanlyquanan.permission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Permission {
    private AppCompatActivity activity;

    public Permission(AppCompatActivity activity) {
        this.activity = activity;
    }

    public boolean isPermissionCaptureAccepted() {
        boolean isAccepted = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        return isAccepted;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissionCapture() {
        activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1002);
    }

    public boolean isPermissionPickFileAccepted() {
        boolean res1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPickFilePermission() {
        activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
    }
}