package com.unity.mynativeapp.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unity.mynativeapp.model.HomePageResponse
import com.unity.mynativeapp.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel : ViewModel() {

    private val _noDiary = MutableLiveData<String>()
    val noDiary: LiveData<String> = _noDiary

    private val _haveDiary = MutableLiveData<String>()
    val haveDiary: LiveData<String> = _haveDiary

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun gethome(date: String) {


        RetrofitClient.getApiService().getHomePage(date).enqueue(object :
            Callback<HomePageResponse> {
            override fun onResponse(call: Call<HomePageResponse>, response: Response<HomePageResponse>) {
                _loading.postValue(false)

                val code = response.code()
                val data = response.body()?.data

                if (code == 200) {
                    if (data == null) return
                    _haveDiary.postValue(data.toString())
                }
                if (code == 400) {
                    if (data == null) return

                    _noDiary.postValue(response.code().toString())
                }
            }

            override fun onFailure(call: Call<HomePageResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}