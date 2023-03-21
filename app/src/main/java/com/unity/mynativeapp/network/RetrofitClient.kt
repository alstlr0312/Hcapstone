package com.unity.mynativeapp.network

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

	private const val baseUrl = "https://you-have-to.duckdns.org/"


	private val okHttpClient = OkHttpClient.Builder()
		.readTimeout(5000, TimeUnit.MILLISECONDS)
		.connectTimeout(5000, TimeUnit.MILLISECONDS)
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
}