package com.dating.lib.local

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.dating.lib.local.UserManager.myUid
import java.util.LinkedList
import kotlin.reflect.KClass

object ActivityStackManager : Application.ActivityLifecycleCallbacks {

    private val activities = LinkedList<Activity>()

    private val activityCount get() = activities.size

    val topActivity: Activity? get() = activities.peekLast()

    private var isForeground = false

    //打开的Activity数量统计
    private var activityStartCount = 0

    /**
     *当前app是否在前台
     */
    fun isForeground() = isForeground

    fun contains(clazz: KClass<*>): Boolean {
        return activities.any { it.javaClass.name == clazz.java.name }
    }

    fun contains(clazz: Class<*>): Boolean {
        return activities.any { it.javaClass.name == clazz.name }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activities.add(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        activityStartCount++
        isForeground = true
        if (activityStartCount == 1) {//后台回到前台
            tbaValue.initStartTime()
            if (myUid() != 0) {//热启动也要打点
                tbaPost(TbaEventName.startApp)
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }


    override fun onActivityStopped(activity: Activity) {
        activityStartCount--
        if (activityStartCount == 0) {//数值从1到0说明是从前台切到后台
            isForeground = false
            tbaPost(TbaEventName.exitApp, "stay" to (tbaValue.getAppDuration() / 1000))
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        activities.remove(activity)
    }

    fun finish(clazz: Class<out Activity>) {
        runCatching {
            activities.find { it.javaClass.name == clazz.name }?.finish()
        }
    }

    fun finishAll() {
        for (i in activities.indices) {
            activities.pollLast()?.finish()
        }
    }


}

val KClass<out Activity>.isExist get() = ActivityStackManager.contains(this)

fun Class<out Activity>.finish() {
    ActivityStackManager.finish(this)
}