package com.dating.lib.local

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import java.util.Locale

data class Language(
    val code: String = "", val language: String = ""
)

object LanguageManager {
    val LOCALE_LANGUAGE
        get() = (ConfigurationCompat.getLocales(Resources.getSystem().configuration)
            .get(0)?.language ?: Locale.getDefault().language ?: "en").lowercase()
            .takeIf { it in supportLanguages.map { it.code } } ?: "en"

    val LANGUAGE_LOCALE: Locale
        get() = Locale(language.code)

    val ENGLISH = Language("en", "English")
    val HI = Language("hi", "हिंदी")
    val AR = Language("ar", "عربي")

    val NONE = Language("", "")

    val supportLanguages by lazy {
        mutableListOf(
            Language("en", "English"), Language("hi", "हिंदी"), Language("ar", "عربي")
        )
    }

    //语言
    var language: Language = NONE
        get() {
            if (field != NONE) return field
            return supportLanguages.find {
                it.code == DataStoreManager.getString("language_code")
                    .ifEmpty { LOCALE_LANGUAGE }
            } ?: ENGLISH
        }
}