package com.unity.mynativeapp.network

import com.unity.mynativeapp.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

	@POST("signin")
	fun login(@Body loginRequest: LoginRequest): Call<MyResponse<LoginData>>

	@POST("signup")
	fun signup(@Body signUpRequest: SignUpRequest) : Call<MyResponse<String>>

	// 홈 화면 조회 (다이어리 목록 조회)
	@GET("/diary")
	fun getHomePage(
		@Part("date") date: String
	) : Call<HomePageResponse>

	// 다이어리 작성
	@Multipart
	@POST("/diary/write")
	fun diaryWrite(
		@Part("writeDiaryDto") writeDiaryDto: String,
		@Part imageFile : MultipartBody.Part
	): Call<String>
}