package com.unity.mynativeapp.Splash


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.unity.mynativeapp.ApplicationClass
import com.unity.mynativeapp.Main.BaseActivity
import com.unity.mynativeapp.Main.home.HomeFragmentService
import com.unity.mynativeapp.R
import com.unity.mynativeapp.Splash.Login.LoginActivity
import okhttp3.Request
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()}, 2000)
    }

    fun checkLogin(){

        val accessToken = ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN, null)

        if(accessToken != null){ // refresh Token 유효 확인
            val today = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM"))
            val request = Request.Builder()
                .url(ApplicationClass.API_URL + HomeFragmentService.HOME_PAGE + today).get().build()

            ApplicationClass.okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if(response.code != 401){ // 유효
                        val accessToken = ApplicationClass.sSharedPreferences.getString(ApplicationClass.X_ACCESS_TOKEN, null).toString()
                        Log.d("accessToken", accessToken)
                        startActivity(Intent(this@SplashActivity, BaseActivity::class.java))
                        finish()
                    }else{ // 만료
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                    }
                }
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.d("splashActivity", e.message.toString())
                }
            })
        }else{
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }

    }

}

