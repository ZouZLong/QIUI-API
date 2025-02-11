package com.example.openplatform.api.utils;

import android.util.Log;

/**
 * Created by zhy on 15/11/6.
 */
public class L {
    private static final boolean debug = false;

    public static void e(String msg) {
        if (debug) {
            Log.e("OkHttp", msg);
        }
    }

}

