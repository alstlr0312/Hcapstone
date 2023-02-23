package com.unity.mynativeapp.Splash


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.ApplicationClass.Companion.AUTHORIZATION

import com.unity.mynativeapp.ApplicationClass.Companion.sSharedPreferences
import com.unity.mynativeapp.BaseResponse

import com.unity.mynativeapp.Main.BaseActivity
import com.unity.mynativeapp.R
import com.unity.mynativeapp.Splash.Login.LoginActivity
import com.unity.mynativeapp.Splash.Login.LoginActivityService
import com.unity.mynativeapp.Splash.Login.LoginResponse
import okhttp3.Call
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()
            finish()
        },2000)
    }

    fun checkLogin(){

        //sSharedPreferences.edit().remove(AUTHORIZATION).commit()

        if(sSharedPreferences.getString(AUTHORIZATION, null) != null){
            startActivity(Intent(this, BaseActivity::class.java))
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}

