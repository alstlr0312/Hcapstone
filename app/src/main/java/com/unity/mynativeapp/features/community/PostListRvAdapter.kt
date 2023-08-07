package com.unity.mynativeapp.features.community

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.databinding.ItemRvPostingBinding
import com.unity.mynativeapp.features.postdetail.PostDetailActivity
import com.unity.mynativeapp.model.PostItem
import java.time.LocalDateTime

class PostListRvAdapter(val context: Context): RecyclerView.Adapter<PostListRvAdapter.ViewHolder>() {

    var itemList = mutableListOf<PostItem>()

    inner class ViewHolder(val binding: ItemRvPostingBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: PostItem){


            // 유저 이름
            binding.tvUsername.text = item.username

            // 게시글 제목
            binding.tvPostTitle.text = item.title

            // 게시글 작성일
            binding.tvPostDate.text = item.createdAt

            // 미디어, 좋아요, 댓글 개수, 조회수
            if(item.mediaListCount != 0){
                binding.tvMediaNum.text = item.mediaListCount.toString()
                binding.tvMediaNum.visibility = View.VISIBLE
                binding.ivMedia.visibility = View.VISIBLE
            }else{
                binding.tvMediaNum.visibility = View.GONE
                binding.ivMedia.visibility = View.GONE
            }
            binding.tvLikeCnt.text = item.likeCount.toString()
            binding.tvCommentCnt.text = item.commentCount.toString()
            binding.tvViewsCnt.text = item.views.toString()

            binding.root.setOnClickListener {
                val intent = Intent(context, PostDetailActivity::class.java)
                intent.putExtra("postId",item.postId)
                context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvPostingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItem(item: PostItem){
        itemList.add(item)
        notifyItemChanged(itemCount-1)
    }

    fun removeAllItem(){
        itemList = mutableListOf()
        notifyDataSetChanged()
    }


}