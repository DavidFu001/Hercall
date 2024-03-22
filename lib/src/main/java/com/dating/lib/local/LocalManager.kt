package com.dating.lib.local

import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.os.ConfigurationCompat
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import java.util.Locale

object LocalManager {
    //是否为激进模式
    var isSuperMode = false

    const val SDK_AES_KEY = "xxooSMj4iL1jXXOxzDB26Qbl4KB6EEtA"

    val localeCountry
        get() = (ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)?.country
            ?: Locale.getDefault().country ?: "zz").lowercase()

    val localeLanguage: String
        get() = (ConfigurationCompat.getLocales(Resources.getSystem().configuration)
            .get(0)?.language ?: Locale.getDefault().language ?: "").lowercase()

    val localeDisplayLanguage: String
        get() = (ConfigurationCompat.getLocales(Resources.getSystem().configuration)
            .get(0)?.displayLanguage ?: Locale.getDefault().language ?: "").lowercase()


    val retentionDate: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            val calendar = Calendar.getInstance()
            val end = LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            calendar.timeInMillis = TbaHelper.getFirstInstallTime() * 1000L
            val start = LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            return Period.between(
                start, end
            ).days.let { if (it < 0 || it >= 1000) "error" else "$it" }
        }

    fun isSuperType(): Boolean {
        return isSuperMode
    }

    fun setSuerType(mode: Boolean) {
        isSuperMode = mode
    }

    //是否为欧洲经济区
    fun developedCountry(): Int {
        return when (localeCountry) {
            "se",
            "no",
            "is",
            "si",
            "at",
            "be",
            "bg",
            "hr",
            "cy",
            "cz",
            "dk",
            "ee",
            "fi",
            "fr",
            "lu",
            "mt",
            "nl",
            "pl",
            "pt",
            "ro",
            "sk",
            "ch",
            "es",
            "de",
            "gr",
            "hu",
            "ie",
            "it",
            "lv",
            "lt" -> 1

            else -> 0

        }
    }
}

