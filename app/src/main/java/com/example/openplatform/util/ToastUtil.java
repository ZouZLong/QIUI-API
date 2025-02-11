package com.example.openplatform.util;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openplatform.MyApplication;
import com.example.openplatform.R;


/**
 * 简单的Toast封装类
 */
public class ToastUtil {

    private static final int msgSize = 10; //字数的长短，字数超过这个长度就长时间显示，否则短时间显示

    @SuppressLint("ShowToast")
    public static void showToastCenter(String msg) {
        try {
            if (msg.length() <= 0) return;
            LogUtil.loge("showToastCenterA: " + msg);
            View view = View.inflate(MyApplication.getAppContext(), R.layout.common_toast, null);
            TextView common_toast_text = view.findViewById(R.id.common_toast_text);
            //APP内部的提示  每次都弹出
            Toast toast = new Toast(MyApplication.getAppContext());
            if (msg.length() >= msgSize) {
                toast.setDuration(Toast.LENGTH_LONG);
            } else {
                toast.setDuration(Toast.LENGTH_SHORT);
            }
            common_toast_text.setText(msg);
            toast.setView(view);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            LogUtil.loge("showToastCenterA 错误：" + e.toString());
        }
    }

}
