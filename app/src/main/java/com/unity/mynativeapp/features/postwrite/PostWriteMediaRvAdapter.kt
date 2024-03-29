package com.unity.mynativeapp.features.postwrite

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvMediaBinding
import com.unity.mynativeapp.features.media.MediaFullActivity
import com.unity.mynativeapp.model.MediaRvItem
import com.unity.mynativeapp.network.util.SimpleDialog


class PostWriteMediaRvAdapter(val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var itemList = mutableListOf<MediaRvItem>()

    // 새로 추가한 미디어 타입 -> Uri
    inner class ViewHolder_post(val binding: ItemRvMediaBinding): RecyclerView.ViewHolder(binding.root){
        fun bind_post(uriItem: MediaRvItem){
            binding.iconCancel.visibility = View.VISIBLE
            when(uriItem.viewType){
                1 -> { // 사진
                    binding.iconPlay.visibility = View.GONE

                    binding.root.setOnClickListener {
                        val intent = Intent(context, MediaFullActivity::class.java)
                        intent.putExtra("url", uriItem.uri)
                        intent.putExtra("viewType", uriItem.viewType)
                        context.startActivity(intent)
                    }
                }
                2 -> { // 동영상
                    binding.iconPlay.visibility = View.VISIBLE
                }
            }

            setImage(uriItem.uri!!, binding)

            // 미디어 크기 보기
            binding.root.setOnClickListener {
                val intent = Intent(context, MediaFullActivity::class.java)
                intent.putExtra("uri", uriItem.uri.toString())
                intent.putExtra("viewType", uriItem.viewType)
                context.startActivity(intent)
            }

            // 미디어 삭제
            /*
            binding.root.setOnLongClickListener OnLongClickListener@{
                val dialog = SimpleDialog(context, context.getString(R.string.you_want_delete_media))
                dialog.show()

                dialog.setOnDismissListener {
                    if(dialog.resultCode == 1){
                        itemList.removeAt(adapterPosition)
                        dialog.dismiss()
                        notifyItemRemoved(adapterPosition)
                    }
                }
                return@OnLongClickListener true
            }
             */
            binding.iconCancel.setOnClickListener {
                val dialog = SimpleDialog(context, context.getString(R.string.you_want_delete_media))
                dialog.show()

                dialog.setOnDismissListener {
                    if(dialog.resultCode == 1){
                        itemList.removeAt(adapterPosition)
                        dialog.dismiss()
                        notifyItemRemoved(adapterPosition)

                    }
                }
            }

        }
    }

    // 서버로부터 받은 미디어 타입 -> url
    inner class ViewHolder_get(val binding: ItemRvMediaBinding): RecyclerView.ViewHolder(binding.root){

        fun bind_get(item: MediaRvItem){
            binding.iconCancel.visibility = View.VISIBLE
            item.url?.let{
                when(item.viewType){
                    3 -> { // 사진
                        binding.iconPlay.visibility = View.GONE
                        binding.root.setOnClickListener {
                            val intent = Intent(context, MediaFullActivity::class.java)
                            intent.putExtra("url", item.url)
                            intent.putExtra("viewType", item.viewType)
                            context.startActivity(intent)
                        }
                    }
                    4 -> { // 동영상
                        binding.iconPlay.visibility = View.VISIBLE
                    }
                }
                // 미디어 세팅
                setImage(it, binding)
                // 미디어 삭제

            }
            binding.iconCancel.setOnClickListener {
                val dialog = SimpleDialog(context, context.getString(R.string.you_want_delete_media))
                dialog.show()

                dialog.setOnDismissListener {
                    if(dialog.resultCode == 1){
                        itemList.removeAt(adapterPosition)
                        dialog.dismiss()
                        notifyItemRemoved(adapterPosition)
                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1 -> ViewHolder_post(ItemRvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            2 -> ViewHolder_post(ItemRvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            3 -> ViewHolder_get(ItemRvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            4 -> ViewHolder_get(ItemRvMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw RuntimeException("DiaryMediaRvAdapter onCreateViewHolder ERROR")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PostWriteMediaRvAdapter.ViewHolder_post) holder.bind_post(itemList[position])
        else if(holder is PostWriteMediaRvAdapter.ViewHolder_get) itemList[position].url?.let{holder.bind_get(itemList[position])}
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItem(media: MediaRvItem){
        itemList.add(media)
        notifyItemRemoved(itemCount-1)
    }

    fun getMediaList(): List<MediaRvItem>{
        return itemList
    }

    fun setMediaList(mList: List<String>) {

        for(itemIdx in mList.indices){ // url > bitmap 변환 후 리스트에 추가
            Glide.with(context)
                .asBitmap()
                .load(mList[itemIdx])
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        itemList.add(MediaRvItem(3, null, mList[itemIdx], resource))
                        notifyItemRemoved(itemCount-1)

                        if(itemIdx == mList.size-1){
                        }
                    }
                })
        }

    }

    override fun getItemViewType(position: Int):Int {
        if(itemList[position].viewType == 1)
            return 1
        else if(itemList[position].viewType == 2)
            return 2
        else if(itemList[position].viewType == 3)
            return 3
        else return 4
    }

    private fun setImage(image: Any, binding: ItemRvMediaBinding){
        Glide.with(binding.photo)
            .load(image)
            .placeholder(R.drawable.shape_bg_black_rounded)
            .error(R.drawable.ic_image_failed)
            .fallback(R.drawable.shape_bg_black_rounded)
            .override(300, 300)
            .apply(RequestOptions.centerCropTransform())
            .into(binding.photo)
    }



    companion object {
        const val TAG = "PostWriteMediaRvAdapterLog"
    }


}