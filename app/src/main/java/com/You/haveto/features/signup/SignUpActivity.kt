package com.You.haveto.features.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.You.haveto.R
import com.You.haveto.config.BaseActivity
import com.You.haveto.databinding.ActivitySignUpBinding
import com.You.haveto.network.util.LOADING_LOSS_WARNING
import com.You.haveto.network.util.LoadingDialog
import java.util.regex.Pattern


class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate){
    private val viewModel by viewModels<SignUpViewModel>()

    private val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    private val pwPattern = Pattern.compile("^(?=.*([a-z].*[A-Z])|([A-Z].*[a-z]))(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]\$@\$!%*#?&.]{8,20}\$")
    private val checkArr = arrayListOf(false, false, false, false)
    private var firstStart = true
    lateinit var dialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialog = LoadingDialog(this)

        subscribeUI()

        setUiEvent()
    }

    private fun setUiEvent() {

        binding.btnEmailAuthenticate.setOnClickListener {
            viewModel.check(binding.edtEmail.text.toString())
        }

        binding.btnSignUp.setOnClickListener {
            val id = binding.edtId.text.toString()
            val pw = binding.edtPassword.text.toString()
            val pwCheck = binding.edtPasswordConfirm.text.toString()
            val name = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            var field: String? = binding.edtAddress.text.toString()
            if (field == "") field = null
            val code = binding.edtAuthenticateCode.text.toString()

            viewModel.signup(id, pw, pwCheck, email, name, field, code)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        // 아이디 입력 이벤트
        binding.edtId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val id = binding.edtId.text.toString()
                if (p0 == null || id.isEmpty()) {
                    binding.tvId.visibility = View.GONE
                    return
                }

                if (id.length < 5 || id.length > 20) {
                    binding.tvId.visibility = View.VISIBLE
                    checkArr[0] = false
                } else {
                    binding.tvId.visibility = View.GONE
                    checkArr[0] = true
                }
            }
        })

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
                    checkArr[1] = true
                } else {
                    binding.tvPassword.visibility = View.VISIBLE
                    checkArr[1] = false
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
                    if (checkArr[1] && binding.edtPassword.text.toString() == pwConfirm) {
                        binding.tvPasswordConfirm.visibility = View.GONE
                        checkArr[2] = true
                    } else {
                        binding.tvPasswordConfirm.visibility = View.VISIBLE
                        checkArr[2] = false
                    }
                } else {
                    binding.tvPasswordConfirm.visibility = View.GONE
                }

            }
        })

        // 이메일 입력 이벤트
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                var email = binding.edtEmail.text.toString()
                if (p0 != null && !email.equals("")) { // 공백이 아니라면
                    if (emailPattern.matcher(email).matches()) { // 이메일 형식이 맞다면
                        binding.tvEmail.visibility = View.GONE
                        checkArr[3] = true
                        binding.btnEmailAuthenticate.visibility = View.VISIBLE

                    } else { // 이메일 형식이 아니라면
                        binding.tvEmail.visibility = View.VISIBLE
                        checkArr[3] = false
                        binding.btnEmailAuthenticate.visibility = View.GONE

                    }
                } else { // 공백 이라면
                    binding.tvEmail.visibility = View.GONE
                    binding.btnEmailAuthenticate.visibility = View.GONE

                }
            }
        })
    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }


        viewModel.loading.observe(this) { type ->
            when(type){
                SHOW_LOADING -> showLoadingDialog(this)
                SHOW_TEXT_LOADING -> showLoadingDialog(this, LOADING_LOSS_WARNING)
                DISMISS_LOADING -> dismissLoadingDialog()
            }
        }

        viewModel.checkSuccess.observe(this){ isSuccess ->
            if(!isSuccess) return@observe
            binding.edtEmail.isEnabled = false
            binding.tvEmail.text = getString(R.string.please_input_email_code)
            binding.tvEmail.visibility = View.VISIBLE
            binding.edtAuthenticateCode.visibility = View.VISIBLE
            binding.btnEmailAuthenticate.visibility = View.GONE
        }

        viewModel.signupSuccess.observe(this) { isSuccess ->
            if(!isSuccess) { // 이메일 중복
                binding.edtEmail.isEnabled = true
                binding.edtEmail.setText("")
                binding.tvEmail.text = getString(R.string.email_invalied_format)
                binding.edtAuthenticateCode.setText("")
                return@observe
            }
            finish()

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