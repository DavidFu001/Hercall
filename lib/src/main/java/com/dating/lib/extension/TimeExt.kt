package com.dating.lib.extension

import android.text.format.DateUtils
import com.dating.lib.App.Companion.appContext
import com.dating.lib.R
import com.dating.lib.utils.logD
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


fun dateFormat(time: Long, pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(time)

fun Long.today(): Boolean = DateUtils.isToday(this)

fun Long.yesterday(): Boolean = (this + DateUtils.DAY_IN_MILLIS).today()

fun Long.currentYear(): Boolean {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    calendar.timeInMillis = this
    return calendar.get(Calendar.YEAR) == currentYear
}

val Long.publicTime: String
    get() {
        if (today()) {
            return SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)
        }
        if (yesterday()) {
            return "${
                SimpleDateFormat(
                    "HH:mm", Locale.getDefault()
                ).format(this)
            } ${appContext.getString(R.string.yesterday)}"
        }
        if (currentYear()) {
            return SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(this)
        }
        return SimpleDateFormat(
            "MM-dd-yyyy HH:mm", Locale.getDefault()
        ).format(this)
    }




fun timeHourMinSecond(value: Long): String {
    if (value <= 0L) {
        return "00:00:00"
    } else {
        val time = StringBuilder()
        // 多少day
        val d = TimeUnit.MILLISECONDS.toDays(value)
        // 多少小时
        val h = TimeUnit.MILLISECONDS.toHours(value - d * 24 * 60 * 60 * 1000)
        // 多少分
        val m =
            TimeUnit.MILLISECONDS.toMinutes(value - (d * 24 * 60 * 60 * 1000) - (h * 60 * 60 * 1000))
        // 多少秒
        val s =
            TimeUnit.MILLISECONDS.toSeconds(value - (d * 24 * 60 * 60 * 1000) - (h * 60 * 60 * 1000) - (m * 60 * 1000))
        if (d > 0) {
            time.append("${d}d:")
        }
        if (h in 1..9) {
            time.append("0${h}:")
        } else if (h >= 10) {
            time.append("${h}:")
        } else {
            time.append("00:")
        }
        if (m in 1..9) {
            time.append("0${m}:")
        } else if (m >= 10) {
            time.append("${m}:")
        } else {
            time.append("00:")
        }
        if (s in 1..9) {
            time.append("0${s}")
        } else if (s >= 10) {
            time.append("$s")
        } else {
            time.append("00")
        }
        return time.toString()
    }
}

/**
 * 00:50 分:秒 的格式
 */
fun timeMinSecond(value: Long): String {
    if (value <= 0L) {
        return "00:00"
    } else {
        val time = StringBuilder()
        // 多少分
        val m = TimeUnit.MILLISECONDS.toMinutes(value)
        // 多少秒
        val s = TimeUnit.MILLISECONDS.toSeconds(value - (m * 60 * 1000))

        if (m in 1..9) {
            time.append("0${m}:")
        } else if (m >= 10) {
            time.append("${m}:")
        } else {//小于1分钟
            time.append("00:")
        }
        if (s in 1..9) {
            time.append("0$s")
        } else if (s >= 10) {
            time.append("$s")
        } else {//小于1秒钟
            time.append("00")
        }
        return time.toString()
    }
}

/**
 * showHMS:是否显示时分秒单位
 */
fun timeConversion(value: Long, showHMS: Boolean = false): String {
    if (value <= 0L) {
        return "00:00:00"
    } else {
        val time = StringBuilder()
        // 多少小时
        val h = TimeUnit.MILLISECONDS.toHours(value)
        // 多少分
        val m = TimeUnit.MILLISECONDS.toMinutes(value - h * 60 * 60 * 1000)
        // 多少秒
        val s = TimeUnit.MILLISECONDS.toSeconds(value - (h * 60 * 60 * 1000) - (m * 60 * 1000))
        if (showHMS) {
            if (h > 0) {
                time.append("${h}h")
            }
            if (m > 0) {
                time.append("${m}m")
            }
            if (s > 0) {
                time.append("${s}s")
            }
            logD(tag = "时间换算", msg = "${h}h${m}m${s}s")
        } else {
            if (h in 1..9) {
                time.append("0${h}:")
            } else if (h >= 10) {
                time.append("${h}:")
            } else {//小于1小时
                time.append("00:")
            }
            if (m in 1..9) {
                time.append("0${m}:")
            } else if (m >= 10) {
                time.append("${m}:")
            } else {//小于1分钟
                time.append("00:")
            }
            if (s in 1..9) {
                time.append("0$s")
            } else if (s >= 10) {
                time.append("$s")
            } else {//小于1秒钟
                time.append("00")
            }
        }
        return time.toString()
    }
}