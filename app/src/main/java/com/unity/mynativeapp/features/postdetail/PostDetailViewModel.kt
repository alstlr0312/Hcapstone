package com.unity.mynativeapp.features.postdetail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.features.comment.CommentViewModel
import com.unity.mynativeapp.features.diary.DiaryViewModel
import com.unity.mynativeapp.model.CommentGetResponse
import com.unity.mynativeapp.model.PostDetailResponse
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.network.util.DELETE_COMPLETE
import com.unity.mynativeapp.network.util.POST_EDIT_COMPLETE
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailViewModel : ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> = _logout

    private val _postDetailData = MutableLiveData<PostDetailResponse?>()
    val postDetailData: LiveData<PostDetailResponse?> = _postDetailData

    private val _postDeleteSuccess = MutableLiveData<Boolean>()
    val postDeleteSuccess: LiveData<Boolean> = _postDeleteSuccess

    private val _mediaData = MutableLiveData<ResponseBody?>()
    val mediaData: MutableLiveData<ResponseBody?> = _mediaData

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

    ////// 미디어
    fun media(num: Int) {

        _loading.postValue(true)

        getmediaAPI(num)
    }

    private fun getmediaAPI(num: Int) {

        RetrofitClient.getApiService().getMedia(num).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                _loading.postValue(false)
                if (response.isSuccessful) {
                    val imageBytes = response.body()
                    _mediaData.postValue(imageBytes)
                } else {
                    val body = response.errorBody()?.string()
                    val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                    _toastMessage.postValue(data.error.toString())
                }

            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(DiaryViewModel.TAG, "Error: ${t.message}")
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
                    200 -> { // 요청 성공
                        _likePressed.postValue(true)
                    }
                    400 -> { // 요청 실패
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        val error = data.error.toString()
                        _toastMessage.postValue(error)
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