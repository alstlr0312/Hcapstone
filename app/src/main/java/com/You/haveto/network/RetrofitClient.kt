package com.You.haveto.network

import com.google.gson.GsonBuilder
import com.You.haveto.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient{

	private val okHttpClient = OkHttpClient.Builder()
		.readTimeout(30000, TimeUnit.MILLISECONDS)
		.connectTimeout(30000, TimeUnit.MILLISECONDS)
		.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		.addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
		.build()

	private val retrofit = Retrofit.Builder()
		.baseUrl(BuildConfig.BASE_URL)
		.client(okHttpClient)
		//.addConverterFactory(ScalarsConverterFactory.create())
		.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
		.build()

	private val retrofitService = retrofit.create(RetrofitService::class.java)

	fun getApiService(): RetrofitService = retrofitService

}

