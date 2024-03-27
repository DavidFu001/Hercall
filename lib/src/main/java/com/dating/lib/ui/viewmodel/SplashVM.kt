package com.dating.lib.ui.viewmodel

import com.dating.lib.extension.dateFormat
import com.dating.lib.im.RongCloudManager
import com.dating.lib.local.ReferManager
import com.dating.lib.local.TbaEventName
import com.dating.lib.local.TbaManager
import com.dating.lib.local.UserManager.saveUser
import com.dating.lib.local.tbaPost
import com.dating.lib.net.api.apiService
import com.dating.lib.utils.DeviceUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SplashVM : MviViewModel() {
    private val _jump2MainFlow = MutableSharedFlow<Int>()
    val jump2MainFlow: SharedFlow<Int> get() = _jump2MainFlow


    fun start(needLoading: Boolean = true) {
        superLaunch(showLoading = needLoading) {
            apiService.hasRegister().apply {
                data?.let {
                    if (it.hasRegister) {
                        it.saveUser()
                        RongCloudManager.connectIMClient(connectOk = {
                            superLaunch {
                                _jump2MainFlow.emit(1)
                            }
                        })
                    } else {
                        doRegister()
                    }
                }
            }
        }
    }

    private fun doRegister() {
        val map = mutableMapOf<String, String>().apply {
            put("prefefice", DeviceUtil.androidId)
            put("pyawer", "United States")
            put("pest", "1")
            put("beke", "18")
        }
        superLaunch(showLoading = true) {
            apiService.userRegister(map).apply {
                data?.let {
                    it.saveUser()
                    RongCloudManager.connectIMClient(connectOk = {
                        superLaunch {
                            _jump2MainFlow.emit(1)
                        }
                    })
                    tbaPost(
                        TbaEventName.registerTime,
                        "istm" to dateFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm")
                    )
                    //每次注册成功，需要将付费refer清零
                    updateReferForBill()
                }
            }
        }
    }

    private fun updateReferForBill() {
        //付费的refer清除
        ReferManager.isPaid = false
        ReferManager.payTimes = 0
        ReferManager.totalPayAmount = 0.0F
        TbaManager.uploadGlobalParam()
    }
}

/**
 * 等待一些配置信息
 */
//suspend fun CoroutineScope.waitxxxConfig() {
//    val configJob = async {
//        withTimeoutOrNull(10500) {
//            ConfigRepo.continueConfigJob()
//        }
//    }
//
//    val cloakJob = async {
//        withTimeoutOrNull(10500) {
//            ConfigRepo.continueCloakJob()
//        }
//    }
//
//    val activityJob = async {
//        withTimeoutOrNull(10500) {
//            ConfigRepo.continueActivityConfigJob()
//        }
//    }
//    configJob.await()
//    cloakJob.await()
//    activityJob.await()
//    LocalManager.setSuerType(ConfigRepo.getSuperType())
//}