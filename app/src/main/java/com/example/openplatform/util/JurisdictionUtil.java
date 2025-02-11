package com.example.openplatform.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.util.ArrayList;
import java.util.List;


/**
 * 申请权限的工具
 */
public class JurisdictionUtil {

    private static final int REQUEST_CODE_BLUETOOTH_PERMISSIONS = 100;
    private static PermissionCallback permissionCallback;

    public interface PermissionCallback {
        void onGranted();
        void onDenied();
    }

    /**
     * 请求蓝牙相关权限
     *
     * @param activity         当前 Activity
     * @param callback         权限请求结果回调
     */
    public static void requestBluetoothPermissions(Activity activity, PermissionCallback callback) {
        permissionCallback = callback;
        List<String> permissionsToRequest = new ArrayList<>();

        // 添加位置权限
        permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);

        // Android 12 及以上版本需要添加蓝牙相关权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN);
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE);
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT);
        }

        // 检查是否有未授权的权限
        List<String> permissionsNotGranted = new ArrayList<>();
        for (String permission : permissionsToRequest) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }

        // 如果有未授权的权限，则请求权限
        if (!permissionsNotGranted.isEmpty()) {
            String[] permissionsArray = permissionsNotGranted.toArray(new String[0]);
            ActivityCompat.requestPermissions(activity, permissionsArray, REQUEST_CODE_BLUETOOTH_PERMISSIONS);
        } else {
            // 所有权限已授权
            if (permissionCallback != null) {
                permissionCallback.onGranted();
            }
        }
    }

    /**
     * 处理权限请求结果
     *
     * @param requestCode  请求码
     * @param permissions  请求的权限数组
     * @param grantResults 授权结果数组
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_BLUETOOTH_PERMISSIONS) {
            boolean allGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                // 所有权限已授权
                if (permissionCallback != null) {
                    permissionCallback.onGranted();
                }
            } else {
                // 有权限被拒绝
                if (permissionCallback != null) {
                    permissionCallback.onDenied();
                }
            }
        }
    }

    public static void showToastCenter(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
