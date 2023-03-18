package com.unity.mynativeapp.feature.checkemail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityCheckEmailBinding
import com.unity.mynativeapp.feature.SignUp.SignUpActivity
import com.unity.mynativeapp.util.LoadingDialog
import com.unity.mynativeapp.util.hideKeyboard

class CheckEmailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCheckEmailBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CheckViewModel>()
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
        binding.checkbtn.setOnClickListener {
            val email = binding.checkemail.text.toString()
            viewModel.check(email)
        }

        binding.nextbtn.setOnClickListener {
            val checkcode = binding.codetext.text.toString()
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("code",checkcode)
            startActivity(intent)
            finish()
        }

    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) dialog.show() else dialog.dismiss()
        }
    }
}