package com.unity.mynativeapp

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.Splash.LoginResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class ApplicationClass: Application() {
    companion object {
        lateinit var sSharedPreferences: SharedPreferences

        // JWT Token
        val AUTHORIZATION = "Authorization"
        val REFRESH_TOKEN = "refreshToken"
        val GRANT_TYPE = "Bearer"

        val X_ACCESS_TOKEN = "X_ACCESS_TOKEN"
        val X_REFRESH_TOKEN = "X_REFRESH_TOKEN"

        lateinit var okHttpClient: OkHttpClient

        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
        val FORM_DATA: MediaType = "application/octet-stream".toMediaType()

        val API_URL = "http://175.114.240.162:8080/"
        var TOKEN =""

    }

    override fun onCreate() {
        super.onCreate()
        sSharedPreferences = applicationContext.getSharedPreferences("YOU_HAVE_TO", MODE_PRIVATE)

        okHttpClient = OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

    }

    private inner class XAccessTokenInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val builder = request.newBuilder();

            val token = sSharedPreferences.getString(X_ACCESS_TOKEN, null)
            if(token != null){ // 사용하던 토큰이 있다면 헤더에 넣음
                builder.addHeader(AUTHORIZATION, "$GRANT_TYPE $token")
            }
            Log.d("applicationClass", "token $token")
            TOKEN=token.toString()
            request = builder.build()
            val response = chain.proceed(request)

            if (response.code == 401) { // 사용하던 토큰이 만료되었다면
                Log.d("applicationClass", "토큰 만료됨")

                synchronized (okHttpClient) {
                    val currentToken = sSharedPreferences.getString(X_ACCESS_TOKEN, null)

                    if(currentToken != null && currentToken.equals(token)) { // 토큰이 재발급 안되었다면

                        val code = refreshToken() //  토큰 재발급
                        if(code != 200) { // 재발급이 안되었다면
                            logout() // 로그아웃 -> 다시 로그인
                            Log.d("applicationClass", "로그아웃")
                            return response
                        }
                    }
                    val newToken = sSharedPreferences.getString(X_ACCESS_TOKEN, null)
                    if(newToken != null) { // 재발급한 토큰으로 재요청하기
                        builder.header(AUTHORIZATION, "$GRANT_TYPE $newToken")
                        request = builder.build()
                        TOKEN=newToken.toString()
                        return chain.proceed(request)
                    }
                }
            }else{
                Log.d("applicationClass", "토큰 없거나 유효함")
            }
            return response
        }

        private fun refreshToken(): Int { // 토큰 재발급
            var returnCode = 0
            val today = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM"))
            val refreshRequest = Request.Builder()
                .url(ApplicationClass.API_URL + "diary?date=" + today).get()
                .addHeader(AUTHORIZATION, "$GRANT_TYPE ${sSharedPreferences.getString(X_ACCESS_TOKEN, null).toString()}")
                .addHeader(REFRESH_TOKEN, "$GRANT_TYPE ${sSharedPreferences.getString(X_REFRESH_TOKEN, null).toString()}")
                .build()

            val client = OkHttpClient.Builder().build()

            client.newCall(refreshRequest).enqueue(object : okhttp3.Callback {
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if(response.isSuccessful){
                        val body = response.body?.string()
                        //val gson = GsonBuilder().create()
                        //                    val data = gson.fromJson(body, BaseResponse::class.java)
                        val data = GsonBuilder().create().fromJson(body, BaseResponse::class.java)
                        returnCode = data.status

                        if(data.status != 200){ // 토큰 재발급 실패
                            Log.d("applicationClass", "토큰 재발급 실패 $data")
                        }else{  // 토큰 재발급 성공
                            //val body = response.body?.string()
                            //                        val gson = GsonBuilder().create()
                            val data = GsonBuilder().create().fromJson(body, LoginResponse::class.java)

                            Log.d("applicationClass", "토큰 재발급 성공 $data")

                            sSharedPreferences.edit() // 재발급한 토큰 저장
                                .putString(X_ACCESS_TOKEN, data.data?.accessToken)
                                .putString(X_REFRESH_TOKEN, data.data?.refreshToken)
                                .commit()
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
            while(returnCode == 0){}
            return returnCode
        }

        private fun logout() {
            sSharedPreferences.edit().remove(X_ACCESS_TOKEN).remove(X_REFRESH_TOKEN).commit()
        }
    }
}