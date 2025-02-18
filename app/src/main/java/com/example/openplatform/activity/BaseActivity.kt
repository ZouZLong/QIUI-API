package com.example.openplatform.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.example.openplatform.util.StatusBarUtils

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    fun setNavigationBar(type: Int) { //设置导航栏  0为透明底+黑色字体  1为透明底+白色字体
        StatusBarUtils.with(this).init()
        if (type == 0) { //修改标题栏的字体为黑色  版本大于 M=23=android 6.0
            val decor = window.decorView
            // ... existing code ...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val controller = window.insetsController
                controller?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}