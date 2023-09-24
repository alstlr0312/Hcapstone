package com.You.haveto.features.diary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.You.haveto.config.BaseActivity.Companion.DISMISS_LOADING
import com.You.haveto.config.BaseActivity.Companion.SHOW_LOADING
import com.You.haveto.config.BaseActivity.Companion.SHOW_TEXT_LOADING
import com.You.haveto.model.DiaryResponse
import com.You.haveto.model.FeedbackPostureRequest
import com.You.haveto.network.MyError
import com.You.haveto.network.MyResponse
import com.You.haveto.network.RetrofitClient
import com.You.haveto.network.util.EDIT_COMPLETE
import com.You.haveto.network.util.SAVE_COMPLETE
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class DiaryViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Int>()
    val loading: LiveData<Int> = _loading

    private val _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> = _logout

    private val _diaryWriteSuccess = MutableLiveData<Boolean>()
    val diaryWriteSuccess: LiveData<Boolean> = _diaryWriteSuccess

    private val _diaryData = MutableLiveData<DiaryResponse?>()
    val diaryData: MutableLiveData<DiaryResponse?> = _diaryData

    private val _mediaData = MutableLiveData<ResponseBody?>()
    val mediaData: MutableLiveData<ResponseBody?> = _mediaData

    private val _diaryEditSuccess = MutableLiveData<Boolean>()
    val diaryEditSuccess: LiveData<Boolean> = _diaryEditSuccess

    private val _diaryDeleteData = MutableLiveData<Int?>()
    val diaryDeleteData: LiveData<Int?> = _diaryDeleteData

    private val _postureFeedbackData = MutableLiveData<String?>()
    val postureFeedbackData: MutableLiveData<String?> = _postureFeedbackData


    // 다이어리 작성
    fun diaryWrite(body: RequestBody, body1: MutableList<MultipartBody.Part>) {
        _loading.postValue(SHOW_TEXT_LOADING)
        postDiaryWrite(body,body1)
    }

    private fun postDiaryWrite(body: RequestBody, body1: MutableList<MultipartBody.Part>) {
        RetrofitClient.getApiService().postDiaryWrite(body,body1).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(DISMISS_LOADING)

                val code = response.code()
                if(code == 201){ // 다이어리 작성 성공
                    val data = response.body()?.data
                    _toastMessage.postValue(SAVE_COMPLETE)
                    _diaryWriteSuccess.postValue(true)

                }else if(code == 401) { // 존재하지 않는 유저
                    // 재로그인
                    Log.d(TAG, "$code 존재하지 않는 유저")
                    _logout.postValue(true)
                }
                else if(code == 400){
                    Log.d(TAG, "$code   ")
                }else if(code == 415){
                    Log.d(TAG, "$code 잘못된 Content-Type")
                }else if(code == 500){
                    Log.d(TAG, "$code 파일 입출력 오류")
                }else{
                    Log.d(TAG, "$code")
                }

            }

            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(DISMISS_LOADING)

            }
        })
    }

    // 다이어리 상세 조회
    fun diaryDetail(date: String) {
        _loading.postValue(SHOW_LOADING)
        getDiaryAPI(date)
    }

    private fun getDiaryAPI(date: String) {
        RetrofitClient.getApiService().getDiary(date).enqueue(object :
            Callback<MyResponse<DiaryResponse>> {
            override fun onResponse(
                call: Call<MyResponse<DiaryResponse>>,
                response: Response<MyResponse<DiaryResponse>>
            ) {
                _loading.postValue(DISMISS_LOADING)

                val code = response.code()
                Log.d(TAG, code.toString())

                when (code) {
                    200 -> { // 다이어리 상세조회 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        _diaryData.postValue(data)
                    }
                    400 -> {//  실패
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        Log.d(TAG, data.error.toString())
                        _diaryData.postValue(null)
                    }
                    401 -> {
                        _logout.postValue(true)
                    }
                    else -> {
                        Log.d(TAG, "$code")
                    }
                }
            }


            override fun onFailure(call: Call<MyResponse<DiaryResponse>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(DISMISS_LOADING)
            }
        })
    }

    // 다이어리 수정
    fun diaryEdit(diaryId: Int, diaryDto: RequestBody, files: MutableList<MultipartBody.Part>) {
        _loading.postValue(SHOW_TEXT_LOADING)
        patchDiaryEditAPI(diaryDto,files, diaryId)
    }

    private fun patchDiaryEditAPI(diaryDto: RequestBody, files: MutableList<MultipartBody.Part>, diaryId: Int) {
        RetrofitClient.getApiService().patchDiaryEdit(diaryId, diaryDto, files).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(DISMISS_LOADING)

                val code = response.code()
                if(code == 200){ // 다이어리 수정 성공
                   val data = response.body()?.data
                    _toastMessage.postValue(EDIT_COMPLETE)
                    _diaryEditSuccess.postValue(true)

                }else if(code == 401) { // 존재하지 않는 유저
                    // 재로그인
                    Log.d(TAG, "$code 존재하지 않는 유저")
                    _logout.postValue(true)
                }
                else if(code == 400){
                    Log.d(TAG, "$code   ")
                }else if(code == 415){
                    Log.d(TAG, "$code 잘못된 Content-Type")
                }else if(code == 500){
                    Log.d(TAG, "$code 파일 입출력 오류")
                }else{
                    Log.d(TAG, "$code")
                }

            }

            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(DISMISS_LOADING)

            }
        })
    }

    // 운동 자극 피드백
    fun postureFeedback(exerciseName: String, bodyPart: String) {
        _loading.postValue(SHOW_LOADING)
        postPostureFeedback(FeedbackPostureRequest(exerciseName, bodyPart))
    }
    private fun postPostureFeedback(body: FeedbackPostureRequest) {
        RetrofitClient.getApiService().postFeedbackPosture(body).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(DISMISS_LOADING)

                val code = response.code()
                when(code){
                    200 -> {// 운동 자극 피드백 요청 성공
                        val data = response.body()?.data
                        _postureFeedbackData.postValue(data)
                    }
                    401 -> {// 존재하지 않는 유저
                        Log.d(TAG, "$code 존재하지 않는 유저")
                        _logout.postValue(true)
                    }

                    else -> {
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        _toastMessage.postValue(data.error.toString())
                        Log.d(TAG, "$code: ${data.error.toString()}")
                    }
                }

            }

            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(DISMISS_LOADING)

            }
        })
    }


    companion object {
        const val TAG = "DiaryViewModel"
    }
}