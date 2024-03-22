package com.dating.lib.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

object StatusBarUil {

    /**
     * 设置状态栏透明
     */
    fun setWindowStatusTransparent(window: Window?) {
        window?.let {
            // 设置沉浸式
            WindowCompat.setDecorFitsSystemWindows(window, false)
            // 设置状态栏底色
            window.statusBarColor = Color.TRANSPARENT
            // 设置字体颜色
            ViewCompat.getWindowInsetsController(window.decorView)?.let { controller ->
                controller.show(WindowInsetsCompat.Type.statusBars()) // 显示状态栏
                controller.isAppearanceLightStatusBars = false //true字体黑色,false白色
            }
            // 适配异形屏幕，让内容延伸至异形屏下面
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }
    }

    /**
     * navigationBar设置透明色
     */
    fun Activity.transparentNaviBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = Color.TRANSPARENT
    }
}