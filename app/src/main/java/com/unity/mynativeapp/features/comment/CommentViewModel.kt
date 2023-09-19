package com.unity.mynativeapp.features.comment

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.features.diary.DiaryViewModel
import com.unity.mynativeapp.model.CommentGetResponse
import com.unity.mynativeapp.model.CommentWriteRequest
import com.unity.mynativeapp.model.PostDetailResponse
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ChildDeleteData(
    val commentId: Int?,
    val parentId: Int
)
open class CommentViewModel : ViewModel() {

    val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> = _logout

    private val _commentGetData = MutableLiveData<CommentGetResponse?>()
    val commentGetData : MutableLiveData<CommentGetResponse?> = _commentGetData

    private val _commentWriteSuccess = MutableLiveData<Boolean>()
    val commentWriteSuccess: LiveData<Boolean> = _commentWriteSuccess

    private val _parentCommentDeleteData = MutableLiveData<Int?>()
    val parentCommentDeleteData: LiveData<Int?> = _parentCommentDeleteData

    private val _childCommentDeleteData = MutableLiveData<ChildDeleteData>()
    val childCommentDeleteData: LiveData<ChildDeleteData> = _childCommentDeleteData


    ///// 댓글 조회
    fun commentGet(postId: Int?, parentId: Int?, username: String?, page: Int?, size: Int?) { //comment?postId=1&page=0&size=6'

        _loading.postValue(true)

        getCommentAPI(postId, parentId, username, page, size)
    }

    private fun getCommentAPI(postId: Int?, parentId: Int?, username: String?, page: Int?, size: Int?) {
        RetrofitClient.getApiService().getComment(postId, parentId, username, page, size).enqueue(object :
            Callback<MyResponse<CommentGetResponse>> {
            override fun onResponse(
                call: Call<MyResponse<CommentGetResponse>>,
                response: Response<MyResponse<CommentGetResponse>>
            ) {
                _loading.postValue(false)

                val code = response.code()
                when (code) {
                    200 -> { // 댓글 조회 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
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
                        Log.d(TAG, "$code")
                    }
                }
            }
            override fun onFailure(call: Call<MyResponse<CommentGetResponse>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    ///// 댓글 작성
    fun commentWrite(requestBody: CommentWriteRequest) { //comment?postId=1&page=0&size=6'

        _loading.postValue(true)
        postCommentWriteAPI(requestBody)
    }

    private fun postCommentWriteAPI(requestBody: CommentWriteRequest) {
        RetrofitClient.getApiService().postCommentWrite(requestBody).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(
                call: Call<MyResponse<String>>,
                response: Response<MyResponse<String>>
            ) {
                _loading.postValue(false)

                when (val code = response.code()) {
                    201 -> { // 댓글 작성 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        _commentWriteSuccess.postValue(true)
                        _toastMessage.postValue("댓글을 작성하였습니다.")
                    }
                    401 -> _logout.postValue(true)
                    400 -> { // 댓글 작성 실패
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        _toastMessage.postValue(data.error.toString())
                    }
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

    // 댓글 삭제
    fun commentDelete(commentId: Int, parentId: Int? = null){ // type = 0: 부모 댓글, type = 1: 자식 댓글
        _loading.postValue(true)
        patchCommentDeleteAPI(commentId, parentId)
    }

    private fun patchCommentDeleteAPI(commentId: Int, parentId: Int? = null){
        RetrofitClient.getApiService().patchCommentDelete(commentId).enqueue(object :
            Callback<MyResponse<Int>> {
            override fun onResponse(
                call: Call<MyResponse<Int>>,
                response: Response<MyResponse<Int>>
            ) {
                _loading.postValue(false)

                when (val code = response.code()) {
                    200 -> { // 댓글 삭제 성공
                        val commentId = response.body()?.data
                        Log.d(TAG, commentId.toString())
                        if(parentId == null){ // 부모 댓글 삭제 성공
                            _parentCommentDeleteData.postValue(commentId)
                        }else{ // 자식 댓글 삭제 성공
                            _childCommentDeleteData.postValue(ChildDeleteData(commentId!!, parentId))
                        }
                        _toastMessage.postValue("댓글을 삭제하였습니다.")
                    }
                    401 -> _logout.postValue(true)
                    400 -> { // 댓글 삭제 실패
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        if(parentId == null){ // 부모 댓글 삭제 실패
                            _parentCommentDeleteData.postValue(null)
                        }else{ // 자식 댓글 삭제 실패
                            _childCommentDeleteData.postValue(ChildDeleteData(null, parentId))
                        }
                        _toastMessage.postValue(data.error.toString())
                    }
                    else -> {
                        Log.d(TAG, "$code")
                    }
                }
            }
            override fun onFailure(call: Call<MyResponse<Int>>, t: Throwable) {
                Log.e(ContentValues.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }
    companion object {
        const val TAG = "CommentViewModel"
    }
}