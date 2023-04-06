package com.unity.mynativeapp.features.postdetail

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvMediaBinding
import com.unity.mynativeapp.features.postwrite.PostWriteMediaRvAdapter
import com.unity.mynativeapp.util.DeleteDialog

class PostDetailMediaRvAdapter(val context: Context)
    : RecyclerView.Adapter<PostDetailMediaRvAdapter.ViewHolder>() {

    private var itemList = mutableListOf<Int>()

    inner class ViewHolder(val binding: ItemRvMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(img: Int) {

            Glide.with(binding.photo)
                .load(img)
                .placeholder(R.drawable.shape_bg_black_rounded)
                .error(R.drawable.shape_bg_black_rounded)
                .fallback(R.drawable.shape_bg_black_rounded)
                .override(280, 280)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRvMediaBinding.inflate(
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


    private fun getRealPathFromUri(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if (buildName.equals("Xiaomi")) {
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }
}