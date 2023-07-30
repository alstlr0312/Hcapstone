package com.unity.mynativeapp.features.mypage.editprofile

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.features.mypage.MyPageViewModel
import com.unity.mynativeapp.model.EditProfileRequest
import com.unity.mynativeapp.network.MyError
import com.unity.mynativeapp.network.MyResponse
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.network.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.regex.Pattern

class ProfileViewModel(val context: Context): MyPageViewModel() {

    // 비밀번호 검사 후 받은 아이디
    private val _getMemberId = MutableLiveData<Int?>()
    val getMemberId: MutableLiveData<Int?> = _getMemberId

    // 프로필 수정 요청 결과
    private val _editProfileSuccess = MutableLiveData<Boolean>()
    val editProfileSuccess: LiveData<Boolean> = _editProfileSuccess


    private val pwPattern = Pattern.compile("^(?=.*([a-z].*[A-Z])|([A-Z].*[a-z]))(?=.*[0-9])(?=.*[\$@\$!%*#?&.])[A-Za-z[0-9]\$@\$!%*#?&.]{8,20}\$")

    fun checkPw(password: String){

        if(password.isEmpty()){
            _toastMessage.postValue(PW_EMPTY_ERROR)
            return
        }
        _loading.postValue(true)
        RetrofitClient.getApiService().postPasswordCheck(password).enqueue(object : Callback<MyResponse<Int>> {
            override fun onResponse(call: Call<MyResponse<Int>>, response: Response<MyResponse<Int>>) {


                val code = response.code()
                Log.d(TAG, code.toString())

                when(code) {
                    200 -> { // 비밀번호 검사 성공
                        val data = response.body()?.data
                        data?.let {
                            _getMemberId.postValue(data)
                        }
                    }
                    400 -> {
                        _loading.postValue(false)
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        val error = data.error.toString()
                        _toastMessage.postValue(error)
                    }
                    401 -> {
                        _loading.postValue(false)
                        _logout.postValue(true)
                    }
                    else -> {
                        _loading.postValue(false)
                        Log.d(TAG, "$code")
                    }
                }
            }
            override fun onFailure(call: Call<MyResponse<Int>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })

    }

    fun editProfile(username: String, password: String, field: String?, memberId: Int, profileImgPath: String?) {

        //_loading.postValue(true)

        if (username.isEmpty()) {
            _toastMessage.postValue(NICKNAME_EMPTY_ERROR)
            return
        }

        if (username.length > 20) {
            _toastMessage.postValue(NICKNAME_PATTERN_ERROR)
            return
        }

        if (password.isEmpty()) {
            _toastMessage.postValue(PW_EMPTY_ERROR)
            return
        }

        if (!pwPattern.matcher(password).find()) {
            _toastMessage.postValue(PW_PATTERN_ERROR)
            return
        }

        // 프로필 정보
        val editProfileDto = EditProfileRequest(username, password, field, memberId)
        val profileDtoBody = GsonBuilder().serializeNulls().create().toJson(editProfileDto).toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        // 프로필 이미지
        var profileImgBody: MultipartBody.Part? = null
        val profileImgFile = profileImgPath?.let { File(it)}
        //Log.d("herehere", profileImgUri?.path.toString())
        profileImgFile?.let{
            val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                profileImgFile
            )
            profileImgBody = MultipartBody.Part.createFormData("files",  profileImgFile.name, requestFile)
        }
        editProfileAPI(profileDtoBody, profileImgBody)
    }

    private fun editProfileAPI(editProfileDto: RequestBody, profileImgFile: MultipartBody.Part?) {
        RetrofitClient.getApiService().patchMemberInfoEdit(editProfileDto, profileImgFile).enqueue(object : Callback<MyResponse<String>> {
            override fun onResponse(call: Call<MyResponse<String>>, response: Response<MyResponse<String>>) {
                _loading.postValue(false)

                val code = response.code()
                Log.d(TAG, code.toString())

                when(code) {
                    200 -> { // 프로필 수정 성공
                        val data = response.body()?.data
                        Log.d(TAG, data.toString())
                        data?.let {
                            _editProfileSuccess.postValue(true)
                        }
                    }
                    400 -> {
                        val body = response.errorBody()?.string()
                        val data = GsonBuilder().create().fromJson(body, MyError::class.java)
                        val error = data.error.toString()
                        _toastMessage.postValue(error)
                        _editProfileSuccess.postValue(false)
                    }
                    401 -> _logout.postValue(true)
                    else -> {
                        Log.d(TAG, "$code")
                    }
                }
            }
            override fun onFailure(call: Call<MyResponse<String>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                _loading.postValue(false)
            }
        })
    }

    companion object{
        val TAG = "ProfileViewModel"
    }

    private fun getRealPathFromUri(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")){
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, proj, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

}