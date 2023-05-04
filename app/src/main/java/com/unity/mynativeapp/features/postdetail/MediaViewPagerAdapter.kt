package com.unity.mynativeapp.features.postdetail

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemVpMediaBinding

class MediaViewPagerAdapter(val context: Context)
    : RecyclerView.Adapter<MediaViewPagerAdapter.ViewHolder>() {

    private var itemList = mutableListOf<Int>()

    inner class ViewHolder(val binding: ItemVpMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(img: Int) {

            Glide.with(binding.ivMedia)
                .load(img)
                .placeholder(R.color.main_black_dark)
                .error(R.color.main_black_dark)
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

    fun getListFromView(nList: MutableList<Int>) {
        itemList = nList
        notifyDataSetChanged()
    }

}