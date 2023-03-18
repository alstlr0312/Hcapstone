package com.unity.mynativeapp.features.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.R

import com.unity.mynativeapp.databinding.ActivitySignUpBinding
import com.unity.mynativeapp.features.BaseActivity
import com.unity.mynativeapp.util.LoadingDialog
import com.unity.mynativeapp.util.hideKeyboard
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity(){
    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SignUpViewModel>()

    val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    val pwPattern = Pattern.compile("^(?=.*([a-z].*[A-Z])|([A-Z].*[a-z]))(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]\$@\$!%*#?&.]{8,20}\$")
    val checkArr = arrayListOf(false, false, false, false)

    lateinit var dialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dialog = LoadingDialog(this)

        subscribeUI()

        setUiEvent()
    }

    private fun setUiEvent() {

        binding.btnSignUp.setOnClickListener {
            val id = binding.edtId.text.toString()
            val pw = binding.edtPassword.text.toString()
            val pwCheck = binding.edtPasswordConfirm.text.toString()
            val name = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()

            viewModel.signup(id, pw, pwCheck, email, name)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.layoutMain.setOnClickListener {
            this.hideKeyboard()
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
                if (p0 != null && !email.equals("")) {
                    if (emailPattern.matcher(email).matches()) {
                        binding.tvEmail.visibility = View.GONE
                        checkArr[3] = true
                    } else {
                        binding.tvEmail.visibility = View.VISIBLE
                        checkArr[3] = false
                    }
                } else {
                    binding.tvEmail.visibility = View.GONE
                }
            }
        })
    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) dialog.show() else dialog.dismiss()
        }
        viewModel.signupSuccess.observe(this) { isSuccess ->
            if (!isSuccess) return@observe

            finish()
        }
    }


}