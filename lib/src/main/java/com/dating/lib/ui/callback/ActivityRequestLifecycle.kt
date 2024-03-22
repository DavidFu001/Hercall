

package com.dating.lib.ui.callback

/**
 * 在Activity中进行网络请求所需要经历的生命周期函数。
 *
 */
interface ActivityRequestLifecycle:RequestLifecycle {
    fun showLoading()
    fun hideLoading()
}
