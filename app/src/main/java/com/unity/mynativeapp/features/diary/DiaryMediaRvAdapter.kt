package com.unity.mynativeapp.features.diary

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.unity.mynativeapp.model.MediaRvItem
import com.unity.mynativeapp.util.DeleteDialog


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
        fun bind(imageUri: Uri){

            pathList.add(getRealPathFromUri(imageUri))
            //println("이미지 경로 : $pathList")

            setImage(imageUri, binding)
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
        fun bind(bitmap: Bitmap){
            //val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            //binding.photo.setImageBitmap(bitmap)

            setImage(bitmap, binding)



        }
    }

    // 다이어리 수정할 때 보내야 하는 미디어 타입 -> String(imagePath): 받은 미디어를 갤러리에 저장 후 불러와서 path 변환해서 요청
    inner class ViewHolder_patch(val binding: ItemRvMediaBinding): RecyclerView.ViewHolder(binding.root) {
        init {
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

        fun bind(imageUri: Uri) {

            //pathList.add(getRealPathFromUri(imageUri))
            //println("이미지 경로 : $pathList")

            setImage(imageUri, binding)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1 -> ViewHolder_post(ItemRvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            2 -> ViewHolder_get(ItemRvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw RuntimeException("DiaryMediaRvAdapter onCreateViewHolder ERROR")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder_post) itemList[position].uri?.let { holder.bind(it) }
        else if(holder is ViewHolder_get) itemList[position].bitmap?.let { holder.bind(it) }
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
            .error(R.drawable.shape_bg_black_rounded)
            .fallback(R.drawable.shape_bg_black_rounded)
            .override(430, 430)
            .apply(RequestOptions.centerCropTransform())
            .into(binding.photo)
    }
}