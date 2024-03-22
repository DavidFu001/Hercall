package com.dating.lib.extension

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

fun Lifecycle.doOnDestroy(onDestroy: () -> Unit) {
    addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            onDestroy.invoke()
        }
    })
}

fun Lifecycle.doOnResume(onResume: () -> Unit) {
    addObserver(object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            onResume.invoke()
        }
    })
}

fun Lifecycle.doOnPause(onPause: () -> Unit) {
    addObserver(object : DefaultLifecycleObserver {
        override fun onPause(owner: LifecycleOwner) {
            onPause.invoke()
        }
    })
}

val Lifecycle.isResume get() = currentState.isAtLeast(Lifecycle.State.RESUMED)
val Lifecycle.isStarted get() = currentState.isAtLeast(Lifecycle.State.STARTED)