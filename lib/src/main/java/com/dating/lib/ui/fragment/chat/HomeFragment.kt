package com.dating.lib.ui.fragment.chat

import com.dating.lib.databinding.FragmentHomeBinding
import com.dating.lib.ui.fragment.BaseMviFragment
import com.dating.lib.ui.viewmodel.HomeVM
import com.dating.lib.view.ProgressRootView

class HomeFragment:BaseMviFragment<FragmentHomeBinding, HomeVM>() {
    override fun getProgressRootView(): ProgressRootView? {
        return null
    }
}