package com.dating.lib.im

import com.dating.lib.App.Companion.appContext
import com.dating.lib.local.UserManager.getImToken
import com.dating.lib.ui.callback.Api.Companion.rongKey
import com.dating.lib.utils.logD
import com.dating.lib.utils.logMessage
import io.rong.imlib.IRongCoreCallback
import io.rong.imlib.IRongCoreEnum
import io.rong.imlib.RongCoreClient
import io.rong.imlib.listener.OnReceiveMessageWrapperListener
import io.rong.imlib.model.InitOption
import io.rong.imlib.model.Message
import io.rong.imlib.model.ReceivedProfile


object RongCloudManager {

    fun configRYunIM() {
        val initOption = InitOption.Builder()
            .build()

        RongCoreClient.init(appContext, rongKey, initOption)
        logMessage("初始化完毕...")
        RongCoreClient.addOnReceiveMessageListener(onReceiveMessage)
    }

    fun connectIMClient(connectOk: () -> Unit, retryCount: Int = 3) {
        var retryMaxTime = retryCount
        getImToken().let { token ->
            if (token.isEmpty()) {
                return
            }
            if (retryCount < 1) {//如果超过了重试次数，那么就退出链接逻辑
                return
            }
            retryMaxTime--
            logMessage("开始连接融云服务")
            RongCoreClient.connect(token, object : IRongCoreCallback.ConnectCallback() {
                /**
                 * 成功回调
                 * @param userId 当前用户 ID
                 */
                override fun onSuccess(userId: String) {
                    logMessage("链接成功...")
                    connectOk.invoke()
                }

                /**
                 * 错误回调
                 * @param errorCode 错误码
                 */
                override fun onError(errorCode: IRongCoreEnum.ConnectionErrorCode) {

                    when (errorCode) {
                        IRongCoreEnum.ConnectionErrorCode.RC_CONNECT_TIMEOUT -> {
                            logMessage("链接超时")
                            connectIMClient(connectOk)
                        }

                        IRongCoreEnum.ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT -> {
                            logMessage("Token 错误...")
                        }

                        IRongCoreEnum.ConnectionErrorCode.RC_CLIENT_NOT_INIT -> {
                            configRYunIM()
                            connectIMClient(connectOk)
                        }

                        IRongCoreEnum.ConnectionErrorCode.RC_CONNECTION_EXIST -> {
                            logMessage("之前已经链接过了")
                            connectOk.invoke()
                        }

                        else -> {
                            logD("连接融云失败, 错误码: $errorCode = ${errorCode.value}")
                        }
                    }
                }

                /**
                 * 数据库回调.
                 * @param code 数据库打开状态. DATABASE_OPEN_SUCCESS 数据库打开成功; DATABASE_OPEN_ERROR 数据库打开失败
                 */
                override fun onDatabaseOpened(code: IRongCoreEnum.DatabaseOpenStatus?) {
                    code?.let {
                        when (it) {
                            IRongCoreEnum.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS -> {
                                logMessage("本地数据库打开成功")
                            }

                            else -> {
                                logMessage("本地数据库打开失败")
                            }
                        }
                    }
                }
            })
        }
    }

    fun logOutRongCloud() {
        RongCoreClient.getInstance().logout()
    }

    private val onReceiveMessage by lazy {
        object : OnReceiveMessageWrapperListener() {
            override fun onReceivedMessage(message: Message?, profile: ReceivedProfile?) {

            }
        }
    }
}