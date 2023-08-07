package com.unity.mynativeapp.features.mypage.editprofile

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.unity.mynativeapp.databinding.DialogSaveProfileBinding


class SaveProfileDialog(context: Context): Dialog(context) {

    val binding by lazy { DialogSaveProfileBinding.inflate(layoutInflater) }
    var resultCode = 0
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUiEvent()
    }


    private fun setUiEvent(){

        // 취소
        binding.btnCancel.setOnClickListener {
            resultCode = 0
            dismiss()
        }
        // 확인
        binding.btnSave.setOnClickListener {
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

