package com.dating.lib.local

import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.adjust.sdk.AdjustThirdPartySharing
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.dating.lib.App.Companion.appContext
import com.dating.lib.appScope
import com.dating.lib.extension.superLaunch
import com.dating.lib.utils.logRefer
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume



object ReferManager : DataStoreOwner {
    private var referred by dataBool(false)
    var ipCountry: String by dataString("")

    //是否是付费用户
    var isPaid: Boolean by dataBool(false)

    //支付成功次数
    var payTimes: Int by dataInt()

    //用户充值过的总金额$
    var totalPayAmount: Float by dataFloat(0f)

    fun init() {
        if (referred) {
            return
        }
        appScope.superLaunch {
            var result: ReferrerDetails? = null
            for (i in 0..3) {
                result = connect()
                if (result != null) break
                delay(2000)
            }
            referred = true
            if (result != null) {
                TbaManager.uploadInstallEvent(result)
            }

            //adjust安装事件的上报
            val adjustEvent = AdjustEvent("x6q3b7")
            Adjust.trackEvent(adjustEvent)

            val adjustThirdPartySharing = AdjustThirdPartySharing(null)
            adjustThirdPartySharing.addGranularOption(
                "google_dma",
                "eea",
                "${LocalManager.developedCountry()}"
            )
            adjustThirdPartySharing.addGranularOption("google_dma", "ad_personalization", "1")
            adjustThirdPartySharing.addGranularOption("google_dma", "ad_user_data", "1")
            Adjust.trackThirdPartySharing(adjustThirdPartySharing)
        }
    }

    private suspend fun connect() = withTimeout(10000) {
        return@withTimeout suspendCancellableCoroutine {
            val client = InstallReferrerClient.newBuilder(appContext).build()
            client.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            logRefer("Refer获取成功 --> ${client.installReferrer.installReferrer}")
                            runCatching {
                                onEnd(client.installReferrer)
                            }
                        }

                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                            logRefer("Refer获取失败，当前设备不支持")
                            onEnd(null)
                        }

                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                            logRefer("Refer获取失败")
                            onEnd(null)
                        }
                    }
                }

                fun onEnd(refer: ReferrerDetails?) {
                    runCatching {
                        if (it.isActive) {
                            it.resume(refer)
                        }
                    }
                    runCatching {
                        client.endConnection()
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    onEnd(null)
                }
            })
        }
    }
}