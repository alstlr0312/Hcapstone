package com.unity.mynativeapp.features.diary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.model.DiaryResponse
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.network.util.EDIT_COMPLETE
import com.unity.mynativeapp.network.util.SAVE_COMPLETE
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class DiaryViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

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



    // 다이어리 작성
    fun diaryWrite(body: RequestBody, body1: MutableList<MultipartBody.Part>) {
        _loading.postValue(true)
        postDiaryWrite(body,body1)
    }

    private fun postDiaryWrite(body: RequestBody, body1: MutableList<MultipartBody.Part>) {
        RetrofitClient.getApiService().postDiaryWrite(body,body1).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(false)

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
                _loading.postValue(false)

            }
        })
    }

    // 다이어리 상세 조회
    fun diaryDetail(date: String) {
        _loading.postValue(true)
        getDiaryAPI(date)
    }

    private fun getDiaryAPI(date: String) {
        RetrofitClient.getApiService().getDiary(date).enqueue(object :
            Callback<MyResponse<DiaryResponse>> {
            override fun onResponse(call: Call<MyResponse<DiaryResponse>>, response: Response<MyResponse<DiaryResponse>>) {
                _loading.postValue(false)

                val code = response.code()
                Log.d(TAG, code.toString())

                when(code) {
                    200 -> { // 다이어리 상세조회 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        data?.let {
                            _diaryData.postValue(data)
                        }
                    }
                    400 -> {//  실패
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        _toastMessage.postValue(data.error.toString())
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
                _loading.postValue(false)
            }
        })
    }
    fun media(num: Int) {
        _loading.postValue(true)
        getMediaAPI(num)
    }

    private fun getMediaAPI(num: Int) {
        RetrofitClient.getApiService().getMedia(num).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                _loading.postValue(false)
                if (response.isSuccessful) {
                    val data = response.body()
                    _mediaData.postValue(data)

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

    // 다이어리 수정
    fun diaryEdit(diaryDto: RequestBody, files: MutableList<MultipartBody.Part>, diaryId: Int) {
        _loading.postValue(true)
        patchDiaryEditAPI(diaryDto,files, diaryId)
    }

    private fun patchDiaryEditAPI(diaryDto: RequestBody, files: MutableList<MultipartBody.Part>, diaryId: Int) {
        RetrofitClient.getApiService().patchDiaryEdit(diaryId, diaryDto,files).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(false)

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
                _loading.postValue(false)

            }
        })
    }



    companion object {
        const val TAG = "DiaryViewModel"
    }
}