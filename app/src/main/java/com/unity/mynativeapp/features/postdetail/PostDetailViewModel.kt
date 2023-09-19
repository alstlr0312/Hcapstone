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
                    // 응답이 실패한 경우 처리하는 코드 작성
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

    private val _commentGetData = MutableLiveData<CommentGetResponse?>()
    val commentGetData : MutableLiveData<CommentGetResponse?> = _commentGetData

    ///// 댓글 조회
    fun commentGet(postId: Int, parentId: Int?, username: String?, page: Int?, size: Int?) { //comment?postId=1&page=0&size=6'

        _loading.postValue(true)

        getCommentAPI(postId, parentId, username, page, size)
    }

    private fun getCommentAPI(postId: Int, parentId: Int?, username: String?, page: Int?, size: Int?) {
        RetrofitClient.getApiService().getComment(postId, parentId, username, page, size).enqueue(object :
            Callback<MyResponse<CommentGetResponse>> {
            override fun onResponse(
                call: Call<MyResponse<CommentGetResponse>>,
                response: Response<MyResponse<CommentGetResponse>>
            ) {
                _loading.postValue(false)

                val code = response.code()
                Log.d("aaaaa", "$code $postId")
                when (code) {
                    200 -> { // 댓글 조회 성공
                        val data = response.body()?.data
                        Log.d(CommentViewModel.TAG, data.toString())
                        if(data != null){
                            data.parentId = parentId
                            _commentGetData.postValue(data)
                        }
                    }
                    401 -> _logout.postValue(true)
                    400 -> {// 존재하지 않는 댓글
                        _commentGetData.postValue(null)
                    }
                    else -> {
                        Log.d(CommentViewModel.TAG, "$code")
                    }
                }
            }
            override fun onFailure(call: Call<MyResponse<CommentGetResponse>>, t: Throwable) {
                Log.e(ContentValues.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    companion object {
        const val TAG = "PostDetailViewModel"
    }
}