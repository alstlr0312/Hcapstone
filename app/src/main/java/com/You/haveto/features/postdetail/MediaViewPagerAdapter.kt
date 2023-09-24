package com.You.haveto.features.postdetail

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.You.haveto.R
import com.You.haveto.databinding.ItemVpMediaBinding

class MediaViewPagerAdapter(val context: Context)
    : RecyclerView.Adapter<MediaViewPagerAdapter.ViewHolder>() {

    private var itemList = mutableListOf<Bitmap>()

    inner class ViewHolder(val binding: ItemVpMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap) {
            Glide.with(binding.ivMedia)
                .load(bitmap)
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
        for(url in nList){ // url > bitmap 변환 후 리스트에 추가
            Glide.with(context)
                .asBitmap()
                .load(url)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        itemList.add(resource)
                        notifyItemRemoved(itemCount-1)
                    }
                })
        }
    }


    fun removeAllItem() {
        itemList = mutableListOf()
        notifyDataSetChanged()
    }
}