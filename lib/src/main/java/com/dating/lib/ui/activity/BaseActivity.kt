package com.dating.lib.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.dating.lib.extension.dp2px
import com.dating.lib.extension.statusBarHeight
import com.dating.lib.extension.superLaunch
import com.dating.lib.local.SecureManager
import com.dating.lib.ui.callback.ActivityRequestLifecycle
import com.dating.lib.ui.callback.UILifecycleLogic
import com.dating.lib.ui.dialog.LoadingDialog
import com.dating.lib.utils.StatusBarUil
import com.dating.lib.view.ProgressRootView
import java.lang.reflect.ParameterizedType

/**
 * 应用程序中所有Activity的基类。
 *
 * @author davidFu
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(),
    ActivityRequestLifecycle, UILifecycleLogic {
    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    val mBinding by lazy {
        getBinding()
    }

    private fun getBinding(): VB {
        val superclass = javaClass.genericSuperclass
        val aClass = (superclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUil.setWindowStatusTransparent(window = window)
        setContentView(mBinding.root)
        updateTopPadding()
        initUI()
        initData()
        initListener()
        initObserver()
        initBlackScreenSetting()
    }

    private fun initBlackScreenSetting() {
        superLaunch {
            if (!SecureManager.getWhiteActivityLists()
                    .contains(this@BaseActivity.javaClass.simpleName)
            ) {//如果不在白名单中，禁止截屏和录屏
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }
        }
    }

    /**
     * 设置距离顶部距离，默认为状态栏
     */
    private fun updateTopPadding() {
        if (needTopPadding()) {
            mBinding.root.updatePadding(top = statusBarHeight + 10.dp2px())
        }
    }

    open fun needTopPadding(): Boolean {
        return true
    }

    override fun showLoading() {
        runCatching {
            loadingDialog.showProgress()
        }
    }

    override fun hideLoading() {
        runCatching {
            loadingDialog.hideProgress()
        }
    }

    /**
     * 加载完成，将加载等待控件隐藏。
     */
    override fun showCustom() {
        getProgressRootView()?.showCustomData()
    }

    override fun showEmpty() {
        getProgressRootView()?.showEmptyData()
    }

    abstract fun getProgressRootView(): ProgressRootView?

}
