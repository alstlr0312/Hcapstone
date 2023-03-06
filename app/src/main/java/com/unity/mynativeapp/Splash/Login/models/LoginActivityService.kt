package com.unity.mynativeapp.Splash.Login.models

import android.util.Log
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.Splash.LoginResponse
import okhttp3.Call
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginActivityService(val loginActivityInterface: LoginActivityInterface) {
    companion object{
        const val SIGN_IN = "signin"
    }
    // 로그인 요청
    fun tryPostLogin(data: String){

        val postLoginRequest = Request.Builder()
            .url(ApplicationClass.API_URL + SIGN_IN)
            .post(data.toRequestBody(ApplicationClass.JSON))
            .build()

        ApplicationClass.run {
            okHttpClient.newCall(postLoginRequest).enqueue(object : okhttp3.Callback {

                override fun onResponse(call: Call, response: okhttp3.Response) {

                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val data = gson.fromJson(body, LoginResponse::class.java)

                    if (data != null) {
                        Log.d("loginActivityService", data.toString() + " " + response.code)
                        loginActivityInterface.onPostLoginSuccess(data)

                    } else {
                        loginActivityInterface.onPostLoginFailure(response.code.toString())
                    }


                }
                override fun onFailure(call: Call, e: IOException) {
                    loginActivityInterface.onPostLoginFailure(e.message.toString())
                }
            })
        }
    }
}