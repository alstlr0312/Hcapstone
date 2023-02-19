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
            binding.tvReadMoreLess.visibility = View.GONE
            binding.tvPostingTxt.post{
                val lineCount = binding.tvPostingTxt.layout.lineCount
                if(lineCount > 0){
                    if(binding.tvPostingTxt.layout.getEllipsisCount(lineCount-1) > 0) {
                        binding.tvReadMoreLess.visibility = View.VISIBLE

                        binding.tvReadMoreLess.setOnClickListener {
                            if(binding.tvPostingTxt.maxLines == 2){
                                binding.tvReadMoreLess.text = context.getText(R.string.read_less)
                                binding.tvPostingTxt.maxLines = Int.MAX_VALUE
                            }else{
                                binding.tvReadMoreLess.text = context.getText(R.string.read_more)
                                binding.tvPostingTxt.maxLines = 2
                            }
                        }
                    }
                }
            }


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