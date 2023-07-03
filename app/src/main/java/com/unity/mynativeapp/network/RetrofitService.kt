package com.unity.mynativeapp.network

import com.unity.mynativeapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

	// 이메일 인증
	@POST("email")
	fun email(@Body CheckRequest: CheckRequest): Call<MyResponse<CheckResponse>>

	// 로그인
	@POST("signin")
	fun login(@Body loginRequest: LoginRequest): Call<MyResponse<LoginData>>

	// 회원가입
	@POST("signup")
	fun signup(
		@Query("code") code: String,
		@Body signUpRequest: SignUpRequest
	) : Call<MyResponse<String>>

	// 아이디 찾기
	@GET("find/id")
	fun getFindId(
		@Query("code") code: String
	): Call<MyResponse<String>>

	// 비밀번호 찾기
	@PATCH("find/pw")
	fun patchFindPw(
		@Query("code") code: String
	): Call<MyResponse<String>>

	// 토큰 재발급
	@GET("/diary")
	fun getRefreshToken(
		@Query("date") date: String
	): Call<MyResponse<LoginData>>

	// 홈 화면 조회 (다이어리 목록 조회)
	@GET("/diary")
	fun getHome(
		@Query("date") date: String
	) : Call<MyResponse<HomeResponse>>

	// 미디어 조회
	@GET("/media/{num}")
	fun getMedia(
		@Path("num") num: Int
	) : Call<ResponseBody>

	/*
	//////////////// 다이어리 ///////////////
	 */
	// 다이어리 작성
	@Multipart
	@POST("/diary/write")
	fun postDiaryWrite(
		@Part("writeDiaryDto") writeDiaryDto: RequestBody,
		@Part imageFile: MutableList<MultipartBody.Part>
	): Call<MyResponse<String>>

	// 다이어리 상세 조회
	@GET("/diary/detail")
	fun getDiary(
		@Query("date") date: String
	) : Call<MyResponse<DiaryResponse>>


	// 다이어리 수정
	@Multipart
	@PATCH("/diary/edit/{num}")
	fun patchDiaryEdit(
		@Path("num") num: Int,
		@Part("writeDiaryDto") writeDiaryDto: RequestBody,
		@Part imageFile: MutableList<MultipartBody.Part>
		): Call<MyResponse<String>>

	// 다이어리 삭제
	@PATCH("diary/delete/{num}")
	fun patchDiaryDelete(
		@Path("num") num: Int,
	): Call<MyResponse<Int>>

	/*
	////////////// 게시글 /////////////
	 */

	//게시글 목록 확인
	@GET("/post")
	fun getPost(
		@Query("postType") postType: String?,
		@Query("woryOutCategory") woryOutCategory: String?,
		@Query("username") username: String?,
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

	//게시글 수정
	@Multipart
	@PATCH("/post/edit/{postId}")
	fun postPatchEdit(
		@Path("postId") postId: Int,
		@Part("writePostDto") writePostDto: RequestBody,
		@Part imageFile: MutableList<MultipartBody.Part> // files
	): Call<MyResponse<String>>

	@PATCH("/post/delete/{postId}")
	fun patchPostDelete(
		@Path("postId") postId: Int,
	): Call<MyResponse<String>>

	// 좋아요
	@PATCH("post/like")
	fun patchPostLike(
		@Query("post_id") post_id: Int,
		@Query("clicked") clicked: Boolean,
	): Call<MyResponse<Int>>

	/*
	/////////// 댓글 ////////////////
	 */
	@GET("/comment")
	fun getComment(
		@Query("postId") postId: Int,
		@Query("parentId") parent: Int?,
		@Query("username") username: String?,
		@Query("page") page: Int?,
		@Query("size") size: Int?,
	): Call<MyResponse<CommentGetResponse>>

	@POST("/comment/write")
	fun postCommentWrite(
		@Body commentWriteRequest: CommentWriteRequest
	): Call<MyResponse<String>>

	/*
	//////////// 회원정보 ////////////////
	 */
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