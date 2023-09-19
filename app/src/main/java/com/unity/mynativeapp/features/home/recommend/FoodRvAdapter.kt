package com.unity.mynativeapp.features.home.recommend

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.databinding.ItemRvEdttextBinding

class FoodRvAdapter(val context: Context): RecyclerView.Adapter<FoodRvAdapter.ViewHolder>() {

    private var itemList = mutableListOf<String>()

    inner class ViewHolder(val binding: ItemRvEdttextBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(food: String){
            binding.edtText.setText(food)
        }

        init{
            binding.edtText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    itemList[adapterPosition] = binding.edtText.text.toString()
                }
            })

            binding.ivCancel.setOnClickListener {

                itemList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                Log.d("food", itemList.toString())
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvEdttextBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItem(){
        itemList.add("")
        notifyItemChanged(itemCount-1)
    }

    fun getFoodList(): ArrayList<String>{
        val foodList = mutableListOf<String>()
        for(item in itemList){
            if(item != ""){
                foodList.add(item)
            }
        }
        itemList = foodList
        notifyDataSetChanged()
        return itemList as ArrayList<String>

    }


}