package com.unity.mynativeapp.network

import com.unity.mynativeapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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

	// 홈 화면 조회 (다이어리 목록 조회)
	// 토큰 재발급
	@GET("/diary")
	fun getRefreshToken(
		@Query("date") date: String
	): Call<MyResponse<LoginData>>
	@GET("/diary/detail") //다이어리 상세 확인
	fun getDiary(
		@Query("date") date: String
	) : Call<MyResponse<DiaryResponse>>
	@GET("/diary") //다이어리 목록 확인
	fun getHome(
		@Query("date") date: String
	) : Call<MyResponse<HomeResponse>>

	@GET("/media/{num}")  //사진 가져오기
	fun getMedia(
		@Path("num") num: Int
	) : Call<ResponseBody>

	// 다이어리 작성
	@Multipart
	@POST("/diary/write")
	fun postDiaryWrite(
		@Part("writeDiaryDto") writeDiaryDto: RequestBody,
		@Part imageFile: MutableList<MultipartBody.Part>
	): Call<MyResponse<String>>

	//게시글 목록 확인
	@GET("/post")
	fun getPost(
		@Query("postType") postType: String,
		@Query("woryOutCategory") woryOutCategory: String,
		@Query("page") page: Int,
		@Query("size") size: Int
	) : Call<MyResponse<PostResponse>>

	//게시글 상세 확인
	@GET("/post/detail/{num}")
	fun getDetailPost(
		@Path("num") num: Int
	) : Call<MyResponse<PostDetailResponse>>
	//게시글 작성
	@Multipart
	@POST("/post/write")
	fun postPostWrite(
		@Part("writePostDto") writePostDto: RequestBody,
		@Part imageFile: MutableList<MultipartBody.Part>
	): Call<MyResponse<PostWriteResponse>>

	// 다이어리 수정
	@Multipart
	@PATCH("/diary/edit/{num}")
	fun patchDiaryEdit(
		@Path("num") num: Int,
		@Part("writeDiaryDto") writeDiaryDto: RequestBody,
		@Part imageFile: MutableList<MultipartBody.Part>
		): Call<MyResponse<String>>

	//게시글 목록 확인
	@GET("/post")
	fun getPost(
		@Query("postType") postType: String?,
		@Query("woryOutCategory") woryOutCategory: String?,
		@Query("page") page: Int?,
		@Query("size") size: Int?
	) : Call<MyResponse<PostResponse>>

	//게시글 상세 확인
	@GET("/post/detail/{num}")
	fun getDetailPost(
		@Path("num") num: Int
	) : Call<MyResponse<PostDetailResponse>>
	//게시글 작성
	@Multipart
	@POST("/post/write")
	fun postPostWrite(
		@Part("writePostDto") writePostDto: RequestBody,
		@Part imageFile: MutableList<MultipartBody.Part>
	): Call<MyResponse<String>>

	// 회원 정보(마이페이지) 조회
	@GET("/member/info")
	fun getMemberPage(
		@Query("username") username: String
	): Call<MyResponse<MemberPageResponse>>


	//지도 체육관 가져오기
	@GET("/api/exercise/seoul")
	fun getMap(
		@Query("district") district: String,
		@Query("offset") offset: Int,
		@Query("limit") limit: Int
	): Call<MyResponse<MapResponse>>
}