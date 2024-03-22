package com.dating.lib.local

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.dating.lib.bean.UserBean
import com.dating.lib.extension.doOnPause
import com.dating.lib.extension.doOnResume
import com.dating.lib.extension.isResume
import com.dating.lib.extension.superLaunch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.atomic.AtomicLong

class UserStateManager(
    private val lifecycle: Lifecycle, private val getIds: () -> List<Int>
) {
    private var latestRefreshTime = AtomicLong(0L)

    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val requestRunnable = Runnable { doRefreshRequest() }

    private val _onlineState = MutableSharedFlow<List<UserBean>>()
    val onlineState: SharedFlow<List<UserBean>> get() = _onlineState

    init {
        lifecycle.doOnResume {
            lifecycle.coroutineScope.superLaunch {
                delay(maxOf(0, 10000 - (System.currentTimeMillis() - latestRefreshTime.get())))
                if (!lifecycle.isResume) return@superLaunch
                doRefreshRequest()
            }
        }
        lifecycle.doOnPause {
            handler.removeCallbacksAndMessages(null)
        }
    }

    private fun doRefreshRequest() {
        val ids = getIds()
        if (ids.isEmpty()) {
            handler.postDelayed(requestRunnable, 10000)
            return
        }
        lifecycle.coroutineScope.superLaunch {
//            val resultMap = apiService.refreshState(UserStateRequestBody(ids)).data
//            if (resultMap != null) {
//                _onlineState.emit(resultMap)
//            }
            latestRefreshTime.set(System.currentTimeMillis())
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(requestRunnable, 10000)
        }
    }
}