package com.dating.lib.ui.dialog.live

import android.content.Context
import com.dating.lib.databinding.DialogAgreeBinding
import com.dating.lib.extension.click
import com.dating.lib.ui.dialog.BaseMviDialog


class AgreeDialog(context: Context, private val onSure: () -> Unit) :
    BaseMviDialog<DialogAgreeBinding>(context, isBottom = true) {

    override fun initListener() {
        mBinding.ivStart.click {
            dismiss()
            onSure.invoke()
        }

        mBinding.ivClose.click {
            dismiss()
        }
    }
}