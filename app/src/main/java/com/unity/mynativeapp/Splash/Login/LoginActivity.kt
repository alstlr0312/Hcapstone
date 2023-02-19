package com.unity.mynativeapp.Splash.Login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.unity.mynativeapp.ApplicationClass.Companion.sSharedPreferences
import com.unity.mynativeapp.Main.BaseActivity
import com.unity.mynativeapp.R
import com.unity.mynativeapp.SignUp.SignUpActivity
import com.unity.mynativeapp.Splash.LoginResponse
import com.unity.mynativeapp.databinding.ActivityLoginBinding
import com.unity.mynativeapp.util.LoadingDialog
import com.unity.mynativeapp.util.hideKeyboard
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

val SIGN_IN = "signin"

class LoginActivity : AppCompatActivity(){
    lateinit var binding: ActivityLoginBinding
    lateinit var dialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {


            if(binding.edtId.text.toString() == ""){
                Toast.makeText(this, getString(R.string.please_input_id), Toast.LENGTH_SHORT).show()
            }else if(binding.edtPassword.text.toString() == ""){
                Toast.makeText(this, getString(R.string.please_input_password), Toast.LENGTH_SHORT).show()
            }else{

                dialog = LoadingDialog(this)
                dialog.show()

                val id = binding.edtId.text.toString()
                val pw = binding.edtPassword.text.toString()

                //val postRequest = PostLoginRequest(loginId = id, password = pw)
                //LoginService(this).tryPostLogin(postRequest)

                // 로그인 요청
                val data = JSONObject()
                data.put("loginId", id)
                data.put("password", pw)

                val requestBody: RequestBody =
                    RequestBody.create("application/json; charset=utf-8".toMediaType(), data.toString())

                val url = ApplicationClass.API_URL + SIGN_IN

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()


                ApplicationClass.okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {

                    override fun onResponse(call: Call, response: okhttp3.Response) {
                        dialog.dismiss()

                        if (response.body != null) {
                            val body = response.body?.string()
                            val gson = GsonBuilder().create()
                            val data = gson.fromJson(body, LoginResponse::class.java)

                            Log.d("loginActivity",data.toString())

                            if(data.status == 401){
                                runOnUiThread {
                                    Toast.makeText(this@LoginActivity, data.error.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }else if(data.status == 200){
                                if(data.data?.accessToken != null){
                                    var edit = sSharedPreferences.edit()
                                    edit.putString(X_ACCESS_TOKEN, data.data.accessToken)
                                    edit.commit()
                                }
                                runOnUiThread {
                                    Toast.makeText(this@LoginActivity, getString(R.string.success_sign_in), Toast.LENGTH_SHORT).show()
                                }
                                startActivity(Intent(applicationContext, BaseActivity::class.java))
                                finish()
                            }


                        } else {
                            Log.d("loginActivity","response body is null")
                        }
                    }
                    override fun onFailure(call: Call, e: IOException) {
                        dialog.dismiss()

                        Log.d("loginActivity", e.message.toString())
                    }
                })
            }
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.layoutMain.setOnClickListener {
            this.hideKeyboard()
        }
    }


}