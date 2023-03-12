package com.unity.mynativeapp.network

import com.unity.mynativeapp.model.HomePageResponse
import com.unity.mynativeapp.model.LoginData
import com.unity.mynativeapp.model.LoginRequest
import com.unity.mynativeapp.model.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {

	@POST("signin")
	fun login(@Body loginRequest: LoginRequest): Call<MyResponse<LoginData>>

	@POST("signup")
	fun signup(@Body signUpRequest: SignUpRequest) : Call<MyResponse<String>>

	// 홈 화면
	@GET("/home")
	fun getHomePage( @Query("userIdx") userIdx: Int) : Call<HomePageResponse>
}