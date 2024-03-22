package com.dating.lib.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.dating.lib.exception.ExceptionConsumer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun CoroutineScope.superLaunch(
    error: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch {
        runCatching {
            block()
        }.onFailure {
            if (error != null) {
                error.invoke(it)
            } else {
                ExceptionConsumer.handle(it)
            }
        }
    }
}

fun LifecycleOwner.superLaunch(
    error: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return lifecycleScope.superLaunch(error, block)
}

/**
 * 制定协程作用域，以及处理异常情况
 */
fun CoroutineScope.superLaunchWithDispatch(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    error: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(dispatcher) {
        runCatching {
            block()
        }.onFailure {
            if (error != null) {
                error.invoke(it)
            } else {
                ExceptionConsumer.handle(it)
            }
        }
    }
}

fun CoroutineScope.launchLoop(
    delay: Long,
    interval: Long,
    callback: suspend CoroutineScope.(Int) -> Unit
): Job {
    var duration = 0
    return superLaunch {
        delay(delay)
        while (true) {
            callback.invoke(this, duration)
            duration++
            delay(interval)
        }
    }
}

