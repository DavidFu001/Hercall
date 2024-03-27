package com.dating.lib.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.dating.lib.R
import java.lang.reflect.ParameterizedType


abstract class BaseMviDialog<VB : ViewBinding>(
    context: Context, private val isBottom: Boolean = false, private val cancelAble: Boolean = true
) : AlertDialog(context, if (isBottom) R.style.DialogBottomInStyle else R.style.DialogCustomStyle) {

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
        setContentView(mBinding.root)
        window?.let { window ->
            val lp = window.attributes
            lp.width = LayoutParams.MATCH_PARENT
            lp.height = LayoutParams.WRAP_CONTENT
            lp.gravity = if (isBottom) Gravity.BOTTOM else Gravity.CENTER
            window.attributes = lp
            window.setBackgroundDrawableResource(android.R.color.transparent)
            //保证EditText能弹出键盘
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        setCancelable(cancelAble)
        setCanceledOnTouchOutside(cancelAble)
        initView()
        initData()
        initListener()
    }

    open fun initListener() {

    }

    open fun initData() {

    }

    open fun initView() {

    }

}