package com.dating.lib.ui.activity.chat

import android.os.CountDownTimer
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.dating.lib.R
import com.dating.lib.databinding.ActivitySplashBinding
import com.dating.lib.extension.click
import com.dating.lib.extension.color
import com.dating.lib.extension.dp2px
import com.dating.lib.extension.gone
import com.dating.lib.extension.open
import com.dating.lib.extension.openBrowser
import com.dating.lib.extension.superLaunch
import com.dating.lib.extension.visible
import com.dating.lib.local.TbaEventName
import com.dating.lib.local.TbaManager
import com.dating.lib.local.UserManager
import com.dating.lib.local.tbaPost
import com.dating.lib.ui.activity.BaseMviActivity
import com.dating.lib.ui.dialog.live.AgreeDialog
import com.dating.lib.ui.viewmodel.SplashVM
import com.dating.lib.view.ProgressRootView
import com.gyf.immersionbar.ktx.navigationBarHeight
import kotlinx.coroutines.flow.collectLatest

class SplashActivity : BaseMviActivity<ActivitySplashBinding, SplashVM>() {
    override fun getProgressRootView(): ProgressRootView? = null
    private lateinit var countDownTimer: CountDownTimer

    override fun initUI() {
        super.initUI()
        mBinding.tvTerms.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomMargin = 20.dp2px() + navigationBarHeight
        }

        mBinding.progressBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomMargin = 20.dp2px() + navigationBarHeight
        }

        mBinding.tvTerms.movementMethod = LinkMovementMethod.getInstance()
        mBinding.tvTerms.text = buildPPSA()
    }

    override fun initData() {
        super.initData()
        if (!UserManager.cacheTokenIsEmpty()) {
            viewModel.start(false)
        }
    }

    override fun initListener() {
        countDownTimer = object : CountDownTimer(2000, 10) {
            override fun onTick(millisUntilFinished: Long) {
                mBinding.progressBar.progress =
                    ((2000 - millisUntilFinished) * 1.0 / 2000 * 100).toInt()
            }

            override fun onFinish() {
                countDownTimer.cancel()
                mBinding.progressBar.progress = 100
                lifecycleScope.superLaunch {
                    if (UserManager.cacheTokenIsEmpty()) {
                        mBinding.progressBar.gone()
                        mBinding.ivTopBg.visible()
                        mBinding.layerStart.visible()
                    }
                }
            }
        }.start()

        mBinding.ivStart.click {
            AgreeDialog(context = this@SplashActivity, onSure = {
                showLoading()
                viewModel.start(false)
            }).show()
        }
    }

    private fun buildPPSA() = buildSpannedString {
        append(getString(R.string.policy_previous))
        append(" ")
        color(R.color.black.color()) {
            inSpans(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    openBrowser("https://tomilive.com/TermsOfUse.html")
                }
            }) {
                append(getString(R.string.terms))
            }
        }
        append(" ")
        append(getString(R.string.and))
        append(" ")
        color(R.color.black.color()) {
            inSpans(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    openBrowser("https://tomilive.com/PrivacyPolicy.html")
                }
            }) {
                append(getString(R.string.privacy_policy))
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        superLaunch {
            viewModel.jump2MainFlow.collectLatest {
                countDownTimer.cancel()
                waitxxxConfig()
                if (mBinding.progressBar.isVisible) {
                    mBinding.progressBar.progress = 100
                }
                doCheck()
            }
        }
    }

    private fun waitxxxConfig() {
    }

    private fun doCheck() {
        hideLoading()
        routeMain()
    }

    private fun routeMain() {
        open<RootActivity>()
        tbaPost(TbaEventName.startApp)
        //每次进入登录或者注册进入主界面，需要tba上报全局属性
        TbaManager.uploadGlobalParam()
    }

    override fun needTopPadding(): Boolean {
        return false
    }
}