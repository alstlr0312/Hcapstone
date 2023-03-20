package com.unity.mynativeapp.features.community

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.model.PostingRvItem
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvPostingBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PostingRvAdapter(val context: Context): RecyclerView.Adapter<PostingRvAdapter.ViewHolder>() {

    var itemList = mutableListOf<PostingRvItem>()
    var today = LocalDateTime.now()
    inner class ViewHolder(val binding: ItemRvPostingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: PostingRvItem){

            //binding.ivProfileImg.setImageURI()

            // 유저 이름
            binding.tvUsername.text = item.userName

            // 유저 프로필 사진
            Glide.with(binding.ivProfileImg)
                .load(item.profileImg)
                .placeholder(R.drawable.shape_bg_black_rounded)
                .error(R.drawable.shape_bg_black_rounded)
                .fallback(R.drawable.shape_bg_black_rounded)
                .centerCrop()
                .apply(RequestOptions.centerCropTransform())
                .into(binding.ivProfileImg)

            // 게시글 제목
            binding.tvPostTitle.text = item.postTitle

            // 게시글 작성일

            var uploadDate = ""
            if(today.toLocalDate().equals(item.postDate.toLocalDate())){ // 오늘
                if(today.hour == item.postDate.hour && today.minute == item.postDate.minute){ // 방금
                    uploadDate = context.getString(R.string.now)
               }else{
                    uploadDate = item.postDate.format(DateTimeFormatter.ofPattern("HH:mm"))
                }
            }else{
                uploadDate = item.postDate.format(DateTimeFormatter.ofPattern("yy.MM.dd"))
            }
            binding.tvPostDate.text = uploadDate

            // 미디어, 좋아요, 댓글 개수
            if(item.mediaNum != 0){
                binding.tvMediaNum.text = item.mediaNum.toString()
                binding.tvMediaNum.visibility = View.VISIBLE
                binding.ivMedia.visibility = View.VISIBLE
            }else{
                binding.tvMediaNum.visibility = View.INVISIBLE
                binding.ivMedia.visibility = View.INVISIBLE
            }
            binding.tvHeartNum.text = item.heartNum.toString()
            binding.tvCommentNum.text = item.commentNum.toString()

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

    fun getListFromView(nList: MutableList<PostingRvItem>){
        itemList = nList
        notifyDataSetChanged()
    }

    fun addItem(item: PostingRvItem){
        itemList.add(item)
        notifyDataSetChanged()
    }

}