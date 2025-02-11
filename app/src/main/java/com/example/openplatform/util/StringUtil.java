package com.example.openplatform.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;


public class StringUtil {

    /**
     * 将蓝牙返回的数据 进行拆分 每两个数据后有一个空格
     */
    public static String setBluetoothSeparate(String src) {
        int length = 2;
        int n = (src.length() + length - 1) / length; //获取整个字符串可以被切割成字符子串的个数
        String[] split = new String[n];
        for (int i = 0; i < n; i++) {
            if (i < (n - 1)) {
                split[i] = src.substring(i * length, (i + 1) * length);
            } else {
                split[i] = src.substring(i * length);
            }
        }
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            content.append(split[i]).append("(").append(i).append(")").append(" ");
        }
        return content.toString();
    }


    /**
     * 将byte转换为16进制字符串
     */
    public static String byteToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }
            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * 字符串转换为字节
     */
    public static byte[] hexStr2Bytes(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }

    //字符串转换为十六进制字符串
    public static String hexStr2String(String str) {
        byte[] bytes = str.getBytes(); // 将字符串转换为字节数组

        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF); // 将字节转换为十六进制字符串
            if (hex.length() == 1) {
                hexBuilder.append("0"); // 如果十六进制字符长度为1，添加前导零
            }
            hexBuilder.append(hex);
        }

        return hexBuilder.toString(); // 获取最终的十六进制字符串
    }



}
