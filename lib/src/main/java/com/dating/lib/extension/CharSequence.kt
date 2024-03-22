

package com.dating.lib.extension

import android.util.Base64
import android.widget.Toast
import com.dating.lib.App
import okio.Buffer
import java.io.EOFException
import java.net.URLEncoder
import java.security.MessageDigest
import kotlin.experimental.and

/**
 * 弹出Toast提示。
 *
 * @param duration 显示消息的时间  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
 */
fun CharSequence.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.appContext, this, duration).show()
}

val String.decode64 get() = Base64.decode(this, Base64.NO_WRAP).decodeToString()

fun String.md5(): String {
    val hexDigits = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    )
    val digest = MessageDigest.getInstance("MD5")
    digest.update(toByteArray())
    val bytes = digest.digest() ?: return ""
    val len = bytes.size
    if (len <= 0) return ""
    val ret = CharArray(len shl 1)

    var i = 0
    var j = 0
    while (i < len) {
        ret[j++] = hexDigits[bytes[i].toInt() shr 4 and 0x0f]
        ret[j++] = hexDigits[(bytes[i] and 0x0f).toInt()]
        i++
    }
    return String(ret)
}

fun String.encodeUtf8(): String? {
   return URLEncoder.encode(this, "utf-8");
}


internal fun Buffer.isProbablyUtf8(): Boolean {
    try {
        val prefix = Buffer()
        val byteCount = size.coerceAtMost(64)
        copyTo(prefix, 0, byteCount)
        for (i in 0 until 16) {
            if (prefix.exhausted()) {
                break
            }
            val codePoint = prefix.readUtf8CodePoint()
            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                return false
            }
        }
        return true
    } catch (_: EOFException) {
        return false // Truncated UTF-8 sequence.
    }
}