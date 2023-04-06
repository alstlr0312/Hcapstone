package com.unity.mynativeapp.features.diary

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.ByteArrayAdapter
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvDiaryMediaBinding
import com.unity.mynativeapp.util.DeleteDialog
import retrofit2.http.Multipart

class DiaryMediaRvAdapter2(val context: Context) :
    RecyclerView.Adapter<DiaryMediaRvAdapter2.ViewHolder>() {
    private var itemList = mutableListOf<ByteArray>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val byteArrayImageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_byte_array, parent, false)
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