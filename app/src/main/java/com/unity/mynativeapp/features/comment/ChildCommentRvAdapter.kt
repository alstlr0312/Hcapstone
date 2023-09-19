package com.unity.mynativeapp.features.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvChildCommentBinding
import com.unity.mynativeapp.model.CommentData
import kotlin.properties.Delegates

class ChildCommentRvAdapter(val context: Context): RecyclerView.Adapter<ChildCommentRvAdapter.ViewHolder>() {

    private var itemList = mutableListOf<CommentData>()
    private var parentId = -1

    inner class ViewHolder(val binding: ItemRvChildCommentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: CommentData){

            with(binding){

                Glide.with(ivProfileImg)
                    .load(item.profileImage)
                    .placeholder(R.drawable.ic_profile_photo_base)
                    .error(R.drawable.ic_profile_photo_base)
                    .fallback(R.drawable.ic_profile_photo_base)
                    .apply(RequestOptions.centerCropTransform())
                    .into(ivProfileImg)

                tvUsername.text = item.username
                tvPostDate.text = item.createdAt
                tvCommentContext.text = item.commentContext

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildCommentRvAdapter.ViewHolder {
        return ViewHolder(ItemRvChildCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChildCommentRvAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setItemList(nList: MutableList<CommentData>){
        itemList = nList
        notifyDataSetChanged()
    }


    fun setParentId(id: Int){
        parentId = id
    }
    fun getParentId(): Int{
        return parentId
    }
}