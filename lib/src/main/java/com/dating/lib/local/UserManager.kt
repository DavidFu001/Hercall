package com.dating.lib.local

import com.dating.lib.bean.UserBean


object UserManager {
    fun myUid() = userInfo?.uid ?: 0
    fun myAvatar() = userInfo?.avatar ?: ""
    fun myNickName() = userInfo?.nickname
    fun myCountry() = userInfo?.userCountry
    fun myCoins() = userInfo?.coin ?: "0"
    fun mySex() = userInfo?.sex ?: 0
    fun myAge() = userInfo?.age ?: 0
    fun getAuth() = userInfo?.token ?: ""
    fun getImToken() = userInfo?.imToken ?: ""
    fun myLevel() = userInfo?.userLevel ?: 0
    fun myNotDisturb() = userInfo?.notDisturb ?: false
    fun leftTime() = userInfo?.leftTime ?: 0
    fun isMale() = userInfo?.sex == 1
    fun myIntroduce() = userInfo?.introduce


    fun String.updateCoins() {
        userInfo?.coin = this
    }

    fun UserBean.saveUser() {
        userInfo = this
    }

    /**
     * 将用户对象存入内存，顺便将token存入缓存
     */
    private var userInfo: UserBean? = null
        get() {
            if (field == null) {
                field = UserBean()
            }
            return field
        }
        private set(value) {
            field = value
            if (value != null) {
                DataStoreManager.put("token", value.token)
            } else {
                DataStoreManager.put("token", "")
            }
        }

    /**
     * 当前本地是否有token的记录
     */
    fun cacheTokenIsEmpty(): Boolean {
        return DataStoreManager.getString("token", "").isEmpty()
    }

    fun logOut() {
        userInfo = null
    }
}

