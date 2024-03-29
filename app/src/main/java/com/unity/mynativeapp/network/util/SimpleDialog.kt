package com.unity.mynativeapp.network.util

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.unity.mynativeapp.databinding.DialogSimpleBinding

class SimpleDialog(context: Context, val title: String, val headString: String = ""): Dialog(context) {
    val binding by lazy { DialogSimpleBinding.inflate(layoutInflater)}
    var resultCode = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        setContentView(binding.root)

        binding.tvTitle.text = title
        binding.tvHead.text = headString

        resizeDialog(0.85, 0.28)

        if(headString != ""){
            binding.tvHead.visibility = View.VISIBLE
        }else{
            binding.tvHead.visibility = View.GONE
        }

        binding.btnYes.setOnClickListener {
            resultCode = 1
            dismiss()
        }
        binding.btnNo.setOnClickListener {
            resultCode = 0
            dismiss()
        }

    }

    override fun show() {
        if(!this.isShowing) super.show()
    }

    override fun dismiss() {
        if(this.isShowing) super.dismiss()
    }

    fun resizeDialog(width: Double, height: Double){
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        val params: ViewGroup.LayoutParams? = this.window?.attributes
        params?.width = (size.x * width).toInt()
        params?.height = (size.y * height).toInt()
        this.window?.attributes = params as WindowManager.LayoutParams

    }
}