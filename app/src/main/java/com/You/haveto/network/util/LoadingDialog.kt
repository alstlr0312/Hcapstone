package com.You.haveto.network.util

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.You.haveto.databinding.DialogLoadingBinding

class LoadingDialog(context: Context, val title: String = ""): Dialog(context) {
    val binding by lazy {
        DialogLoadingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable())
        window!!.setDimAmount(0.0f)

        binding.tvWarning.text = title

    }

    override fun show() {
        if(!this.isShowing) super.show()
    }

    override fun dismiss() {
        if(this.isShowing) super.dismiss()
    }
}