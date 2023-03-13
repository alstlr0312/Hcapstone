package com.unity.mynativeapp.network

import com.unity.mynativeapp.model.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

	@POST("signin")
	fun login(@Body loginRequest: LoginRequest): Call<MyResponse<LoginData>>

	@POST("signup")
	fun signup(@Body signUpRequest: SignUpRequest) : Call<MyResponse<String>>

	@GET("diary?date={date}")
	fun datePage(@Query("date") date: String) : Call<diarydateResponse>
	// 홈 화면
	@GET("/home")
	fun getHomePage( @Query("userIdx") userIdx: Int) : Call<HomePageResponse>

	@Multipart
	@POST("diary/write")
	fun createDiary(
		@Part("writeDiaryDto") writeDiaryDto: DiaryWriteRequest,
	): Call<MyResponse<WriteData>>
}

