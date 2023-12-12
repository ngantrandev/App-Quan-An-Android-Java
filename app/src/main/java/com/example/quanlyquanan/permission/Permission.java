package com.example.quanlyquanan.permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.quanlyquanan.setting.MyApplication;

public class Permission {
    private Fragment fragment;

    public Permission(Fragment fragment) {
        this.fragment = fragment;
    }

    public boolean isPermissionPickFileAccepted() {
        boolean res2 = ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestRuntimePermission() {
        if (fragment.shouldShowRequestPermissionRationale(MyApplication.PERMISSION_READ_EXTERNAL_STORAGE)) {
            Log.d("CHECKPERMISSION", "requestRuntimePermission: " + "else if");
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(fragment.requireContext());
            builder.setMessage("Ứng dụng cần quyền truy cập bộ nhớ để mở thư viện ảnh!")
                    .setTitle("Yêu cầu quyền truy cập bộ nhớ")
                    .setCancelable(false)
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{MyApplication.PERMISSION_READ_EXTERNAL_STORAGE}, MyApplication.PERMISSION_REQ_CODE);

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        } else {
            Log.d("CHECKPERMISSION", "requestRuntimePermission: " + "else");
            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{MyApplication.PERMISSION_READ_EXTERNAL_STORAGE}, MyApplication.PERMISSION_REQ_CODE);
        }
    }
}
