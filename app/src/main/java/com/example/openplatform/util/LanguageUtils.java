package com.example.openplatform.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;


/**
 * 切换语言工具列
 */

public class LanguageUtils {

    //修改语言
    public static void changeAppLanguage(Context context) {
        try {
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = Locale.CHINA;//设置为简体
            res.updateConfiguration(conf, dm);
        } catch (Exception e) {
            LogUtil.loge("修改语言 错误：" + e);
        }

    }
}
