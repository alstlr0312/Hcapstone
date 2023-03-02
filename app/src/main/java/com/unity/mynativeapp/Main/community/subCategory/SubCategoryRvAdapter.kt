package com.unity.mynativeapp.Main.community.subCategory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemTvSubCatogoryBinding


class SubCategoryAdapter(val context: Context): RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    var itemList = listOf<String>()
    var bindingList = mutableListOf<ItemTvSubCatogoryBinding>()

    inner class ViewHolder(val binding: ItemTvSubCatogoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(title: String){

            binding.category.text = title


        }
        init {
            binding.category.isChecked = bindingList.size == 0
            bindingList.add(binding)

            //binding.category.setOnCheckedChangeListener { button, b ->
            //
            //            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryAdapter.ViewHolder {
        return ViewHolder(ItemTvSubCatogoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SubCategoryAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getListFromView(nList: List<String>){
        itemList = nList
        notifyDataSetChanged()
    }
}