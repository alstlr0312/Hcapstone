package com.unity.mynativeapp.network

import android.util.Log
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.util.X_ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class XAccessTokenInterceptor : Interceptor {

	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val builder: Request.Builder = chain.request().newBuilder()

		val jwtToken: String? = MyApplication.prefUtil.getString(X_ACCESS_TOKEN, null)
		if (jwtToken != null) {
			builder.addHeader("Authorization", "Bearer $jwtToken")
		}
		return chain.proceed(builder.build())
	}
}