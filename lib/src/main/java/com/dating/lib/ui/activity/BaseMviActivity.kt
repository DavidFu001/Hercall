package com.dating.lib.ui.activity

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.dating.lib.extension.superLaunch
import com.dating.lib.ui.viewmodel.LoadingView
import com.dating.lib.ui.viewmodel.MviViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseMviActivity<VB : ViewBinding, VM : MviViewModel> : BaseActivity<VB>() {

    protected val viewModel: VM by lazy {
        val type = javaClass.genericSuperclass
        val modelClass: Class<VM> = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[modelClass]
    }


    override fun initObserver() {
        superLaunch {
            viewModel.loadUiIntentFlow.collect { loadingView ->
                if (loadingView is LoadingView.Loading) {
                    if (loadingView.show) showLoading() else hideLoading()
                }
            }
        }
    }
}
