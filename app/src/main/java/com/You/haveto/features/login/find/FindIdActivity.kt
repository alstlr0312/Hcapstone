package com.You.haveto.features.login.find

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.You.haveto.R
import com.You.haveto.config.BaseActivity
import com.You.haveto.databinding.ActivityFindIdBinding

class FindIdActivity : BaseActivity<ActivityFindIdBinding>(
    ActivityFindIdBinding::inflate) {
    private val viewModel by viewModels<FindViewModel>()
    private var firstStart = true
    private val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){

        binding.layoutEmail.visibility = View.VISIBLE
        binding.layoutCode.visibility = View.INVISIBLE
        binding.layoutGetId.visibility = View.INVISIBLE

        binding.tvWarningEmail.visibility = View.GONE
        binding.btnEmailAuthenticate.background = getDrawable(R.drawable.shape_btn_gray_unenabled)
        binding.btnEmailAuthenticate.isEnabled = false

    }

    private fun setUiEvent(){
        // 이메일 입력 이벤트
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                var email = binding.edtEmail.text.toString()
                if (p0 != null && email != "") { // 공백이 아니라면
                    if (emailPattern.matcher(email).matches()) { // 이메일 형식이 맞다면
                        binding.tvWarningEmail.visibility = View.GONE
                        binding.btnEmailAuthenticate.isEnabled = true
                        binding.btnEmailAuthenticate.background = getDrawable(R.drawable.sel_btn_red_pressed)

                    } else { // 이메일 형식이 아니라면
                        binding.tvWarningEmail.visibility = View.VISIBLE
                        binding.btnEmailAuthenticate.isEnabled = false
                        binding.btnEmailAuthenticate.background = getDrawable(R.drawable.shape_btn_gray_unenabled)
                    }
                } else { // 공백 이라면
                    binding.tvWarningEmail.visibility = View.GONE
                    binding.btnEmailAuthenticate.isEnabled = false
                    binding.btnEmailAuthenticate.background = getDrawable(R.drawable.shape_btn_gray_unenabled)

                }
            }
        })

        // 인증코드 요청 이벤트
        binding.btnEmailAuthenticate.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            viewModel.emailCode(email)
        }

        // 찾기 버튼 이벤트
        binding.btnFind.setOnClickListener {
            val code = binding.edtAuthenticateCode.text.toString()
            viewModel.findId(code) // 아이디 찾기 요청
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }

        // 이메일 인증코드 요청
        viewModel.getEmailCodeSuccess.observe(this){ isSuccess ->
            if(isSuccess){ // 성공
                binding.layoutEmail.visibility = View.INVISIBLE
                binding.layoutCode.visibility = View.VISIBLE
                binding.tvNotificationSend.text = getString(R.string.please_input_email_code)
                binding.tvNotificationSend.visibility = View.VISIBLE

            }else{ // 실패
                binding.layoutEmail.visibility = View.VISIBLE
                binding.layoutCode.visibility = View.INVISIBLE
            }
        }

        // 찾기 성공
        viewModel.findData.observe(this){ data ->
            if(data == null) return@observe

            binding.tvNotificationSend.visibility = View.GONE
            binding.btnFind.isEnabled = false
            binding.btnFind.background = getDrawable(R.drawable.shape_btn_gray_unenabled)

            binding.layoutGetId.visibility = View.VISIBLE
            binding.tvGetId.text = data
        }

        // 찾기 실패
        viewModel.findError.observe(this){ error ->
            if(error == null) return@observe
            binding.tvNotificationSend.text = error
        }

    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }

    }

}