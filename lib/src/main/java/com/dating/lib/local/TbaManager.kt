package com.dating.lib.local

import android.os.Build
import android.os.Bundle
import android.webkit.WebSettings
import androidx.core.os.bundleOf
import com.android.installreferrer.api.ReferrerDetails
import com.dating.lib.App.Companion.appContext
import com.dating.lib.appScope
import com.dating.lib.local.UserManager.myUid
import com.dating.lib.ui.callback.Api
import com.dating.lib.utils.DeviceUtil
import com.dating.lib.utils.logTba
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

object TbaManager {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    fun uploadInstallEvent(refer: ReferrerDetails) {
        if(true){
            return
        }
        uploadTrackJson("安装") {
            putOpt("silicon", JSONObject().apply {
                putOpt("griffin", "build/${Build.ID}")//系统构建版本，Build.ID， 以 build/ 开头
                putOpt(//webview中的user_agent, 注意为webview的，android中的useragent有;wv关键字
                    "silty",
                    runCatching { WebSettings.getDefaultUserAgent(appContext) }.getOrDefault("")
                )
                putOpt("oregano", 0)//用户是否启用了限制跟踪，0：没有限制，1：限制了；枚举值，映射关系：{“chop”: 0, “verify”: 1}
                putOpt(
                    "bogota",
                    refer.referrerClickTimestampSeconds
                )//引荐来源网址点击事件发生时的客户端时间戳（ referrer_click_timestamp_seconds
                putOpt(
                    "shriek",
                    refer.installBeginTimestampSeconds
                )//应用安装开始时的客户端时间戳 install_begin_timestamp_seconds
                putOpt(
                    "stockade",
                    refer.installBeginTimestampServerSeconds
                )//引荐来源网址点击事件发生时的服务器端时间戳 referrer_click_timestamp_server_seconds
                putOpt("elide", refer.installBeginTimestampSeconds)//应用安装开始时的服务器端时间戳（以秒为单位）
                putOpt("abigail", TbaHelper.getFirstInstallTime())//应用首次安装的时间（以秒为单位）
                putOpt("papal", TbaHelper.getLastUpdateTime())//应用最后一次更新的时间（以秒为单位）
            })
        }
    }

    fun uploadSessionRequest() {
        if(true){
            return
        }
        uploadTrackJson("Session") {
            putOpt("mollie", JSONObject())
        }
    }

    fun uploadGlobalParam() {
        if(true){
            return
        }
        val arrays = mutableListOf<Pair<String, Any>>().apply {
            add("uid" to myUid())
            add("ip_country" to LocalManager.localeCountry)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                add("retention" to LocalManager.retentionDate)
            }
            add("usermode" to if (LocalManager.isSuperType()) "aggressive" else "review")
            add("device" to Build.MANUFACTURER)
            add("ispaid" to if (ReferManager.isPaid) 1 else 0)
            add("pay_times" to ReferManager.payTimes)
            add("total_pay_amount" to ReferManager.totalPayAmount)
            add("userlv" to (UserManager.myLevel()))
        }
        commitGlobalByBundle("intrepid", bundleOf(*arrays.toTypedArray()))
    }

    fun commitTrackByBundle(
        name: String, bundle: Bundle, needPrefix: Boolean = true
    ) {
        uploadTrackJson("打点 $name${
            if (!bundle.isEmpty) " param = ${
                bundle.keySet().map { "$it=${bundle.get(it)}" }
            }" else ""
        }") {
            putOpt("monadic", name)
            bundle.keySet().forEach {
                putOpt(if (needPrefix) "$it^anthem" else it, bundle.get(it))
            }
        }
    }

    private fun commitGlobalByBundle(
        name: String, bundle: Bundle
    ) {
        if(true){
            return
        }
        uploadTrackJson("打点 $name${
            if (!bundle.isEmpty) " param = ${
                bundle.keySet().map { "$it=${bundle.get(it)}" }
            }" else ""
        }") {
            putOpt("monadic", "lupine")
            putOpt(name, JSONObject().apply {
                bundle.keySet().forEach {
                    putOpt(it, bundle.get(it))
                }
            })

        }
    }

    private suspend fun buildCommonParams() = withContext(Dispatchers.IO) {
        JSONObject().apply {
            val commonBody = JSONObject().apply {
                putOpt(
                    "wolfe",
                    "weigh"
                )//操作系统；枚举值，映射关系：{“weigh”: “android”, “ovid”: “ios”, “gentile”: “web”}
                putOpt("rosen", UUID.randomUUID().toString())//日志唯一id，用于排重日志，格式要求标准的uuid
                putOpt("getaway", Build.BRAND)//手机厂商，huawei、oppo
                putOpt("breast", DeviceUtil.androidId)//用户排重字段，统计涉及到的排重用户数就是依据该字段
                putOpt("rex", DeviceUtil.versionName)//应用的版本
                putOpt("trouble", DeviceUtil.androidId)//android App需要有该字段，原值
                putOpt("ballad", System.currentTimeMillis())//日志发生的客户端时间，毫秒数
                putOpt("lascar", Build.MODEL)//手机型号
                putOpt(
                    "helium",
                    TbaHelper.getOperateId()
                )//网络供应商名称，mcc和mnc： https://string.quest/read/2746270
                putOpt(
                    "ponce",
                    LocalManager.localeCountry
                )//String locale = Locale.getDefault(); 拼接为：zh_CN的形式，下杠
                putOpt("ritter", "com.veehub.live")//当前的包名称，a.b.c
                putOpt("meredith", Build.VERSION.RELEASE)//操作系统版本号
            }
            putOpt("deceit", commonBody)
        }
    }

    private fun uploadTrackJson(
        event: String, params: suspend JSONObject.() -> Unit
    ) {
        appScope.launch {
            val param = buildCommonParams()
            param.params()
            tryRequest(event, param)
        }
    }

    private suspend fun tryRequest(
        event: String, param: JSONObject
    ) {
        withContext(Dispatchers.IO) {
            val builder = StringBuilder(event)
            builder.append(" --------> 开始上报\n")
            builder.append("请求参数:\n")
            builder.append("${param.toString().jsonFormat()}\n")
            runCatching {
                doRequest(param.toString().toRequestBody())
            }.onSuccess {
                if (!it.isSuccessful) {
                    builder.append("$event --------> 上报失败")
                    builder.append("\n")
                    builder.append(it.body?.string())
                    builder.append("\n")
                    logTba(builder.toString())
                    retry(event, param)
                    return@withContext
                }
                builder.append("响应:\n")
                builder.append(it.body?.string())
                builder.append("$event --------> 上报成功 ")
                builder.append("\n")
                logTba(builder.toString())
            }.onFailure {
                builder.append(it.message)
                builder.append("\n")
                builder.append("$event --------> 上报失败 ")
                logTba(builder.toString())
                retry(event, param)
            }
        }
    }

    private suspend fun retry(event: String, param: JSONObject) {
        withContext(Dispatchers.IO) {
            delay(10000)
            val builder = StringBuilder(event)
            builder.append(" --------> 开始重试\n")
            builder.append("请求参数:\n")
            builder.append("${param.toString().jsonFormat()}\n")
            runCatching {
                param.putOpt("fuzzy", System.currentTimeMillis())
                doRequest(param.toString().toRequestBody())
            }.onSuccess {
                if (!it.isSuccessful) {
                    builder.append("$event --------> 重试失败")
                    builder.append("\n")
                    builder.append(it.body)
                    builder.append("\n")
                    logTba(builder.toString())
                    return@withContext
                }
                builder.append("响应:\n")
                builder.append(it.body?.string())
                builder.append("$event --------> 重试成功 ")
                logTba(builder.toString())
            }.onFailure {
                builder.append(it.message)
                builder.append("\n")
                builder.append("$event --------> 重试失败, 插入数据库")
                logTba(builder.toString())
            }
        }
    }


    private suspend fun doRequest(body: RequestBody) = withContext(Dispatchers.IO) {
        okHttpClient.newCall(
            Request.Builder().url(Api.API_TBA_Path).post(body).build()
        ).execute()
    }

    private fun String.jsonFormat(): String {
        return runCatching {
            val gson: Gson = GsonBuilder().setPrettyPrinting().create()
            val je = JsonParser.parseString(this)
            gson.toJson(je)
        }.getOrDefault(this)
    }
}

/**
 *tba日志上传
 */
fun tbaPost(event: String, vararg pair: Pair<String, Any> = arrayOf()) {
    if(true){
        return
    }
    TbaManager.commitTrackByBundle(name = event, bundle = bundleOf(*pair), needPrefix = true)
}