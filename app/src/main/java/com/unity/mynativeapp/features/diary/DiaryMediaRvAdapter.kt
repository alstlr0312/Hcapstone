package com.unity.mynativeapp.features.diary

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvMediaBinding
import com.unity.mynativeapp.features.media.MediaFullActivity
import com.unity.mynativeapp.model.MediaRvItem
import com.unity.mynativeapp.network.util.DeleteDialog



class DiaryMediaRvAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var itemList = mutableListOf<MediaRvItem>()
    private var pathList = mutableListOf<String>()

    // 다이어리 작성할 때 미디어 타입 -> Uri를 path로 변경하여 요청
    inner class ViewHolder_post(val binding: ItemRvMediaBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnLongClickListener OnLongClickListener@{
                var dialog = DeleteDialog(context, context.getString(R.string.delete_media))
                dialog.show()

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
        fun bind_post(uriItem: MediaRvItem){

            when(uriItem.viewType){
                1 -> { // 사진
                    binding.iconPlay.visibility = View.GONE
                }
                2 -> { // 동영상
                    binding.iconPlay.visibility = View.VISIBLE
                }
            }

            setImage(uriItem.uri!!, binding)

            binding.root.setOnClickListener {
                val intent = Intent(context, MediaFullActivity::class.java)
                intent.putExtra("uri", uriItem.uri.toString())
                intent.putExtra("viewType", uriItem.viewType)
                context.startActivity(intent)
            }

        }
    }

    // 다이어리 상세조회할 때 받아서 출력하는 미디어 타입 -> bitmap
    inner class ViewHolder_get(val binding: ItemRvMediaBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnLongClickListener OnLongClickListener@{
                var dialog = DeleteDialog(context, context.getString(R.string.delete_media))
                dialog.show()

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
        fun bind_get(bitmapItem: MediaRvItem){
            //val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            //binding.photo.setImageBitmap(bitmap)

            bitmapItem.bitmap?.let{
                when(bitmapItem.viewType){
                    3 -> { // 사진
                        binding.iconPlay.visibility = View.GONE
                        binding.root.setOnClickListener {
                            val intent = Intent(context, MediaFullActivity::class.java)
                            intent.putExtra("bitmap", bitmapItem.bitmap)
                            intent.putExtra("viewType", bitmapItem.viewType)
                            context.startActivity(intent)
                        }
                    }
                    4 -> { // 동영상
                        binding.iconPlay.visibility = View.VISIBLE
                    }
                }


                setImage(it, binding)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1 -> ViewHolder_post(ItemRvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            2 -> ViewHolder_post(ItemRvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            3 -> ViewHolder_get(ItemRvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw RuntimeException("DiaryMediaRvAdapter onCreateViewHolder ERROR")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder_post) itemList[position].uri?.let{holder.bind_post(itemList[position])}
        else if(holder is ViewHolder_get) itemList[position].bitmap?.let{holder.bind_get(itemList[position])}
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItem(media: MediaRvItem){

        itemList.add(media)
        notifyDataSetChanged()
    }

    fun getMediaList(): List<MediaRvItem>{
        return itemList
    }

    fun getByteToPathList(): List<MediaRvItem>{
        return itemList
    }
    override fun getItemViewType(position: Int):Int {
        if(itemList[position].viewType == 1)
            return 1
        else if(itemList[position].viewType == 2)
            return 2
        else return 3
    }


    fun getRealPathFromUri(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")){
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, proj, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

    private fun setImage(image: Any, binding: ItemRvMediaBinding){
        Glide.with(binding.photo)
            .load(image)
            .placeholder(R.drawable.shape_bg_black_rounded)
            .error(R.drawable.ic_image_failed)
            .fallback(R.drawable.shape_bg_black_rounded)
            .override(430, 430)
            .apply(RequestOptions.centerCropTransform())
            .into(binding.photo)
    }
}