package com.unity.mynativeapp.features.mypage.setting

import android.os.Bundle
import androidx.activity.viewModels
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivitySettingBinding
import com.unity.mynativeapp.model.BaseRvItem
import com.unity.mynativeapp.network.util.LOADING_LOSS_WARNING
import com.unity.mynativeapp.network.util.MEMBER_DELETE_COMPLETE

interface OnSettingClick{
    fun logoutListener()
    fun memberDeleteListener(pw: String)
}
class SettingActivity : BaseActivity<ActivitySettingBinding>(ActivitySettingBinding::inflate), OnSettingClick {

    private var firstStart = true
    private val viewModel by viewModels<SettingViewModel>()
    private lateinit var settingAdapter: SettingAdapter
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setSettingList(): ArrayList<BaseRvItem>{
        val list = arrayListOf<BaseRvItem>()
        list.add(BaseRvItem(SETTING_TYPE_CHANGE_PW, R.drawable.ic_password, getString(R.string.change_password)))
        list.add(BaseRvItem(SETTING_TYPE_USE_GUIDE, R.drawable.ic_infomation, getString(R.string.app_explain)))
        list.add(BaseRvItem(SETTING_TYPE_LICENCE, R.drawable.ic_open_source, getString(R.string.open_source_license)))
        list.add(BaseRvItem(SETTING_TYPE_LOG_OUT, R.drawable.ic_logout, getString(R.string.logout)))
        list.add(BaseRvItem(SETTING_TYPE_LEAVE, R.drawable.ic_leave, getString(R.string.delete_member)))
        //list.add(BaseRvItem(R.drawable.ic_notification, getString(R.string.notification)))
        //list.add(BaseRvItem(R.drawable.ic_comment_2, getString(R.string.chatting)))

        return list
    }

    private fun setView(){
        settingAdapter = SettingAdapter(this, this)
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

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            showCustomToast(message)
        }

        viewModel.loading2.observe(this) { type ->
            when(type){
                SHOW_LOADING -> showLoadingDialog(this)
                SHOW_TEXT_LOADING -> showLoadingDialog(this, LOADING_LOSS_WARNING)
                DISMISS_LOADING -> dismissLoadingDialog()
            }
        }

        viewModel.logout.observe(this) { logout ->
            if(!logout)return@observe
            logout()
        }

        // 회원 탈퇴
        viewModel.MemberDeleteSuccess.observe(this) { isSuccess ->
            if(!isSuccess)return@observe
            showCustomToast(MEMBER_DELETE_COMPLETE)
            logout(memberDelete = true)
        }
    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }else{
            overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right)
        }
    }

    override fun logoutListener() {
        logout()
    }

    override fun memberDeleteListener(pw: String) {
        viewModel.memberDelete(pw)
    }

    companion object {
        const val TAG = "SettingActivityLog"
        const val SETTING_TYPE_CHANGE_PW = 0
        const val SETTING_TYPE_USE_GUIDE = 1
        const val SETTING_TYPE_LICENCE = 2
        const val SETTING_TYPE_LOG_OUT = 3
        const val SETTING_TYPE_LEAVE = 4
    }
}