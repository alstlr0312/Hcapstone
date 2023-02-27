package com.unity.mynativeapp.Main.home.Calender.Diary.DairyMediaRv

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.Main.home.Calender.diaryActivity
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvDiaryMediaBinding


class DiaryMediaRvAdapter(val context: Context)
    : RecyclerView.Adapter<DiaryMediaRvAdapter.ViewHolder>(){

    var itemList = mutableListOf<Uri>()
    inner class ViewHolder(val binding: ItemRvDiaryMediaBinding): RecyclerView.ViewHolder(binding.root){

        init{
            binding.root.setOnLongClickListener OnLongClickListener@{
                var dialog = Dialog(context)
                dialog.setContentView(R.layout.dialog_delete)
                dialog.show()

                diaryActivity.resizeDialog(dialog, 0.85, 0.28)
                var btnYes = dialog.findViewById<TextView>(R.id.btn_yes)
                var btnNo = dialog.findViewById<TextView>(R.id.btn_no)

                btnYes.setOnClickListener {
                    itemList.removeAt(adapterPosition)
                    dialog.dismiss()
                    notifyDataSetChanged()
                }
                btnNo.setOnClickListener {
                    dialog.dismiss()
                }

                return@OnLongClickListener true
            }
        }
        fun bind(imageUri: Uri){
            Glide.with(binding.photo)
                .load(imageUri)
                .placeholder(R.drawable.shape_bg_black_rounded)
                .error(R.drawable.shape_bg_black_rounded)
                .fallback(R.drawable.shape_bg_black_rounded)
                .override(430, 430)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.photo)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryMediaRvAdapter.ViewHolder {
        return ViewHolder(ItemRvDiaryMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DiaryMediaRvAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getListFromView(nList: MutableList<Uri>){
        itemList = nList
    }

    fun addItem(imageUri: Uri){
        itemList.add(imageUri)
        notifyDataSetChanged()
    }
}