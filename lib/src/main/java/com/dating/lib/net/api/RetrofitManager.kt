package com.dating.lib.net.api

import com.dating.lib.net.api.RetrofitManager.okHttpClient
import com.dating.lib.net.interceptor.ConverterFactory
import com.dating.lib.net.interceptor.HeaderInterceptor
import com.dating.lib.net.interceptor.HttpLoggingInterceptor
import com.dating.lib.utils.httpLog
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


object RetrofitManager {
    val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder().retryOnConnectionFailure(false)
            .connectTimeout(30_000L, TimeUnit.MILLISECONDS)
            .readTimeout(30_000, TimeUnit.MILLISECONDS).writeTimeout(30_000, TimeUnit.MILLISECONDS)

        builder.build()
    }

    @JvmStatic
    @JvmOverloads
    fun createRetrofit(baseUrl: String, client: OkHttpClient = okHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(ConverterFactory.create())
            .build()
    }
}


inline fun <reified T> createApi(baseUrl: String): T {
    return RetrofitManager.createRetrofit(baseUrl, okHttpClient.let { client ->
        val newBuilder = client.newBuilder()
        val loggingInterceptor = HttpLoggingInterceptor { message -> //打印retrofit日志
            httpLog(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        newBuilder.addInterceptor(HeaderInterceptor())
        newBuilder.addInterceptor(loggingInterceptor)
        newBuilder.build()
    }).create(T::class.java)
}