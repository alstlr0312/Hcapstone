package com.unity.mynativeapp.features.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.databinding.ItemRvCommentBinding
import com.unity.mynativeapp.model.CommentDto

class CommentRvAdapter(val context: Context): RecyclerView.Adapter<CommentRvAdapter.ViewHolder>() {

    private var itemList = mutableListOf<CommentDto>()

    inner class ViewHolder(val binding: ItemRvCommentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: CommentDto){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRvAdapter.ViewHolder {
        return ViewHolder(ItemRvCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentRvAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun getListFromView(nList: MutableList<CommentDto>){
        itemList = nList
        notifyDataSetChanged()
    }
}