package com.dating.lib

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.http.HttpResponseCache
import android.os.Build
import android.os.Process
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.dating.lib.App.Companion.appContext
import com.dating.lib.im.RongCloudManager
import com.dating.lib.local.ActivityStackManager
import com.dating.lib.local.ReferManager
import com.dating.lib.utils.logAdjust
import com.dating.lib.local.TbaHelper
import com.dating.lib.ui.callback.Api
import com.opensource.svgaplayer.SVGACache
import com.opensource.svgaplayer.SVGAParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.io.File


/**
 * 自定义Application，在这里进行全局的初始化操作。
 *
 * @author davidFu
 */

class App : Application(), CoroutineScope by MainScope() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        if (isMainProcess) {
            initRongIM()
            initSVGA()
            initAdjust()
            initOurApiConfig()
            ReferManager.init()
            TbaHelper.init()
            registerActivityLifecycleCallbacks(ActivityStackManager)
        }
    }

    private fun initOurApiConfig() {
    }

    private fun initRongIM() {
        RongCloudManager.configRYunIM()
    }


    private fun initSVGA() {
        SVGACache.onCreate(appContext, SVGACache.Type.DEFAULT)
        SVGAParser.shareParser().init(this)
        val cacheDir = File(cacheDir, "svgHttp")
        HttpResponseCache.install(cacheDir, 1024 * 1024 * 128)
    }

    private fun initAdjust() {
        val environment = AdjustConfig.ENVIRONMENT_PRODUCTION
        val adjustConfig = AdjustConfig(this, Api.adjustKey, environment)
        adjustConfig.setLogLevel(LogLevel.VERBOSE)
        adjustConfig.setSendInBackground(true)
        adjustConfig.setOnEventTrackingFailedListener { eventSuccessResponseData ->
            logAdjust("adjustConfig: $eventSuccessResponseData")
        }
        adjustConfig.setOnEventTrackingSucceededListener { eventSuccessResponseData ->
            logAdjust("adjustConfig: $eventSuccessResponseData")
        }
        Adjust.onCreate(adjustConfig)
        Adjust.onResume()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: App
    }

    private val Context.isMainProcess get() = packageName.equals(getCurrentProcessName())


    private fun getCurrentProcessName(): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return getProcessName()
        }
        val pid = Process.myPid()
        val am = appContext.getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (processInfo in runningApps) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return null
    }
}


val appScope: CoroutineScope get() = appContext