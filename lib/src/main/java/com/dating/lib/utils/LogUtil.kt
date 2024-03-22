package com.dating.lib.utils

import android.util.Log
import com.dating.lib.R
import com.dating.lib.extension.string

fun logMessage(msg: String) {
    logD(msg = msg, tag = "RongCloud")
}

fun logParseMessage(msg: String) {
    logD(msg = msg, tag = "ParseRong")
}

fun httpLog(msg: String, exception: Throwable? = null) {
    logD(tag = "LogHttp", msg = msg, exception = exception)
}

fun logConfig(msg: String, exception: Throwable? = null) {
    logD(tag = "superConfig", msg = msg, exception = exception)
}


fun logTba(msg: String, exception: Throwable? = null) {
    logD(tag = "LogTba", msg = msg, exception = exception)
}

fun logPayment(msg: String, exception: Throwable? = null) {
    logD(tag = "GoogleBilling", msg = msg, exception = exception)
}

fun logRefer(msg: String) {
    logD(msg, "LogReferrer")
}

fun logAdjust(msg: String) {
    logD(msg, "Adjust")
}


fun logD(
    msg: String, tag: String = R.string.app_name.string(), exception: Throwable? = null
) {
    Log.d(tag, msg, exception)
}

fun logE(
    tag: String? = R.string.app_name.string(), msg: String, exception: Throwable? = null
) {
    Log.e(tag, msg, exception)
}

fun logAgora(msg: String) {
    logD(msg = msg, tag = "Agora")
}

fun logH5(msg: String) {
    logD(msg = msg, tag = "LogH5")
}