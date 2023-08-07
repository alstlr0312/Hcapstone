package com.unity.mynativeapp.features.login.find

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityFindPwBinding
import com.unity.mynativeapp.model.FindPwRequest
import com.unity.mynativeapp.network.util.PW_FORMAT_ERROR
import java.util.regex.Pattern

class FindPwActivity : BaseActivity<ActivityFindPwBinding>(
    ActivityFindPwBinding::inflate) {
    private val viewModel by viewModels<FindViewModel>()
    val pwPattern = Pattern.compile("^(?=.*([a-z].*[A-Z])|([A-Z].*[a-z]))(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]\$@\$!%*#?&.]{8,20}\$")
    val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    val pwCheck = arrayListOf(false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){
        binding.layoutEmail.visibility = View.VISIBLE
        binding.layoutCode.visibility = View.INVISIBLE
        binding.layoutChangePw.visibility = View.INVISIBLE

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

        // 이메일 코드 요청
        binding.btnEmailAuthenticate.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            viewModel.emailCode(email)
        }
        // 이메일 코드 확인
        binding.btnAuthenticate.setOnClickListener {
            val code = binding.edtAuthenticateCode.text.toString()
            viewModel.emailCheck(code)

        }

        // 비밀번호 입력 이벤트
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val pw = binding.edtPassword.text.toString()
                if (p0 == null || pw.isEmpty()) {
                    binding.tvPassword.visibility = View.GONE
                    return
                }

                if (pwPattern.matcher(pw).find()) {
                    binding.tvPassword.visibility = View.GONE
                    pwCheck[0] = true
                } else {
                    binding.tvPassword.visibility = View.VISIBLE
                    pwCheck[0] = false
                }

            }
        })

        // 비밀번호 확인 이벤트
        binding.edtPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                var pwConfirm = binding.edtPasswordConfirm.text.toString()
                if (p0 != null && !pwConfirm.equals("")) {
                    if (pwCheck[0] && binding.edtPassword.text.toString() == pwConfirm) {
                        binding.tvPasswordConfirm.visibility = View.GONE
                        pwCheck[1] = true
                    } else {
                        binding.tvPasswordConfirm.visibility = View.VISIBLE
                        pwCheck[1] = false
                    }
                } else {
                    binding.tvPasswordConfirm.visibility = View.GONE
                }

            }
        })

        binding.btnChangePw.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val pw = binding.edtPassword.text.toString()
            if(pwCheck[0] && pwCheck[1]){
                viewModel.findPw(FindPwRequest(email, pw))
                //showCustomToast("비밀번호 변경 요청")
            }else{
                showCustomToast(PW_FORMAT_ERROR)
            }

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

        // 이메일 코드 요청
        viewModel.getEmailCodeSuccess.observe(this){ isSuccess ->
            if(isSuccess){ // 성공
                binding.layoutEmail.visibility = View.INVISIBLE
                binding.layoutCode.visibility = View.VISIBLE
                binding.tvNotificationSend.text = getString(R.string.please_input_email_code)
                binding.tvNotificationSend.visibility = View.VISIBLE

                binding.btnAuthenticate.text = getString(R.string.email_authenticate)

            }else{ // 실패
                binding.layoutEmail.visibility = View.VISIBLE
                binding.layoutCode.visibility = View.INVISIBLE
            }
        }

        // 이메일 코드 확인
        viewModel.emailCheckSuccess.observe(this){ isSuccess ->

            if(isSuccess){ // 인증 성공
                binding.tvFindByEmail.visibility = View.INVISIBLE
                binding.layoutCode.visibility = View.INVISIBLE
                binding.layoutChangePw.visibility = View.VISIBLE
            }
        }

        // 비밀번호 변경 요청
        viewModel.findData.observe(this){
            if(it==null) return@observe

            finish()
        }

    }



}