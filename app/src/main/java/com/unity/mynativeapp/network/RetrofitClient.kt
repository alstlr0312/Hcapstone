package com.unity.mynativeapp.network


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val baseUrl = "https://you-have-to.duckdns.org/"

object RetrofitClient{

	private val okHttpClient = OkHttpClient.Builder()
		.readTimeout(61, TimeUnit.SECONDS)
		.connectTimeout(61, TimeUnit.SECONDS)
		.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		.addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
		.build()

	private val retrofit = Retrofit.Builder()
		.baseUrl(baseUrl)
		.client(okHttpClient)
		.addConverterFactory(GsonConverterFactory.create())
		.build()

	private val retrofitService = retrofit.create(RetrofitService::class.java)

	fun getApiService(): RetrofitService = retrofitService

	fun getBaseUrl(): String = baseUrl
}

