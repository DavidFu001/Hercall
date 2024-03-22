package com.dating.lib.exception

import com.dating.lib.R
import com.dating.lib.utils.logE
import kotlinx.coroutines.CancellationException


object ExceptionConsumer {

    private val TAG: String by lazy {
        "${R.string.app_name}_${javaClass.simpleName}:"
    }

    fun handle(error: Throwable) {
        error.printStackTrace()
        when (error) {
            is TokenExpiresException -> {}
            is CancellationException -> throw error
            is ApiException -> handleApiException(error)
            else -> logE(TAG, "Handle other exception: ${error.message}")
        }
    }

    private fun handleApiException(error: ApiException) {
        logE(TAG, "Handle api exception: ${error.code} -> ${error.message}")
    }
}