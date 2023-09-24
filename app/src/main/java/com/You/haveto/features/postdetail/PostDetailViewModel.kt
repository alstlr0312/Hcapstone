package com.You.haveto.features.postdetail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.You.haveto.features.comment.CommentViewModel
import com.You.haveto.model.PostDetailResponse
import com.You.haveto.network.MyError
import com.You.haveto.network.MyResponse
import com.You.haveto.network.RetrofitClient
import com.You.haveto.network.util.DELETE_COMPLETE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailViewModel : CommentViewModel() {

    private val _postDetailData = MutableLiveData<PostDetailResponse?>()
    val postDetailData: LiveData<PostDetailResponse?> = _postDetailData

    private val _postDeleteSuccess = MutableLiveData<Boolean>()
    val postDeleteSuccess: LiveData<Boolean> = _postDeleteSuccess

    private val _likePressed = MutableLiveData<Boolean>()
    val likePressed: MutableLiveData<Boolean> = _likePressed


    ///// 게시물 상세 조회
    fun getPostDetail(postId: Int) {

        _loading.postValue(true)

        getPostDetailAPI(postId)
    }

    private fun getPostDetailAPI(postId: Int) {
        RetrofitClient.getApiService().getDetailPost(postId).enqueue(object :
            Callback<MyResponse<PostDetailResponse>> {
            override fun onResponse(
                call: Call<MyResponse<PostDetailResponse>>,
                response: Response<MyResponse<PostDetailResponse>>
            ) {
                _loading.postValue(false)

                val code = response.code()
                when (code) {
                    200 -> { // 게시글 상세 조회 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        data?.let {
                            _postDetailData.postValue(data)
                        }
                    }
                    401 -> _logout.postValue(true)
                    400 -> {// 존재하지 않는 게시글
                        _postDetailData.postValue(null)
                    }
                    else -> {
                        Log.d(TAG, "$code")
                    }
                }
            }


            override fun onFailure(call: Call<MyResponse<PostDetailResponse>>, t: Throwable) {
                Log.e(ContentValues.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }


    //// 좋아요
    fun like(postId: Int, isClicked: Boolean){
        _loading.postValue(true)

        RetrofitClient.getApiService().patchPostLike(postId, isClicked).enqueue(object:
        Callback<MyResponse<Int>> {
            override fun onResponse(
                call: Call<MyResponse<Int>>,
                response: Response<MyResponse<Int>>
            ) {
                _loading.postValue(false)
                when(response.code()){
                    200 -> { // 좋아요 요청 성공
                        _likePressed.postValue(true)
                    }
                    400 -> { // 요청 실패
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        val error = data.error.toString()
                        _toastMessage.postValue(error)
                    }
                    else -> {
                        Log.d(TAG, "${GsonBuilder().create().fromJson(response.errorBody()?.string(), MyError::class.java).error}")
                    }
                }
            }

            override fun onFailure(call: Call<MyResponse<Int>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    ///// 게시물 상세 조회
    fun postDelete(postId: Int) {

        _loading.postValue(true)

        postDeleteAPI(postId)
    }

    private fun postDeleteAPI(postId: Int) {
        RetrofitClient.getApiService().patchPostDelete(postId).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(
                call: Call<MyResponse<String>>,
                response: Response<MyResponse<String>>
            ) {
                _loading.postValue(false)

                val code = response.code()
                when (code) {
                    200 -> { // 게시글 삭제 성공
                        _postDeleteSuccess.postValue(true)
                        _toastMessage.postValue(DELETE_COMPLETE)
                    }
                    400 -> {// 존재 하지 않는 게시글
                        _postDeleteSuccess.postValue(false)
                    }
                    401 -> _logout.postValue(true)
                    else -> {
                        Log.d(TAG, "$code")
                    }
                }
            }
            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(ContentValues.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    companion object {
        const val TAG = "PostDetailViewModel"
    }
}