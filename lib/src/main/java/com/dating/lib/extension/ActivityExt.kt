package com.dating.lib.extension

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.dating.lib.R

inline fun <reified T> Activity.open(
    vararg args: Pair<String, Any> = arrayOf()
) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundleOf(*args))
    startActivity(intent)
}

inline fun <reified T> Context.open(vararg args: Pair<String, Any> = arrayOf()) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundleOf(*args))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

inline fun <reified T> Fragment.open(vararg args: Pair<String, Any> = arrayOf()) {
    val intent = Intent(context, T::class.java)
    intent.putExtras(bundleOf(*args))
    startActivity(intent)
}


inline fun <reified T> Fragment.openPageClearTask(vararg args: Pair<String, Any> = arrayOf()) {
    val intent = Intent(context, T::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.putExtras(bundleOf(*args))
    startActivity(intent)
}

fun Context.openBrowser(url: String) {
    runCatching {
        val uri = url.toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}

fun Fragment.openBrowser(url: String) {
    runCatching {
        val uri = url.toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}

fun Context.copy(text: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("copy_label", text)
    clipboardManager.setPrimaryClip(clip)
    R.string.copy_success.string(this).toast()
}