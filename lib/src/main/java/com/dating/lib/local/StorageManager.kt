package com.dating.lib.local

import android.os.Environment
import android.os.StatFs
import com.dating.lib.App.Companion.appContext

object StorageManager {

    fun getSystemStorage(): Long {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            return runCatching {
                val sdcardDir = Environment.getExternalStorageDirectory()
                val sf = StatFs(sdcardDir.path)
                val blockSize = sf.blockSizeLong
                val blockCount = sf.blockCountLong
                val availCount = sf.availableBlocksLong
                val totalDiskSize = blockSize * blockCount //total_size
                val surplusDiskSize = availCount * blockSize //surplus_size
                totalDiskSize - surplusDiskSize
            }.getOrDefault(0)
        } else {
            val sf = StatFs(appContext.cacheDir.absolutePath)
            val blockSize = sf.blockSizeLong
            val blockCount = sf.blockCountLong
            val availCount = sf.availableBlocksLong
            val totalDiskSize = blockSize * blockCount
            val surplusDiskSize = availCount * blockSize
            return totalDiskSize - surplusDiskSize
        }
    }
}