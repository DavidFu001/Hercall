package com.dating.lib.local

class TbaEventValue {

    private var startTime = System.currentTimeMillis()

    fun initStartTime() {
        startTime = System.currentTimeMillis()
    }

    /**
     * 打开app-切到后台后上报一次时间总长，单位秒
     */
    fun getAppDuration(): Long {
        return System.currentTimeMillis() - startTime
    }
}

val tbaValue = TbaHelper.tbaEventValue

