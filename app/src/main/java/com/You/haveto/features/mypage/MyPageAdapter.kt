package com.You.haveto.features.mypage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.You.haveto.databinding.ItemRvMypageBinding
import com.You.haveto.features.mypage.editprofile.ProfileActivity
import com.You.haveto.features.mypage.mycomments.MyCommentsActivity
import com.You.haveto.features.mypage.myposts.MyPostsActivity
import com.You.haveto.features.mypage.setting.SettingActivity
import com.You.haveto.model.MyPageRvItem

class MyPageAdapter(val context: Context,
                    var itemList: MutableList<MyPageRvItem>,
                    username: String
): RecyclerView.Adapter<MyPageAdapter.ViewHolder>(){

    val username = username

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
                        val intent = Intent(context, MyPostsActivity::class.java)
                        intent.putExtra("username", username)
                        context.startActivity(intent)
                    }
                    1 -> { // 댓글
                        val intent = Intent(context, MyCommentsActivity::class.java)
                        context.startActivity(intent)
                    }
                    2 -> { // 프로필 수정
                        Log.d("aaaa", "clicked")
                        context.startActivity(Intent(context, ProfileActivity::class.java))
                    }
                    3 -> { // 설정
                        context.startActivity(Intent(context, SettingActivity::class.java))
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

    fun setPostCounts(postCnt: Int){
        itemList[0].count = postCnt
        notifyItemChanged(0)
    }
    fun setCmtCounts(cmtCnt: Int){
        itemList[1].count = cmtCnt
        notifyItemChanged(1)
    }


}