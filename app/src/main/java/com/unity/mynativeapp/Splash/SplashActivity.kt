package com.unity.mynativeapp.Splash


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.unity.mynativeapp.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.unity.mynativeapp.ApplicationClass.Companion.sSharedPreferences
import com.unity.mynativeapp.Main.BaseActivity
import com.unity.mynativeapp.R
import com.unity.mynativeapp.Splash.Login.LoginActivity


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

        //sSharedPreferences.edit().remove(X_ACCESS_TOKEN).commit()

        if(sSharedPreferences.getString(X_ACCESS_TOKEN, null) != null){
            startActivity(Intent(this, BaseActivity::class.java))
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}