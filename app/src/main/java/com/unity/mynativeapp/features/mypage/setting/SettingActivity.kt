package com.unity.mynativeapp.features.mypage.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivitySettingBinding
import com.unity.mynativeapp.features.mypage.myposts.MyPostViewModel
import com.unity.mynativeapp.model.BaseRvItem

class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate) {

    private var firstStart = true
    private val viewModel by viewModels<MyPostViewModel>()
    private lateinit var settingAdapter: SettingAdapter
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
    }

    private fun setSettingList(): ArrayList<BaseRvItem>{
        val list = arrayListOf<BaseRvItem>()
        //아직 이미지 없음
       /* list.add(BaseRvItem(R.drawable.ic_password, getString(R.string.change_password)))
        list.add(BaseRvItem(R.drawable.ic_logout, getString(R.string.logout)))
        list.add(BaseRvItem(R.drawable.ic_leave, getString(R.string.delete_member)))
        list.add(BaseRvItem(R.drawable.ic_notification, getString(R.string.notification)))
        list.add(BaseRvItem(R.drawable.ic_infomation, getString(R.string.app_explain)))
        list.add(BaseRvItem(R.drawable.ic_open_source, getString(R.string.open_source_license)))*/
        return list
    }

    private fun setView(){
        settingAdapter = SettingAdapter(this)
        settingAdapter.setList(setSettingList())
        binding.rvSetting.adapter = settingAdapter

        // 유저 이름
        username = MyApplication.prefUtil.getString("username", "").toString()
        if(username.isNotEmpty()){
        }
    }

    private fun setUiEvent(){
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }
    }
}