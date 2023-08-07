package com.unity.mynativeapp.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.model.NewTokenResponse
import com.unity.mynativeapp.network.util.*
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class XAccessTokenInterceptor : Interceptor {

	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		var request = chain.request()
		val builder: Request.Builder = request.newBuilder()

		val jwtToken: String? = MyApplication.prefUtil.getString(X_ACCESS_TOKEN, null)
		if (jwtToken != null) {
			builder.addHeader(AUTHORIZATION, "$GRANT_TYPE $jwtToken")
		}

		request = builder.build()
		val response = chain.proceed(request)
		Log.d(TAG , response.code.toString())


		if (jwtToken!=null && response.code == 401) { // 사용하던 토큰이 만료되었다면
			Log.d(TAG, "토큰 만료됨")

			val currentToken = MyApplication.prefUtil.getString(X_ACCESS_TOKEN, null)

			if(currentToken != null && currentToken == jwtToken) { // 토큰이 재발급 안되었다면

				val code = refreshToken() //  토큰 재발급
				if(code != 200) { // 재발급이 안되었다면
					return response
				}
			}
			val newToken = MyApplication.prefUtil.getString(X_ACCESS_TOKEN, null)
			if(newToken != null) { // 재발급한 토큰으로 재요청하기
				request = builder
					.header(AUTHORIZATION, "$GRANT_TYPE $newToken")
					.removeHeader(REFRESH_TOKEN)
					.build()
				return chain.proceed(request)
			}

		}else{
			Log.d(TAG, "토큰 없거나 유효함")
		}
		return response
	}


	private fun refreshToken(): Int { // 토큰 재발급
		var returnCode = 0

		runBlocking {

			val today = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM"))
			val refreshRequest = Request.Builder()
				.url(RetrofitClient.getBaseUrl() + "diary?date=" + today).get()
				.addHeader(
                    AUTHORIZATION, "$GRANT_TYPE ${MyApplication.prefUtil.getString(
                        X_ACCESS_TOKEN, null).toString()}")
				.addHeader(
                    REFRESH_TOKEN, "$GRANT_TYPE ${MyApplication.prefUtil.getString(
                        X_REFRESH_TOKEN, null).toString()}")
				.build()

			val client = OkHttpClient.Builder().build()

			client.newCall(refreshRequest).enqueue(object : okhttp3.Callback {
				override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
					if(response.isSuccessful){
						val body = response.body?.string()
						val data = GsonBuilder().create().fromJson(body, MyResponse::class.java)
						returnCode = data.status

						if(data.status != 200){ // 토큰 재발급 실패
							println("${data.status}")
							Log.d("applicationClass", "토큰 재발급 실패 ${data.status}")
						}else{  // 토큰 재발급 성공
							val data = GsonBuilder().create().fromJson(body, NewTokenResponse::class.java)
							Log.d("applicationClass", "토큰 재발급 성공")

							if(data.data != null){
								MyApplication.prefUtil.setString(X_ACCESS_TOKEN, data.data.accessToken)
								MyApplication.prefUtil.setString(X_REFRESH_TOKEN, data.data.refreshToken)

							}
						}
					}else{
						returnCode = response.code
					}
				}
				override fun onFailure(call: okhttp3.Call, e: IOException) {
					returnCode = 400
					Log.d("applicationClass", "통신 오류: "+e.message.toString())
				}
			})

		}
		return returnCode
	}

	companion object {
		const val TAG = "accessTokenInterceptor"
	}
}