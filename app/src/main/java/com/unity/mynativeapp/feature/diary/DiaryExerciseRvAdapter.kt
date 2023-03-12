package com.unity.mynativeapp.feature.diary


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.databinding.ItemRvDiaryExerciseBinding
import com.unity.mynativeapp.model.DiaryExerciseRvItem


class DiaryExerciseRvAdapter(var list: MutableList<DiaryExerciseRvItem>, var context: Context)
    : RecyclerView.Adapter<DiaryExerciseRvAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRvDiaryExerciseBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: DiaryExerciseRvItem){

            binding.checkbox.isChecked = item.isChecked == true

            binding.tvExerciseName.text = item.name

            if(item.numbers != null){
                binding.tvExerciseNumbers.text = item.numbers.toString()
                binding.layoutExerciseNumbers.visibility = View.VISIBLE
            }else{
                binding.layoutExerciseNumbers.visibility = View.GONE
            }

            if(item.sets != null){
                binding.tvExerciseSet.text = item.sets.toString()
                binding.layoutExerciseSet.visibility = View.VISIBLE
            }else{
                binding.layoutExerciseSet.visibility = View.GONE
            }

            if(item.times != null){
                binding.tvExerciseTime.text = item.times.toString()
                binding.layoutExerciseTime.visibility = View.VISIBLE
            }else{
                binding.layoutExerciseTime.visibility = View.GONE
            }

            binding.checkbox.setOnClickListener {
                //
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvDiaryExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }



}