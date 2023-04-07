package com.unity.mynativeapp.network

import com.unity.mynativeapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

	@POST("email")
	fun email(@Body CheckRequest: CheckRequest): Call<MyResponse<CheckData>>

	@POST("signin")
	fun login(@Body loginRequest: LoginRequest): Call<MyResponse<LoginData>>

	@POST("signup")
	fun signup(
		@Query("code") code: String,
		@Body signUpRequest: SignUpRequest
	) : Call<MyResponse<String>>

	// 토큰 재발금
	@GET("/diary")
	fun getRefreshToken(
		@Query("date") date: String
	): Call<MyResponse<LoginData>>

	// 홈 화면 조회 (다이어리 목록 조회)
	@GET("/diary")
	fun getHome(
		@Query("date") date: String
	) : Call<MyResponse<HomeResponse>>

	// 다이어리 작성
	@Multipart
	@POST("/diary/write")
	fun postDiaryWrite(
		@Part("writeDiaryDto") writeDiaryDto: RequestBody,
		@Part imageFile: MutableList<MultipartBody.Part>
	): Call<MyResponse<DiaryWriteResponse>>

}