package com.dating.lib.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dating.lib.exception.ExceptionConsumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class MviViewModel : ViewModel() {
    private val pageSize = 20

    private val _loadUiIntentFlow: Channel<LoadingView> = Channel()
    val loadUiIntentFlow: Flow<LoadingView> = _loadUiIntentFlow.receiveAsFlow()

    private fun sendLoadingView(loadUiIntent: LoadingView) {
        viewModelScope.launch {
            _loadUiIntentFlow.send(loadUiIntent)
        }
    }

    fun superLaunch(
        error: (suspend (Throwable) -> Unit)? = null,
        showLoading: Boolean = false,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        return viewModelScope.launch {
            if (showLoading) {
                sendLoadingView(LoadingView.Loading(true))
            }
            runCatching {
                block()
            }.onFailure {
                if (error != null) {
                    error.invoke(it)
                } else {
                    ExceptionConsumer.handle(it)
                }
                if (showLoading) {
                    sendLoadingView(LoadingView.Loading(false))
                }
            }.onSuccess {
                if (showLoading) {
                    sendLoadingView(LoadingView.Loading(false))
                }

            }
        }
    }

}

sealed class LoadingView {
    data class Loading(var show: Boolean) : LoadingView()
}