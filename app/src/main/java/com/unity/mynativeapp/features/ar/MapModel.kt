package com.unity.mynativeapp.features.ar

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unity.mynativeapp.model.MapResponse
import com.unity.mynativeapp.model.PostDetailResponse
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapModel : ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _getMapSuccess = MutableLiveData<Boolean>()
    val getMapSuccess: LiveData<Boolean> = _getMapSuccess

    private val _mapData = MutableLiveData<MapResponse?>()
    val mapData: LiveData<MapResponse?> = _mapData



    fun GetMap(district: String, offset: Int, limit: Int) {

        _loading.postValue(true)

        getMapAPI(district, offset, limit)
    }

    private fun  getMapAPI(district: String, offset: Int, limit: Int) {
        RetrofitClient.getApiService().getMap(district,offset, limit).enqueue(object :
            Callback<MyResponse<MapResponse>> {
            override fun onResponse(
                call: Call<MyResponse<MapResponse>>,
                response: Response<MyResponse<MapResponse>>
            ) {
                _loading.postValue(false)

                val code = response.code()
                Log.d(ContentValues.TAG, code.toString())
                when (code) {
                    200 -> {
                        val data = response.body()?.data
                        Log.d(ContentValues.TAG, data.toString())
                        data?.let {
                            _mapData.postValue(data)
                        }
                    }
                    400 -> {
                        _mapData.postValue(null)
                    }

                    else -> {
                        Log.d(ContentValues.TAG, "$code")
                    }
                }
            }


            override fun onFailure(call: Call<MyResponse<MapResponse>>, t: Throwable) {
                Log.e(ContentValues.TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }
}