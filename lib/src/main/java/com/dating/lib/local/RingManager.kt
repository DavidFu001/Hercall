package com.dating.lib.local

import android.Manifest
import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Vibrator
import androidx.annotation.RequiresPermission
import com.dating.lib.App.Companion.appContext
import com.dating.lib.R

object RingManager {

    private val vibrator: Vibrator? by lazy {
        appContext.getSystemService(
            Context.VIBRATOR_SERVICE
        ) as? Vibrator
    }

    private var isRinging = false

    private var isPlay = false

    private var isLoaded = false

    private val mSoundPool by lazy {
        val abs = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
        SoundPool.Builder().setMaxStreams(1) //设置允许同时播放的流的最大值
            .setAudioAttributes(abs) //完全可以设置为null
            .build().apply {
                setOnLoadCompleteListener { _, _, _ ->
                    isLoaded = true
                    if (isPlay) mStreamId = play(mSoundId, 1f, 1f, 1, -1, 1f) //播放
                }
                mSoundId = load(appContext, R.raw.call_sound, 1)
            }
    }

    private var mStreamId = 0

    private var mSoundId = 0

    /**
     * 播放来电铃声
     */
    fun startRing(vibrate: Boolean = true) {
        if (isRinging) {
            return
        }
        isPlay = true
//        stopRing()
        isRinging = true
        mSoundPool
        if (isLoaded) {
            mStreamId = mSoundPool.play(mSoundId, 1f, 1f, 1, -1, 1f)
        }
        if (vibrate) vibrate(longArrayOf(500L, 1000L, 500L, 1000L), 0)
    }

    /**
     * 停止来电铃声
     */
    fun stopRing() {
        isPlay = false
        try {
            mSoundPool.stop(mStreamId)
            vibrator?.cancel()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            isRinging = false
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(pattern: LongArray?, repeat: Int) {
        vibrator?.vibrate(pattern, repeat)
    }

}