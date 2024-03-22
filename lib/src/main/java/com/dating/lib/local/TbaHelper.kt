package com.dating.lib.local

import android.annotation.SuppressLint
import android.os.BatteryManager
import android.os.SystemClock
import android.telephony.TelephonyManager
import com.dating.lib.App.Companion.appContext
import com.dating.lib.appScope
import com.dating.lib.extension.superLaunch
import com.dating.lib.utils.DeviceUtil
import com.dating.lib.utils.logTba
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

@SuppressLint("StaticFieldLeak")
object TbaHelper {
    val tbaEventValue = TbaEventValue()
    private const val IP_REGEX =
        "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))"

    fun init() {
        TbaManager.uploadSessionRequest()
        queryIpCountry()
    }

    private val isIPLoading = AtomicBoolean(false)
    private var latestIpTime: AtomicLong = AtomicLong(0L)

    private var cachedIp: String = ""
        get() {
            if (field.isEmpty()) {
                updateNetworkIP()
            }
            return field
        }

    private fun updateNetworkIP() {
        if (isIPLoading.get()) return
        if (latestIpTime.get() > 0 && SystemClock.elapsedRealtime() - latestIpTime.get() < 600_000) return//十分钟之内不再请求ip
        appScope.superLaunch {
            withContext(Dispatchers.IO) {
                isIPLoading.set(true)
                val client = OkHttpClient.Builder().writeTimeout(10L, TimeUnit.SECONDS)
                    .readTimeout(10L, TimeUnit.SECONDS).build()
                val request: Request = Request.Builder().url("https://ifconfig.me/ip").build()
                val response = runCatching {
                    client.newCall(request).execute()
                }.getOrDefault(null)
                if (response?.isSuccessful == true) {
                    latestIpTime.set(SystemClock.elapsedRealtime())
                    return@withContext response.body?.string()?.apply {
                        logTba("获取IP成功 $this")
                    } ?: cachedIp
                }
                logTba("获取IP失败")
                return@withContext cachedIp
            }.apply {
                if (!isNullOrEmpty()) {
                    if (Regex(IP_REGEX).matches(this)) {
                        cachedIp = this
                    }
                }
                isIPLoading.set(false)
            }
        }
    }

    private fun queryIpCountry() {
        if (ReferManager.ipCountry.isNotEmpty()) return
        appScope.launch {
            withContext(Dispatchers.IO) {
                val client = OkHttpClient.Builder().writeTimeout(10L, TimeUnit.SECONDS)
                    .readTimeout(10L, TimeUnit.SECONDS).build()
                val request: Request = Request.Builder().url("https://api.myip.com/").build()
                val response = runCatching {
                    client.newCall(request).execute()
                }.getOrDefault(null)
                if (response?.isSuccessful == true) {
                    val json = response.body?.string()?.let { JSONObject(it) }
                    val countryCode = json?.optString("cc")
                    if (countryCode.isNullOrEmpty()) return@withContext
                    ReferManager.ipCountry = countryCode
                    logTba("获取IP国家成功 $countryCode")
                } else {
                    logTba("获取IP国家失败")
                }
            }
        }
    }

    fun getOperateId(): String {
        val operator = appContext.getSystemService(TelephonyManager::class.java).networkOperator
        /**通过operator获取 MCC 和MNC */
        return runCatching {
            val mcc = operator.substring(0, 3).toInt()
            val mnc = operator.substring(3).toInt()
            "$mcc$mnc"
        }.getOrDefault("")
    }


    @SuppressLint("HardwareIds")
    @Synchronized
    fun getAndroidID(): String {
        return DeviceUtil.androidId
    }

    fun getStorageSize(): Long {
        return (StorageManager.getSystemStorage().toDouble() / 1048576).toLong()
    }

    fun getGoogleAdId(): String {
        return ""
    }

    fun getBatteryPercent(): Float {
        val manager = appContext.getSystemService(BatteryManager::class.java)
        return (manager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)?.toFloat()
            ?: 1f) / 100f ///当前电量百分比
    }


    fun getFirstInstallTime(): Long {
        val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
        return packageInfo.firstInstallTime / 1000L
    }

    fun getLastUpdateTime(): Long {
        val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
        return packageInfo.lastUpdateTime / 1000L
    }
}