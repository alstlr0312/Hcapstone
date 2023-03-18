package com.unity.mynativeapp.feature.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import com.unity.mynativeapp.databinding.ActivityLoginBinding
import com.unity.mynativeapp.feature.BaseActivity
import com.unity.mynativeapp.feature.SignUp.SignUpActivity
import com.unity.mynativeapp.feature.checkemail.CheckEmailActivity
import com.unity.mynativeapp.util.LoadingDialog
import com.unity.mynativeapp.util.hideKeyboard
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

	private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
	private val viewModel by viewModels<LoginViewModel>()

	lateinit var dialog: LoadingDialog

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		dialog = LoadingDialog(this)

		// viewModel의 Data를 Observe하는 이벤트 모음 함수
		subscribeUI()

		// UI Event를 정리한 함수
		setUiEvent()
	}

	private fun setUiEvent() {
		binding.btnLogin.setOnClickListener {
			val id = binding.edtId.text.toString()
			val password = binding.edtPassword.text.toString()

			viewModel.login(id, password)
		}

		binding.tvSignUp.setOnClickListener {
			startActivity(Intent(this, CheckEmailActivity::class.java))
		}

		binding.layoutMain.setOnClickListener {
			this.hideKeyboard()
		}
	}

	private fun subscribeUI() {
		viewModel.toastMessage.observe(this) { message ->
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
		}

		viewModel.loading.observe(this) { isLoading ->
			if (isLoading) dialog.show() else dialog.dismiss()
		}

		viewModel.loginSuccess.observe(this) { isSuccess ->
			if (!isSuccess) return@observe
			if(isSuccess == true) {
				startActivity(Intent(this, BaseActivity::class.java))
				finish()
			}
		}
	}
}