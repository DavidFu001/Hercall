package com.dating.lib.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.dating.lib.R
import com.dating.lib.databinding.DialogLoadingBinding
import java.lang.ref.WeakReference

class LoadingDialog(activity: Activity) {
    private val weakActivity: WeakReference<Activity> = WeakReference(activity)
    private var progressDialog: Dialog? = null
    private var isCancelable = true
    private var progressRotateAnimation: Animation? = null
    fun showProgress() {
        val activity = weakActivity.get()
        if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
            if (progressDialog == null) {
                progressRotateAnimation =
                    AnimationUtils.loadAnimation(activity, R.anim.rotate_forever)
                val binding = DialogLoadingBinding.inflate(LayoutInflater.from(activity))
                progressDialog = Dialog(activity, R.style.LoadingDialog).apply {
                    setContentView(binding.root)
                    setCanceledOnTouchOutside(false)
                    setCancelable(isCancelable)
                }

            }

            if (progressDialog?.isShowing == false) {
                kotlin.runCatching { progressDialog?.show() }
            }

            progressDialog?.findViewById<ImageView>(R.id.iv_loading)
                ?.startAnimation(progressRotateAnimation)
        }
    }

    fun hideProgress() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            kotlin.runCatching { progressDialog?.dismiss() }
        }
        progressDialog?.findViewById<ImageView>(R.id.iv_loading)?.clearAnimation()
    }

}