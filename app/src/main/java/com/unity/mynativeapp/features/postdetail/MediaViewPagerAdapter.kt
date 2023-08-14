package com.unity.mynativeapp.features.postdetail

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemVpMediaBinding
import com.unity.mynativeapp.model.OnCommentClick

class MediaViewPagerAdapter(val context: Context)
    : RecyclerView.Adapter<MediaViewPagerAdapter.ViewHolder>() {

    private var itemList = mutableListOf<String>()

    inner class ViewHolder(val binding: ItemVpMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) {

            Glide.with(binding.ivMedia)
                .load(url)
                .placeholder(R.color.main_black_dark)
                .error(R.drawable.ic_image_failed)
                .fallback(R.color.main_black_dark)
                .centerCrop()
                .apply(RequestOptions.centerCropTransform())
                .into(binding.ivMedia)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVpMediaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setMediaList(nList: ArrayList<String>) {
        itemList = nList
        notifyDataSetChanged()
    }

    fun addItem(url: String) {
        itemList.add(url)
        notifyItemChanged(itemList.size-1)
    }
    fun removeAllItem() {
        itemList = mutableListOf()
        notifyDataSetChanged()
    }
}