package com.unity.mynativeapp.network


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val baseUrl = "https://you-have-to.duckdns.org/"

object RetrofitClient{

	private val okHttpClient = OkHttpClient.Builder()
		.readTimeout(5000, TimeUnit.MILLISECONDS)
		.connectTimeout(5000, TimeUnit.MILLISECONDS)
		.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		.addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
		.build()
	val gson = GsonBuilder().setLenient().create()
	private val retrofit = Retrofit.Builder()
		.baseUrl(baseUrl)
		.client(okHttpClient)
		.addConverterFactory(GsonConverterFactory.create(gson))
		.build()
	private val retrofit2 = Retrofit.Builder()
		.baseUrl("https://youhaveto.iptime.org/media/")
		.client(okHttpClient)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
	private val retrofitService = retrofit.create(RetrofitService::class.java)
	private val retrofitService2 = retrofit.create(RetrofitService::class.java)

	fun getApiService(): RetrofitService = retrofitService
	fun getApiService2(): RetrofitService = retrofitService2
	fun getBaseUrl(): String = baseUrl
}

