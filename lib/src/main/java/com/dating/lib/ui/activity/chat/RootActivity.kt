package com.dating.lib.ui.activity.chat

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.viewpager2.widget.ViewPager2
import com.dating.lib.databinding.ActivityRootBinding
import com.dating.lib.extension.click
import com.dating.lib.ui.activity.BaseMviActivity
import com.dating.lib.ui.adapter.RootFragmentAdapter
import com.dating.lib.ui.viewmodel.MainVM
import com.dating.lib.view.ProgressRootView
import com.gyf.immersionbar.ktx.navigationBarHeight

class RootActivity : BaseMviActivity<ActivityRootBinding, MainVM>() {
    override fun getProgressRootView(): ProgressRootView? = null

    private val mainPagerAdapter by lazy { RootFragmentAdapter(this) }

    override fun initUI() {
        mBinding.viewpager.apply {
            isUserInputEnabled = false
            offscreenPageLimit = 4
            adapter = mainPagerAdapter
        }

        mBinding.bottomBar.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomMargin = navigationBarHeight
        }
    }

    override fun initListener() {
        mBinding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mBinding.home.isSelected = position == 0
                mBinding.message.isSelected = position == 1
                mBinding.mine.isSelected = position == 2
            }
        })


        mBinding.flHome.click {
            mBinding.viewpager.setCurrentItem(0, false)
        }

        mBinding.clChat.click {
            mBinding.viewpager.setCurrentItem(1, false)
        }

        mBinding.flMe.click {
            mBinding.viewpager.setCurrentItem(2, false)
        }

        mBinding.ivActivityFloat.click {}
    }
}