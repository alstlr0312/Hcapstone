package com.unity.mynativeapp.Main.community.posting


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.Main.community.PostingRvItem
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvPostingBinding


class PostingRvAdapter(var list: MutableList<PostingRvItem>, val context: Context): RecyclerView.Adapter<PostingRvAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRvPostingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: PostingRvItem){

            //binding.ivProfileImg.setImageURI()
            binding.ivProfileImg.setImageResource(R.drawable.ic_profile_photo_base)

            if(item.postingImg != null){
                binding.ivPostingImg.visibility = View.VISIBLE
                Glide.with(binding.ivPostingImg)
                    .load(item.postingImg)
                    .placeholder(R.drawable.shape_bg_black_rounded)
                    .error(R.drawable.shape_bg_black_rounded)
                    .fallback(R.drawable.shape_bg_black_rounded)
                    .centerCrop()
                    .apply(RequestOptions.centerCropTransform())
                    .into(binding.ivPostingImg)
            }else{
                binding.ivPostingImg.visibility = View.GONE
            }



            binding.tvUsername.text = item.userName

            binding.tvPostingTxt.text = item.postingTxt

            binding.tvHeartNum.text = item.heartNum.toString()
            binding.tvCommentNum.text = item.commentNum.toString()


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostingRvAdapter.ViewHolder {
        return ViewHolder(ItemRvPostingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PostingRvAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}