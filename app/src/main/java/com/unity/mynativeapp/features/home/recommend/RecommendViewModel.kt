package com.unity.mynativeapp.features.home.recommend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.features.login.LoginViewModel
import com.unity.mynativeapp.features.signup.SignUpViewModel
import com.unity.mynativeapp.model.DietFoodRequest
import com.unity.mynativeapp.model.RoutineRequest
import com.unity.mynativeapp.model.SignUpRequest
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.network.util.EMAIL_DUPLICATE_ERROR
import com.unity.mynativeapp.network.util.SIGNUP_SUCCESS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendViewModel: ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> = _logout

    private val _recommendData = MutableLiveData<String?>()
    val recommendData: LiveData<String?> = _recommendData

    fun recommendRoutine(body: RoutineRequest){
        _loading.postValue(true)

        RetrofitClient.getApiService().postRoutines(body).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(false)

                val code = response.code()
                val data = response.body()?.data

                Log.d(TAG, "data: $data")


                if (code == 200) { // 루틴 추천
                    if (data == null) return

                    _recommendData.postValue(data)
                }else{
                    val body = response.errorBody()?.string()
                    val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                    val error = data.error.toString()
                    _toastMessage.postValue(error)
                    _recommendData.postValue(null)

                }
            }

            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    fun recommendFood(body: DietFoodRequest){
        _loading.postValue(true)

        RetrofitClient.getApiService().postDietFood(body).enqueue(object :
            Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(false)

                val code = response.code()
                val data = response.body()?.data

                Log.e(TAG, "data: $data")


                if (code == 200) { // 식단 추천
                    if (data == null) return

                    _recommendData.postValue(data)
                }else{
                    val body = response.errorBody()?.string()
                    val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                    val error = data.error.toString()
                    _toastMessage.postValue(error)
                    _recommendData.postValue(null)

                }
            }

            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    companion object{
        val TAG = "RecommendViewModel"
    }

}