package com.unity.mynativeapp.features.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.features.BaseActivity
import com.unity.mynativeapp.R
import com.unity.mynativeapp.util.X_ACCESS_TOKEN
import com.unity.mynativeapp.util.X_REFRESH_TOKEN


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()}, 2000)
    }

    fun checkLogin(){

      //  MyApplication.prefUtil.removeString(X_ACCESS_TOKEN)
       // MyApplication.prefUtil.removeString(X_REFRESH_TOKEN)
        val accessToken = MyApplication.prefUtil.getString(X_ACCESS_TOKEN, null)
        val accessToken2 = MyApplication.prefUtil.getString(X_REFRESH_TOKEN, null)
        /**
         * accessToken이 존재하면 [BaseActivity], 아니면 로그인하도록 [LoginActivity]로 이동동
         */
        val nextActivity = if (accessToken != null || accessToken2 != null) BaseActivity::class.java else LoginActivity::class.java
        startActivity(Intent(this, nextActivity))
        finish()

    }

}

