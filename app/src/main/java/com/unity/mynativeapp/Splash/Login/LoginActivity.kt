package com.unity.mynativeapp.Splash.Login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.ApplicationClass.Companion.AUTHORIZATION
import com.unity.mynativeapp.ApplicationClass.Companion.GRANT_TYPE
import com.unity.mynativeapp.ApplicationClass.Companion.JSON
import com.unity.mynativeapp.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.unity.mynativeapp.ApplicationClass.Companion.X_REFRESH_TOKEN
import com.unity.mynativeapp.ApplicationClass.Companion.sSharedPreferences
import com.unity.mynativeapp.Main.BaseActivity
import com.unity.mynativeapp.R
import com.unity.mynativeapp.SignUp.SignUpActivity
import com.unity.mynativeapp.databinding.ActivityLoginBinding
import com.unity.mynativeapp.util.LoadingDialog
import com.unity.mynativeapp.util.hideKeyboard
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException


class LoginActivity : AppCompatActivity(), LoginActivityInterface{
    lateinit var binding: ActivityLoginBinding
    lateinit var dialog: LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {


            if (binding.edtId.text.toString() == "") {
                Toast.makeText(this, getString(R.string.please_input_id), Toast.LENGTH_SHORT).show()
            } else if (binding.edtPassword.text.toString() == "") {
                Toast.makeText(this, getString(R.string.please_input_password), Toast.LENGTH_SHORT)
                    .show()
            } else {

                dialog = LoadingDialog(this)
                dialog.show()

                val id = binding.edtId.text.toString()
                val pw = binding.edtPassword.text.toString()

                // 로그인 요청 (post)
                val data = JSONObject()
                data.put("loginId", id)
                data.put("password", pw)

                LoginActivityService(this).tryPostLogin(data.toString())

            }
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.layoutMain.setOnClickListener {
            this.hideKeyboard()
        }
    }

    override fun onPostLoginSuccess(response: LoginResponse) {
        dialog.dismiss()

        if(response.status != 200){
            runOnUiThread {
                Toast.makeText(this@LoginActivity, response.error, Toast.LENGTH_SHORT).show()
            }
        }else{
            if (response.data != null) {
                var edit = ApplicationClass.sSharedPreferences.edit()
                edit.putString(ApplicationClass.X_ACCESS_TOKEN, response.data.accessToken)
                edit.putString(ApplicationClass.X_REFRESH_TOKEN, response.data.refreshToken)
                //

                edit.commit()

                val accessToken = ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN, null).toString()
                Log.d("accessToken", accessToken)

                runOnUiThread {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.success_sign_in),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                startActivity(Intent(applicationContext, BaseActivity::class.java))
                finish()
            }else{
                Log.d("loginActivity", "response.data is null")
            }

        }
    }

    override fun onPostLoginFailure(message: String) {
        dialog.dismiss()
        runOnUiThread {
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

}