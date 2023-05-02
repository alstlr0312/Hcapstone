package com.unity.mynativeapp.features.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.databinding.ItemRvMypageBinding
import com.unity.mynativeapp.model.MyPageRvItem

class MyPageAdapter(val context: Context, var itemList: MutableList<MyPageRvItem>): RecyclerView.Adapter<MyPageAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemRvMypageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: MyPageRvItem){

            binding.layout.setBackgroundResource(item.bg)

            binding.ivImg.setImageResource(item.Img)
            binding.tvTitle.text = item.title
            binding.tvCount.text = item.count.toString()

            if(item.count == -1){
                binding.tvCount.visibility = View.GONE
            }else{
                binding.tvCount.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageAdapter.ViewHolder {
        return ViewHolder(ItemRvMypageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyPageAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setCounts(postCnt: Int, cmtCnt: Int){
        itemList[0].count = postCnt
        itemList[1].count = cmtCnt
        notifyDataSetChanged()
    }

}