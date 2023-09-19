package com.unity.mynativeapp.network.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.unity.mynativeapp.databinding.DialogIdentificationBinding


class IdentificationDialog(context: Context, val positiveBtnString: String): Dialog(context) {

    val binding by lazy { DialogIdentificationBinding.inflate(layoutInflater) }
    var resultCode = 0
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUiEvent()

        binding.btnOk.text = positiveBtnString
    }


    private fun setUiEvent(){

        // 취소
        binding.btnCancel.setOnClickListener {
            resultCode = 0
            dismiss()
        }
        // 확인
        binding.btnOk.setOnClickListener {
            resultCode = 1
            password = binding.edtPassword.text.toString()
            dismiss()
        }

    }

    override fun show() {
        if (!this.isShowing) super.show()
    }

    override fun dismiss() {
        if (this.isShowing) super.dismiss()
    }


}

