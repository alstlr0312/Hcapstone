package com.unity.mynativeapp.features.diary

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.R


class DiaryMediaRvAdapter2(val context: Context)
    : RecyclerView.Adapter<DiaryMediaRvAdapter2.ViewHolder>() {
    private var itemList = mutableListOf<ByteArray>()

    class ViewHolder(val binding: View) :RecyclerView.ViewHolder(binding) {
        val byteArrayImageView: ImageView = itemView.findViewById(R.id.photo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv_media,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        val bitmap = BitmapFactory.decodeByteArray(item, 0, item.size)
        holder.byteArrayImageView.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItem(byteArray: ByteArray) {
        itemList.add(byteArray)
        notifyDataSetChanged()
    }
}