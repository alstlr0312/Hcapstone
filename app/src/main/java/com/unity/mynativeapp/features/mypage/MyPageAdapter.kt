package com.unity.mynativeapp.features.mypage

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.databinding.ItemRvMypageBinding
import com.unity.mynativeapp.features.mypage.myposts.MyPostsActivity
import com.unity.mynativeapp.features.mypage.editprofile.ProfileActivity
import com.unity.mynativeapp.model.MyPageRvItem

class MyPageAdapter(val context: Context, var itemList: MutableList<MyPageRvItem>): RecyclerView.Adapter<MyPageAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemRvMypageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: MyPageRvItem){


            binding.ivImg.setImageResource(item.Img)
            binding.tvTitle.text = item.title
            binding.tvCount.text = item.count.toString()

            if(item.count == -1){
                binding.tvCount.visibility = View.INVISIBLE
            }else{
                binding.tvCount.visibility = View.VISIBLE
            }

            binding.root.setOnClickListener {
                when(adapterPosition){
                    0 -> { // 게시글
                        context.startActivity(Intent(context, MyPostsActivity::class.java))
                    }
                    1 -> { // 댓글

                    }
                    2 -> { // 프로필 수정
                        val intent = Intent(context, ProfileActivity::class.java)
                        //intent.putExtra("username", MyApplication.prefUtil.getString("username", ""))
                        //intent.putExtra("field", MyApplication.prefUtil.getString("field", ""))
                        context.startActivity(intent)
                    }
                    3 -> { // 설정

                    }
                }
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