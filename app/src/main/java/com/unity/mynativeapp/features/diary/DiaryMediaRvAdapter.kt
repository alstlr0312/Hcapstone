package com.unity.mynativeapp.features.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvMediaBinding
import com.unity.mynativeapp.features.media.MediaFullActivity
import com.unity.mynativeapp.model.MediaRvItem
import com.unity.mynativeapp.network.util.SimpleDialog


class DiaryMediaRvAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var itemList = mutableListOf<MediaRvItem>()
    // 다이어리 작성할 때 미디어 타입 -> Uri
    inner class ViewHolder_post(val binding: ItemRvMediaBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnLongClickListener OnLongClickListener@{
                val dialog = SimpleDialog(context, context.getString(R.string.you_want_delete_media))
                dialog.show()

                dialog.setOnDismissListener {
                    if(dialog.resultCode == 1){
                        itemList.removeAt(adapterPosition)
                        notifyDataSetChanged()
                    }
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

    // 다이어리 상세조회할 때 받아서 출력하는 미디어 타입 -> URL
    inner class ViewHolder_get(val binding: ItemRvMediaBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnLongClickListener OnLongClickListener@{
                val dialog = SimpleDialog(context, context.getString(R.string.you_want_delete_media))
                dialog.show()

                dialog.setOnDismissListener {
                    if(dialog.resultCode == 1){
                        itemList.removeAt(adapterPosition)
                        notifyDataSetChanged()
                    }
                }

                return@OnLongClickListener true
            }
        }
        fun bind_get(item: MediaRvItem){

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
                setImage(it, binding)
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
        if(holder is ViewHolder_post) itemList[position].uri?.let{holder.bind_post(itemList[position])}
        else if(holder is ViewHolder_get) itemList[position].url?.let{holder.bind_get(itemList[position])}
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItem(media: MediaRvItem) {
        itemList.add(media)
        notifyDataSetChanged()

    }

    fun getMediaList(): List<MediaRvItem>{
        return itemList
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
            .override(430, 430)
            .apply(RequestOptions.centerCropTransform())
            .into(binding.photo)
    }
}