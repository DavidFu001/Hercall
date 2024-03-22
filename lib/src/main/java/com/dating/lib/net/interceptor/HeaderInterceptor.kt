package com.dating.lib.net.interceptor

import com.dating.lib.local.LocalManager
import com.dating.lib.local.UserManager
import com.dating.lib.utils.DeviceUtil
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().apply {
            header("TUPO", LocalManager.localeCountry)//国家
            header("UJTF", DeviceUtil.versionName)//版本号
            header("CMQT", DeviceUtil.androidId)//设备id
            header("CQBY", UserManager.getAuth())// token
            header("GJZZ", "com.hercall.style")//包名
            header("KILH", if (LocalManager.isSuperType()) "0" else "1")//是否为激进模式, 激进模式传递0
        }.build()
        return chain.proceed(request)
    }
}