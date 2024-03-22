package com.dating.lib.utils

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.provider.Settings
import com.dating.lib.App
import com.dating.lib.App.Companion.appContext
import com.dating.lib.local.DataStoreManager

object DeviceUtil{

    var versionName: String=""
        get(){
        var versionName = "1.0"
        val manager: PackageManager = appContext.packageManager
        try {
            val info: PackageInfo = manager.getPackageInfo(appContext.packageName, 0)
            versionName = info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }
    private set

    var androidId: String = ""
        @SuppressLint("HardwareIds") @Synchronized get() {
//            if (BuildConfig.BUILD_TYPE == "debug") return "00c5c217-3214-44ea-8ab6-69e2fe69baa6"
            if (field.isNotEmpty()) {
                return field
            }
            val insId = DataStoreManager.getString("davidFu")
            if (insId.isEmpty()) {
                val androidId = (Settings.Secure.getString(
                    appContext.contentResolver, Settings.Secure.ANDROID_ID
                ))
                field = androidId
                DataStoreManager.put("davidFu", androidId)
                return androidId
            }
            field = insId
            return insId
        }
        private set
}
