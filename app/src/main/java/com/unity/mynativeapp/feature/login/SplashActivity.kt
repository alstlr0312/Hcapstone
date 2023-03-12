package com.unity.mynativeapp.feature.login


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unity.mynativeapp.Main.BaseActivity
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.databinding.ActivitySplashBinding
import com.unity.mynativeapp.feature.BaseActivity
import com.unity.mynativeapp.util.X_ACCESS_TOKEN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

	private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		CoroutineScope(Dispatchers.Default).launch {
			delay(2000L)
			checkLogin()
		}
	}

	private fun checkLogin() {

		val accessToken = MyApplication.prefUtil.getString(X_ACCESS_TOKEN, null)

		/**
		 * accessToken이 존재하면 [BaseActivity], 아니면 로그인하도록 [LoginActivity]로 이동동
	 	 */
		val nextActivity = if (accessToken != null) BaseActivity::class.java else LoginActivity::class.java
		startActivity(Intent(this, nextActivity))
		finish()
	}
}