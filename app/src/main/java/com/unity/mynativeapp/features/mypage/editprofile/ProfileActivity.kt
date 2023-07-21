package com.unity.mynativeapp.features.mypage.editprofile

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.viewModels
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityProfileBinding
import com.unity.mynativeapp.model.MediaRvItem

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate) {

    private var firstStart = true
    private val viewModel by viewModels<ProfileViewModel>()
    private var currentPw = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){

    }

    private fun setUiEvent(){

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            showCustomToast(message)
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }

        viewModel.logout.observe(this) { logout ->
            if(logout) logout()
        }

        // 회원 정보 조회
        viewModel.myPageData.observe(this) { data ->

        }


        // 비밀번호 검사 성공
        viewModel.getMemberId.observe(this) { id ->
            if(id==null) return@observe


            /*
            viewModel.editProfile(
                username = binding.edtUsername.text.toString(),
                password = currentPw,
                field = binding.edtField.text.toString().ifEmpty { null },
                memberId = id,

            )*/
        }

        viewModel.editProfileSuccess.observe(this) { isSuccess ->
            if(!isSuccess)return@observe
            // 앱 내부 데이터 수정
            // 닉네임, 프로필 사진
        }
    }

    companion object{
        const val TAG = "ProfileActivity"
    }



    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }
    }
}