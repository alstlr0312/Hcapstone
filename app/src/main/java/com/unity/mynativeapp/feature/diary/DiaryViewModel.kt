package com.unity.mynativeapp.feature.diary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.feature.login.LoginViewModel
import com.unity.mynativeapp.model.DiaryExerciseRvItem
import com.unity.mynativeapp.model.DiaryWriteRequest
import com.unity.mynativeapp.model.WriteData
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.util.ID_EMPTY_ERROR
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryViewModel : ViewModel()  {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _loginSuccess = MutableLiveData<Boolean>(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    val shouldStartActivity = MutableLiveData<Boolean>(false)

    fun addex(writeDiaryDto: List<DiaryExerciseRvItem>, memo: String, diaryDate: String) {

        // Email이 비어있을경우
        if (writeDiaryDto.isEmpty()) {
            _toastMessage.postValue(ID_EMPTY_ERROR)
            return
        }


        _loading.postValue(true)

        writeDiaryAPI(writeDiaryDto,memo,diaryDate)
    }


    private fun writeDiaryAPI(
        writeDiaryDto: List<DiaryExerciseRvItem>,
        memo: String,
        diaryDate: String
    ) {
        RetrofitClient.getApiService().createDiary(DiaryWriteRequest(writeDiaryDto,memo,diaryDate)).enqueue(object :
            Callback<MyResponse<WriteData>> {
            override fun onResponse(call: Call<MyResponse<WriteData>>, response: Response<MyResponse<WriteData>>) {
                _loading.postValue(false)
                val code = response.code()
                val data = response.body()?.data
                if (code == 201) {
                    Log.e(LoginViewModel.TAG,code.toString())

                }

            }

            override fun onFailure(call: Call<MyResponse<WriteData>>, t: Throwable) {
                Log.e(LoginViewModel.TAG, "Error: ${t.message}")
            }
        })
    }
}