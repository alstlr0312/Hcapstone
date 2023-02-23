package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv


import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.Main.home.Calender.diaryActivity
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvDiaryExerciseBinding


class DiaryExerciseRvAdapter(var context: Context)
    : RecyclerView.Adapter<DiaryExerciseRvAdapter.ViewHolder>() {

    var itemList = mutableListOf<DiaryExerciseRvItem>()
    inner class ViewHolder(val binding: ItemRvDiaryExerciseBinding): RecyclerView.ViewHolder(binding.root){

        init{
            binding.root.setOnLongClickListener OnLongClickListener@{
                var dialog = Dialog(context)
                dialog.setContentView(R.layout.dialog_remove_diary_exercise)
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
        fun bind(item: DiaryExerciseRvItem){

            binding.checkbox.isChecked = item.finished

            if(item.exerciseName != ""){
                binding.tvExerciseName.text = item.bodyPart + " - " + item.exerciseName
            }else{
                binding.tvExerciseName.text = item.bodyPart
            }

            if(item.reps != 0){
                binding.tvExerciseNumbers.text = item.reps.toString()
                binding.layoutExerciseNumbers.visibility = View.VISIBLE
            }else{
                binding.layoutExerciseNumbers.visibility = View.GONE
            }

            if(item.exSetCount != 0){
                binding.tvExerciseSet.text = item.exSetCount.toString()
                binding.layoutExerciseSet.visibility = View.VISIBLE
            }else{
                binding.layoutExerciseSet.visibility = View.GONE
            }

            if(item.cardioTime != 0){
                binding.tvExerciseTime.text = item.cardioTime.toString()
                binding.layoutExerciseTime.visibility = View.VISIBLE
            }else{
                binding.layoutExerciseTime.visibility = View.GONE
            }

            binding.checkbox.isClickable = item.isClickable == true

            binding.checkbox.setOnClickListener {
                itemList[adapterPosition].finished = binding.checkbox.isChecked
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvDiaryExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getListFormView(nList: MutableList<DiaryExerciseRvItem>){
        itemList = nList
    }

    fun addItem(item: DiaryExerciseRvItem){
        itemList.add(item)
        notifyDataSetChanged()
    }

    fun checkBoxIsClickable(b: Boolean){
        if(itemCount != 0){
            for(item in itemList){
                item.isClickable = b
            }
            notifyDataSetChanged()
        }
    }

}