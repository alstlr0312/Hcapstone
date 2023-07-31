package com.unity.mynativeapp.features.mypage.setting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.databinding.ItemRvBaseBinding
import com.unity.mynativeapp.databinding.ItemRvPostingBinding
import com.unity.mynativeapp.features.postdetail.PostDetailActivity
import com.unity.mynativeapp.model.BaseRvItem
import com.unity.mynativeapp.model.PostItem
import java.time.LocalDateTime

class SettingAdapter(val context: Context): RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    var itemList = mutableListOf<BaseRvItem>()

    inner class ViewHolder(val binding: ItemRvBaseBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: BaseRvItem){

            binding.ivIcon.setImageResource(item.icon)
            binding.tvTitle.text = item.title
            binding.root.setOnClickListener {
                //val intent = Intent(context, PostDetailActivity::class.java)
                //context.startActivity(intent)
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