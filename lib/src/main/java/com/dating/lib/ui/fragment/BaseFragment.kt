package com.dating.lib.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dating.lib.ui.callback.RequestLifecycle
import com.dating.lib.ui.callback.UILifecycleLogic
import com.dating.lib.view.ProgressRootView
import java.lang.reflect.ParameterizedType

/**
 * 应用程序中所有Fragment的基类。
 *
 * @author davidFu
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(), RequestLifecycle, UILifecycleLogic {

    val mBinding by lazy {
        getBinding()
    }

    private fun getBinding(): VB {
        val superclass = javaClass.genericSuperclass
        val aClass = (superclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }

    /**
     * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
     * @param view Fragment中inflate出来的View实例。
     * @return  Fragment中inflate出来的View实例原封不动返回。
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initData()
        initListener()
        initObserver()
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