package com.dating.lib.net.interceptor

import android.util.Base64
import com.dating.lib.ui.callback.GsonTypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.lang.reflect.Type
import java.nio.charset.Charset

internal class ConverterFactory private constructor() : Converter.Factory() {

    private val gson: Gson = GsonBuilder().registerTypeAdapterFactory(
        GsonTypeAdapterFactory()
    ).create()

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return DecodeResponseBodyConverter(adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson, adapter)
    }

    internal class GsonRequestBodyConverter<T> internal constructor(
        private val gson: Gson,
        private val adapter: TypeAdapter<T>
    ) : Converter<T, RequestBody> {
        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            val buffer = Buffer()
            val writer: Writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value)
            jsonWriter.close()
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
        }

        companion object {
            private val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaType()
            private val UTF_8 = Charset.forName("UTF-8")
        }
    }


    internal class DecodeResponseBodyConverter<T>(private val adapter: TypeAdapter<T>) :
        Converter<ResponseBody, T> {
        @Throws(IOException::class)
        override fun convert(responseBody: ResponseBody): T {
            val originData = responseBody.string()
            return adapter.fromJson(originData)
        }
    }


    companion object {
        @JvmOverloads
        fun create(): ConverterFactory {
            return ConverterFactory()
        }
    }
}

/**
 * 将后端返回的数据进行解密
 */
fun decode(originData: String): String {
    val sb: StringBuilder = StringBuilder()
    var result = ""
    runCatching {
        if (originData.isNotEmpty()) {
            val lastInt = originData.last().toString().toInt()
            val firstTime = originData.substring(lastInt, originData.length - 1)
            val top2 = firstTime.substring(0, 2)
            val end2 = firstTime.substring(firstTime.length - 2, firstTime.length)
            val center = firstTime.substring(2, firstTime.length - 2)
            result = sb.append(end2).append(center).append(top2).toString().decodeData()
        }
    }

    return result
}

fun String.decodeData(): String = String(Base64.decode(this, 0))
