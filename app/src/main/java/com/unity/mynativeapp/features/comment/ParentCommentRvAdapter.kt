package com.unity.mynativeapp.features.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvParentCommentBinding
import com.unity.mynativeapp.model.CommentData
import com.unity.mynativeapp.model.PostItem

class ParentCommentRvAdapter(val context: Context): RecyclerView.Adapter<ParentCommentRvAdapter.ViewHolder>() {

    private var itemList = mutableListOf<CommentData>()

    inner class ViewHolder(val binding: ItemRvParentCommentBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: CommentData){

            with(binding){

                if(item.profileImage!=null){
                    Glide.with(ivProfileImg)
                        .load(item.profileImage)
                        .placeholder(R.drawable.ic_profile_photo_base)
                        .error(R.drawable.ic_profile_photo_base)
                        .fallback(R.drawable.ic_profile_photo_base)
                        .apply(RequestOptions.centerCropTransform())
                        .into(ivProfileImg)
                }

                tvUsername.text = item.username
                tvPostDate.text = item.createdAt
                tvCommentContext.text = item.commentContext

                if(item.childCount > 0){ // 자식 댓글이 있으면
                    layoutInvisibleChildComment.visibility = View.VISIBLE
                    tvChildCommentNum.text = item.childCount.toString()
                    layoutVisibleChildComment.visibility = View.GONE
                }else{
                    layoutInvisibleChildComment.visibility = View.GONE
                    layoutVisibleChildComment.visibility = View.GONE
                }
            }

            // 답글 보기 클릭
            binding.layoutInvisibleChildComment.setOnClickListener {
                // 자식 댓글 조회 요청

                var childNum = item.childCount
                val childCommentList = mutableListOf<CommentData>()
//                while(childNum != 0){
//                    childCommentList.add(CommentData(-1, "대댓글$childNum...", "방금", null, "user0$childNum"))
//                    childNum -= 1
//                }
                binding.rvChildComment.adapter = ChildCommentRvAdapter(childCommentList, context)

                it.visibility = View.GONE
                binding.layoutVisibleChildComment.visibility = View.VISIBLE
            }

            // 답글 닫기 클릭
            binding.tvCloseChildComment.setOnClickListener {
                binding.layoutVisibleChildComment.visibility = View.GONE
                binding.layoutInvisibleChildComment.visibility = View.VISIBLE
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentCommentRvAdapter.ViewHolder {
        return ViewHolder(ItemRvParentCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ParentCommentRvAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getListFromView(nList: MutableList<CommentData>){
        itemList = nList
        notifyDataSetChanged()
    }

    fun removeAllItem(){
        itemList = mutableListOf()
        notifyDataSetChanged()
    }

    fun addItem(item: CommentData){
        itemList.add(item)
        notifyItemChanged(itemCount-1)
    }
}