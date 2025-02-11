package com.example.openplatform.util;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;


/**
 * 蓝牙工具类
 */
public class BlueToothUtil {

    private static BlueToothUtil blueToothUtil;
    private static final BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();

    public static BlueToothUtil getInstance() {//获取单例
        if (blueToothUtil == null) {
            synchronized (BlueToothUtil.class) {
                if (blueToothUtil == null) {
                    blueToothUtil = new BlueToothUtil();
                }
            }
        }
        return blueToothUtil;
    }

    public BluetoothAdapter getBlueadapter() {
        return blueadapter;
    }

    public boolean orBlue(Context context) {
        if (blueadapter != null) {//支持蓝牙模块
            return blueadapter.isEnabled();//true 已经打开 false 没有打开/不支持蓝牙
        } else {//不支持蓝牙模块
            ToastUtil.showToastCenter("不支持蓝牙模块");
            return false; //打开
        }
    }

    /**
     * 强制开启当前 Android 设备的 Bluetooth
     *
     * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
     */
    public void turnOnBluetooth(Context context) {
        /*try {
            if (blueadapter != null) {
                //enable()返回值 : true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
                blueadapter.enable();//有的手机会有问题
            }
        } catch (Exception e) {
            LogUtil.loge("开启蓝牙出现了错误");
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enabler, 0);
        }*/
        Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ToastUtil.showToastCenter("turnOnBluetooth 错误");
            return;
        }
        ((Activity) context).startActivityForResult(enabler, 0);
    }
}
