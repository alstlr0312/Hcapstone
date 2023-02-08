package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryPhotoRv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvDiaryPhotoBinding


class DiaryPhotoRvAdapter(var list: MutableList<DiaryPhotoRvItem>, val context: Context)
    : RecyclerView.Adapter<DiaryPhotoRvAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemRvDiaryPhotoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: DiaryPhotoRvItem){
            Glide.with(binding.photo)
                .load(item.image)
                .placeholder(R.drawable.shape_bg_black_rounded)
                .error(R.drawable.shape_bg_black_rounded)
                .fallback(R.drawable.shape_bg_black_rounded)
                .override(250, 250)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.photo)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryPhotoRvAdapter.ViewHolder {
        return ViewHolder(ItemRvDiaryPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DiaryPhotoRvAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}