package com.unity.mynativeapp.SignUp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.SignUp.models.SignUpResponse
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.ApplicationClass.Companion.API_URL
import com.unity.mynativeapp.ApplicationClass.Companion.okHttpClient
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivitySignUpBinding
import com.unity.mynativeapp.util.LoadingDialog
import com.unity.mynativeapp.util.hideKeyboard
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity(){
    lateinit var binding: ActivitySignUpBinding

    val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    val pwPattern = Pattern.compile("^(?=.*([a-z].*[A-Z])|([A-Z].*[a-z]))(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]\$@\$!%*#?&.]{8,20}\$")
    val checkArr = arrayListOf(false, false, false, false)

    lateinit var dialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 아이디 입력 이벤트
        binding.edtId.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                var id = binding.edtId.text.toString()
                if(p0 != null && !id.equals("")){
                    if(id.length < 5 || id.length > 20){
                        binding.tvId.visibility = View.VISIBLE
                        checkArr[0] = false
                    }else{
                        binding.tvId.visibility = View.GONE
                        checkArr[0] = true
                    }
                }else{
                    binding.tvId.visibility = View.GONE
                }

            }
        })

        // 비밀번호 입력 이벤트
        binding.edtPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                var pw = binding.edtPassword.text.toString()
                if(p0 != null && !pw.equals("")){
                    if(pwPattern.matcher(pw).find()){
                        binding.tvPassword.visibility = View.GONE
                        checkArr[1] = true
                    }else{
                        binding.tvPassword.visibility = View.VISIBLE
                        checkArr[1] = false
                    }
                }else{
                    binding.tvPassword.visibility = View.GONE
                }

            }
        })

        // 비밀번호 확인 이벤트
        binding.edtPasswordConfirm.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                var pwConfirm = binding.edtPasswordConfirm.text.toString()
                if(p0 != null && !pwConfirm.equals("")){
                    if(checkArr[1] && binding.edtPassword.text.toString() == pwConfirm){
                        binding.tvPasswordConfirm.visibility = View.GONE
                        checkArr[2] = true
                    }else{
                        binding.tvPasswordConfirm.visibility = View.VISIBLE
                        checkArr[2] = false
                    }
                }else{
                    binding.tvPasswordConfirm.visibility = View.GONE
                }

            }
        })

        // 이메일 입력 이벤트
        binding.edtEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                var email = binding.edtEmail.text.toString()
                if(p0 != null && !email.equals("")){
                    if(emailPattern.matcher(email).matches()){
                        binding.tvEmail.visibility = View.GONE
                        checkArr[3] = true
                    }else{
                        binding.tvEmail.visibility = View.VISIBLE
                        checkArr[3] = false
                    }
                }else{
                    binding.tvEmail.visibility = View.GONE
                }

            }
        })


        binding.btnSignUp.setOnClickListener {
            if(binding.edtId.text.toString() == ""){
                Toast.makeText(this, getString(R.string.please_input_id), Toast.LENGTH_SHORT).show()
            }else if(binding.edtPassword.text.toString() == ""){
                Toast.makeText(this, getString(R.string.please_input_password), Toast.LENGTH_SHORT).show()
            }else if(binding.edtUsername.text.toString() == ""){
                Toast.makeText(this, getString(R.string.please_input_nickname), Toast.LENGTH_SHORT).show()
            }else if(binding.edtEmail.text.toString() == ""){
                Toast.makeText(this, getString(R.string.please_input_email), Toast.LENGTH_SHORT).show()
            }else{
                val id = binding.edtId.text.toString()
                val pw = binding.edtPassword.text.toString()
                val name = binding.edtUsername.text.toString()
                val email = binding.edtEmail.text.toString()

                //val postRequest = PostSignUpRequest(loginId = id, password = pw, username = name, email = email)
                //SignUpService(this).tryPostSignUp(postRequest)

                if(!checkArr[0]){
                    Toast.makeText(this, getString(R.string.id_length), Toast.LENGTH_SHORT).show()
                }else if(!checkArr[1]){
                    Toast.makeText(this, getString(R.string.password_format), Toast.LENGTH_SHORT).show()
                }else if(!checkArr[2]){
                    Toast.makeText(this, getString(R.string.password_not_same), Toast.LENGTH_SHORT).show()
                }else if(!checkArr[3]){
                    Toast.makeText(this, getString(R.string.email_invalied_format), Toast.LENGTH_SHORT).show()
                }else{

                    dialog = LoadingDialog(this)
                    dialog.show()

                    // 회원가입 요청
                    val data = JSONObject()
                    data.put("loginId", id)
                    data.put("password", pw)
                    data.put("username", name)
                    data.put("email", email)

                    val requestBody: RequestBody =
                        RequestBody.create("application/json; charset=utf-8".toMediaType(), data.toString())

                    val url = API_URL + "signup"

                    val request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()


                    okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {

                        override fun onResponse(call: Call, response: okhttp3.Response) {
                            dialog.dismiss()
                            if (response.body != null) {
                                val body = response.body?.string()
                                val gson = GsonBuilder().create()
                                val data = gson.fromJson(body, SignUpResponse::class.java)

                                Log.d("signUpActivity",data.toString())

                                if(data.status == 400){
                                    runOnUiThread {
                                        Toast.makeText(this@SignUpActivity, data.error.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }else if(data.status == 201){
                                    var edit = ApplicationClass.sSharedPreferences.edit()
                                    edit.putString("username", data.data)
                                    edit.commit()

                                    runOnUiThread {
                                        Toast.makeText(this@SignUpActivity, R.string.complete_sign_up, Toast.LENGTH_SHORT).show()
                                    }
                                    finish()
                                }

                            } else {
                                Log.d("signUpActivity","response body is null")
                            }
                        }
                        override fun onFailure(call: Call, e: IOException) {
                            dialog.dismiss()

                            Log.d("signUpActivity", e.message.toString())
                        }
                    })

                }

            }
        }


        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.layoutMain.setOnClickListener {
            this.hideKeyboard()
        }
    }

}