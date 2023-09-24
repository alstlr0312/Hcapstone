package com.You.haveto.features.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.You.haveto.MyApplication
import com.You.haveto.features.BaseActivity
import com.You.haveto.R
import com.You.haveto.network.util.X_ACCESS_TOKEN


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()}, 2000)
    }

    fun checkLogin(){

        val accessToken = MyApplication.prefUtil.getString(X_ACCESS_TOKEN, null)
        /**
         * accessToken이 존재하면 [BaseActivity], 아니면 로그인하도록 [LoginActivity]로 이동
         */
        val nextActivity = if (accessToken != null) BaseActivity::class.java else LoginActivity::class.java
        startActivity(Intent(this, nextActivity))
        finish()

    }

}
