package com.You.haveto.features.mypage.setting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.You.haveto.R
import com.You.haveto.databinding.ItemRvBaseBinding
import com.You.haveto.features.login.find.FindPwActivity
import com.You.haveto.features.mypage.setting.SettingActivity.Companion.SETTING_TYPE_CHANGE_PW
import com.You.haveto.features.mypage.setting.SettingActivity.Companion.SETTING_TYPE_LEAVE
import com.You.haveto.features.mypage.setting.SettingActivity.Companion.SETTING_TYPE_LICENCE
import com.You.haveto.features.mypage.setting.SettingActivity.Companion.SETTING_TYPE_LOG_OUT
import com.You.haveto.features.mypage.setting.SettingActivity.Companion.SETTING_TYPE_USE_GUIDE
import com.You.haveto.model.BaseRvItem
import com.You.haveto.network.util.IdentificationDialog
import com.You.haveto.network.util.SimpleDialog

class SettingAdapter(val context: Context, val listener: OnSettingClick): RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    var itemList = mutableListOf<BaseRvItem>()

    inner class ViewHolder(val binding: ItemRvBaseBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: BaseRvItem){

            binding.ivIcon.setImageResource(item.icon)
            binding.tvTitle.text = item.title
            binding.root.setOnClickListener {
                when(item.type){
                    SETTING_TYPE_CHANGE_PW -> { // 비밀번호 변경
                        val intent = Intent(context, FindPwActivity::class.java)
                        intent.putExtra("mode", "change")
                        context.startActivity(intent)
                    }
                    SETTING_TYPE_LOG_OUT -> { // 로그 아웃
                        val dialog = SimpleDialog(context, context.getString(R.string.you_want_log_out))
                        dialog.show()
                        dialog.setOnDismissListener {
                            if(dialog.resultCode == 1){
                                listener.logoutListener()
                            }
                        }
                    }
                    SETTING_TYPE_LEAVE -> { // 회원 탈퇴
                        val alertDialog = SimpleDialog(context, context.getString(R.string.you_want_delete_member))
                        alertDialog.show()
                        alertDialog.setOnDismissListener {
                            if(alertDialog.resultCode == 1){
                                val dialog = IdentificationDialog(context, context.getString(R.string.withdrawal))
                                dialog.show()
                                dialog.setOnDismissListener {
                                    if(dialog.resultCode == 1){
                                        listener.memberDeleteListener(dialog.password)
                                    }
                                }
                            }
                        }
                    }
                    SETTING_TYPE_USE_GUIDE -> { // 이용 가이드

                    }
                    SETTING_TYPE_LICENCE -> { // 오픈소스 라이선스
                        OssLicensesMenuActivity.setActivityTitle(context.getString(R.string.open_source_license))
                        val intent = Intent(context, OssLicensesMenuActivity::class.java)
                        context.startActivity(intent)
                    }
//                    5 -> { // 알림, 채팅
//                    }
                }

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvBaseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setList(nList: ArrayList<BaseRvItem>){
        itemList = nList
    }

}