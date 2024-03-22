package com.dating.lib.ui.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.dating.lib.extension.superLaunch
import com.dating.lib.ui.activity.BaseActivity
import com.dating.lib.ui.viewmodel.LoadingView
import com.dating.lib.ui.viewmodel.MviViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseMviFragment<VB : ViewBinding, VM : MviViewModel> : BaseFragment<VB>() {

    open val viewModel: VM by lazy {
        val type = javaClass.genericSuperclass
        val modelClass: Class<VM> = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[modelClass]
    }

    override fun initObserver() {
        lifecycleScope.superLaunch {
            viewModel.loadUiIntentFlow.collect { loadingView ->
                if (activity is BaseActivity<*>) {
                    if (loadingView is LoadingView.Loading) {
                        if (loadingView.show) (activity as BaseActivity<*>).showLoading() else (activity as BaseActivity<*>).hideLoading()
                    }
                }
            }
        }
    }
}
