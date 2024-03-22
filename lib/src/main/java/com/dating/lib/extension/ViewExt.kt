package com.dating.lib.extension

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.LayoutDirection
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dating.lib.App.Companion.appContext
import com.dating.lib.local.ActivityStackManager
import com.dating.lib.local.LanguageManager.LANGUAGE_LOCALE
import com.gyf.immersionbar.ImmersionBar

/**
 * 显示view
 */
fun View?.visible(bool: Boolean = true) {
    this?.visibility = if (bool) View.VISIBLE else View.GONE
}


/**
 * 隐藏view
 */
fun View?.gone(bool: Boolean = true) {
    this?.visibility = if (bool) View.GONE else View.VISIBLE
}

/**
 * 占位隐藏view
 */
fun View?.invisible(bool: Boolean = true) {
    this?.visibility = if (bool) View.INVISIBLE else View.VISIBLE
}

var lastClickTime: Long = 0
const val SPACE_TIME: Long = 500L

fun View.click(onClick: (v: View) -> Unit) {
    this.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > SPACE_TIME) {
            lastClickTime = System.currentTimeMillis()
            onClick(this)
        }
    }
}


fun List<View>.click(onClick: (v: View) -> Unit) {
    forEach {
        it.click(onClick)
    }
}


fun Int.color(): Int {
    return ContextCompat.getColor(appContext, this)
}

fun Int.drawable(): Drawable? {
    return ContextCompat.getDrawable(appContext, this)
}

fun Int.string(context: Context = ActivityStackManager.topActivity ?: appContext): String {
    return context.getString(this)
}

/**
 * 根据手机的分辨率将dp转成为px。
 */
fun Int.dp2px(): Int {
    val scale = appContext.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun String.toast(context: Context = appContext) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun isR2L(): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(LANGUAGE_LOCALE) == LayoutDirection.RTL
}

//从父控件中移出
fun View.removeFromParent() {
    (this.parent as? ViewGroup)?.removeView(this)
}

fun ViewGroup.setOnApplyWindowInsets(window: Window) {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
        val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
        val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        val layoutParams = this.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.bottomMargin = if (imeVisible) {
            imeHeight + 16
        } else {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics)
                .toInt()
        }
        this.layoutParams = layoutParams
        insets
    }
}

// 状态栏扩展
val Activity.statusBarHeight get() = ImmersionBar.getStatusBarHeight(this)
val Fragment.statusBarHeight get() = ImmersionBar.getStatusBarHeight(this)