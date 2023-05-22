package com.unity.mynativeapp.features.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.unity.mynativeapp.databinding.ActivityLoginBinding
import com.unity.mynativeapp.features.signup.SignUpActivity
import com.unity.mynativeapp.util.LoadingDialog
import com.unity.mynativeapp.util.hideKeyboard


class LoginActivity : com.unity.mynativeapp.config.BaseActivity<ActivityLoginBinding>(
    ActivityLoginBinding::inflate) {
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewModel의 Data를 Observe하는 이벤트 모음 함수
        subscribeUI()

        // UI Event를 정리한 함수
        setUiEvent()
    }

    private fun setUiEvent() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtId.text.toString()
            val password = binding.edtPassword.text.toString()

            viewModel.login(email, password)
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.layoutMain.setOnClickListener {
            hideKeyBoard()
        }
    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }

        viewModel.loginSuccess.observe(this) { isSuccess ->
            if (!isSuccess) return@observe

            startActivity(Intent(this, com.unity.mynativeapp.features.BaseActivity::class.java))
            finish()
        }
    }


}