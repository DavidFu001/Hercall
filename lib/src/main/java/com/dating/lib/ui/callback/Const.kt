package com.dating.lib.ui.callback

import com.dating.lib.BuildConfig

/**
 * 项目所有全局通用常量的管理类。
 *
 * @author davidFu
 * @since  2023/5/31
 */
interface Const {

    interface ExtraParam {
        companion object {
            const val uid: String = "uid"
            const val user: String = "user"
            const val list: String = "list"
            const val index: String = "index"
            const val isReport: String = "isReport"
            const val requestVideo: String = "requestVideo"
            const val isICall: String = "isICall"
            const val from: String = "from"
        }
    }


    interface HttpCode {

        companion object {
            const val SUCCESS = 22760 // 成功
            const val USER_HAS_EXIST = 34296 // 用户已存在 USER_HAS_EXIST
            const val INSUFFICIENT_BALANCE = 13862 // 余额不足 INSUFFICIENT_BALANCE
            const val INVALID_TOKEN = 18995 // token过期或者无效
            const val SOURCE_NOT_IN_RULE = 39288 //头像（资源）不合法
            const val USER_STATUS_BUSY = 12478//用户忙
        }
    }

    companion object {
        //文本消息
        const val RC_TXT_MSG = "RC:TxtMsg"

        //扩展通知消息
        const val RC_INFO_NTF = "RC:InfoNtf"

        //图片消息
        const val RC_IMG_MSG = "RC:ImgMsg"

        //语音消息
        const val RC_VC_MSG = "RC:VcMsg"

        //高清语音消息
        const val RC_HQ_VC_MSG = "RC:HQVCMsg"

        //客户id
        const val Customer_Service_Id = 10001
    }

}

fun Int.isServiceAccount(): Boolean {
    return Const.Customer_Service_Id == this
}

interface Api {
    companion object {
        val API_HOST_Path: String
            get() = if (BuildConfig.DEBUG) "https://test.hercallstyle.com" else "https://prod.hercallstyle.com"

        val API_TBA_Path: String
            get() = if (BuildConfig.DEBUG) "https://test-augustus.hercallstyle.com/privy/foci" else "https://augustus.hercallstyle.com/cramer/hedonism/begot"

        val API_CLOAK_Path: String
            get() = "https://bank.hercallstyle.com/flu/vie/besmirch"

        val translateKey = "AIzaSyA2sKSx6WS5tt9Ix7OBxRyqbXHaKbPpi6M"
        val adjustKey = "uv8bnetid24g"
        val rongKey = if (BuildConfig.DEBUG) "bmdehs6pbel8s" else "pkfcgjstpc1i8"
        val agoraKey =
            if (BuildConfig.DEBUG) "970425bcbd1a4ea3a96ca20f12e56163" else "abf21bdf80754dab82214b6840ec5924"
        val clockSuperValue = "headmen"
    }
}