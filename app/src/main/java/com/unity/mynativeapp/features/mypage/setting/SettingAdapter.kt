package com.unity.mynativeapp.features.mypage.setting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvBaseBinding
import com.unity.mynativeapp.databinding.ItemRvPostingBinding
import com.unity.mynativeapp.features.postdetail.PostDetailActivity
import com.unity.mynativeapp.model.BaseRvItem
import com.unity.mynativeapp.model.PostItem
import com.unity.mynativeapp.network.util.SimpleDialog
import java.time.LocalDateTime

class SettingAdapter(val context: Context, val listener: OnSettingClick): RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    var itemList = mutableListOf<BaseRvItem>()

    inner class ViewHolder(val binding: ItemRvBaseBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: BaseRvItem){

            binding.ivIcon.setImageResource(item.icon)
            binding.tvTitle.text = item.title
            binding.root.setOnClickListener {
                when(adapterPosition){
                    0 -> { // 비밀번호 변경
                    }
                    1 -> { // 로그 아웃
                        val dialog = SimpleDialog(context, context.getString(R.string.you_want_log_out))
                        dialog.show()
                        dialog.setOnDismissListener {
                            if(dialog.resultCode == 1){
                                listener.logoutListener()
                            }
                        }
                    }
                    2 -> { // 회원 탈퇴

                    }
                    3 -> { // 이용 가이드
                    }
                    4 -> { // 오픈소스 라이선스
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