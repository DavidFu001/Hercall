package com.dating.lib.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import com.dating.lib.R
import com.dating.lib.databinding.ViewProgressRootBinding
import com.dating.lib.extension.click
import com.dating.lib.extension.gone
import com.dating.lib.extension.visible

@SuppressLint("CustomViewStyleable")
open class ProgressRootView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null
) : RelativeLayout(
    mContext, attrs
) {
    private lateinit var titleStr: String
    private lateinit var textStr: String
    private lateinit var btnStr: String
    private var progressRotateAnimation: Animation? = null
    private var childView: View? = null

    private var topIconResource = 0
    private lateinit var binding: ViewProgressRootBinding

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.progress_root_combine)
            btnStr = a.getString(R.styleable.progress_root_combine_btn_text).let {
                it ?: ""
            }
            titleStr = a.getString(R.styleable.progress_root_combine_tv_title).let {
                it ?: ""
            }
            textStr = a.getString(R.styleable.progress_root_combine_tv_text).let {
                it ?: ""
            }
            topIconResource = a.getResourceId(
                R.styleable.progress_root_combine_iv_top_resource,
                R.drawable.icon_empty_combine
            )
            a.recycle()
        }
        initView()
        initData()
        initListener()
    }

    private fun initListener() {

    }

    private fun initView() {
        progressRotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_forever)
        binding = ViewProgressRootBinding.inflate(LayoutInflater.from(mContext), this, true)
    }

    private fun initData() {
        binding.tvEmptyText.text = textStr
        binding.ivEmptyTitle.setImageResource(topIconResource)
    }


    fun showEmptyData() {
        binding.llNoData.visible()
        showChildView(false)
    }

    fun showCustomData() {
        binding.llNoData.gone()
        showChildView(true)
    }

    private fun showChildView(show: Boolean) {
        if (childView == null && childCount > 1) {
            childView = getChildAt(1)
        }
        childView.visible(show)
    }

    fun setEmptyText(str: String?) {
        str?.let {
            this@ProgressRootView.textStr = it
            binding.tvEmptyText.text = str
        }
    }

    fun setEmptyIcon(iconResource: Int) {
        binding.ivEmptyTitle.setImageResource(iconResource)
    }

    fun showTryAgain(clickCallBack: () -> Unit) {
        binding.btnTryAgain.visible()
        binding.btnTryAgain.click {
            clickCallBack()
        }
    }
}