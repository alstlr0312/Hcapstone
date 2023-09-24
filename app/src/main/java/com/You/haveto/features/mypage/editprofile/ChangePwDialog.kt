package com.You.haveto.features.mypage.editprofile

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.You.haveto.databinding.DialogChangePwBinding
import com.You.haveto.network.util.PW_FORMAT_ERROR
import java.util.regex.Pattern


class ChangePwDialog(context: Context): Dialog(context) {

    val binding by lazy { DialogChangePwBinding.inflate(layoutInflater) }
    var resultCode = 0
    var changedPw = ""
    var check = arrayListOf(false, false)

    val pwPattern = Pattern.compile("^(?=.*([a-z].*[A-Z])|([A-Z].*[a-z]))(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]\$@\$!%*#?&.]{8,20}\$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUiEvent()
    }

    private fun Context.dialogResize(dialog: Dialog, width: Float, height: Float){
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rect = windowManager.currentWindowMetrics.bounds
        val window = dialog.window
        val x = (rect.width() * width).toInt()
        val y = (rect.height() * height).toInt()
        window?.setLayout(x, y)
    }

    private fun setUiEvent(){

        // 비밀번호 입력 이벤트
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val pw = binding.edtPassword.text.toString()
                if (p0 == null || pw.isEmpty()) {
                    binding.tvPwWarning.visibility = View.GONE
                    return
                }
                if (pwPattern.matcher(pw).find()) {
                    binding.tvPwWarning.visibility = View.GONE
                    check[0] = true
                } else {
                    binding.tvPwWarning.visibility = View.VISIBLE
                    check[0] = false
                }

            }
        })

        // 비밀번호 확인 이벤트
        binding.edtPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                var pwConfirm = binding.edtPasswordConfirm.text.toString()
                if (p0 != null && !pwConfirm.equals("")) {
                    if (check[0] && binding.edtPassword.text.toString() == pwConfirm) {
                        binding.tvPwCheckWarning.visibility = View.GONE
                        check[1] = true
                    } else {
                        binding.tvPwCheckWarning.visibility = View.VISIBLE
                        check[1] = false
                    }
                } else {
                    binding.tvPwCheckWarning.visibility = View.GONE
                }
            }
        })

        // 취소
        binding.btnCancel.setOnClickListener {
            resultCode = 0
            dismiss()
        }
        // 확인
        binding.btnOk.setOnClickListener {

            if(check[0] && check[1]){
                resultCode = 1
                changedPw = binding.edtPassword.text.toString()
                dismiss()
            }else{
                Toast.makeText(context, PW_FORMAT_ERROR, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun show() {
        if (!this.isShowing) super.show()
    }

    override fun dismiss() {
        if (this.isShowing) super.dismiss()
    }


}

