package com.unity.mynativeapp.network

import com.unity.mynativeapp.model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {
	@POST("email")
	fun email(@Body CheckRequest: CheckRequest): Call<MyResponse<CheckData>>
	@POST("signin")
	fun login(
		@Body loginRequest: RequestBody): Call<MyResponse<LoginData>>
	@POST("signup")
	fun signup(@Query("code") code: String?, @Body signUpRequest: RequestBody) : Call<MyResponse<String>>

	// 홈 화면
	@GET("/diary")
	fun getHomePage( @Query("date") userIdx: String) : Call<HomePageResponse>
}