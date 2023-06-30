package com.unity.mynativeapp.features.diary


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.MyApplication.Companion.bodyPartHashMap
import com.unity.mynativeapp.MyApplication.Companion.bodyPartKorHashMap
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvDiaryExerciseBinding
import com.unity.mynativeapp.model.ExerciseItem
import com.unity.mynativeapp.network.util.DeleteDialog


class DiaryExerciseRvAdapter(var context: Context)
    : RecyclerView.Adapter<DiaryExerciseRvAdapter.ViewHolder>() {

    private var itemList = mutableListOf<ExerciseItem>()
    private var bindingList = mutableListOf<ItemRvDiaryExerciseBinding>()

    inner class ViewHolder(val binding: ItemRvDiaryExerciseBinding): RecyclerView.ViewHolder(binding.root){

        init{
            bindingList.add(binding)
            binding.root.setOnLongClickListener OnLongClickListener@{
                var dialog = DeleteDialog(context, context.getString(R.string.you_want_delete_exercise))
                dialog.show()

                var btnYes = dialog.findViewById<TextView>(R.id.btn_yes)
                var btnNo = dialog.findViewById<TextView>(R.id.btn_no)

                btnYes.setOnClickListener {
                    itemList.removeAt(adapterPosition)
                    bindingList.remove(binding)
                    dialog.dismiss()
                    notifyDataSetChanged()
                }
                btnNo.setOnClickListener {
                    dialog.dismiss()
                }

                return@OnLongClickListener true
            }
        }
        fun bind(item: ExerciseItem){

            binding.checkbox.isChecked = item.finished
            val part = bodyPartKorHashMap[item.bodyPart]
            if(part != null){
                item.bodyPart = part
            }


            if(item.cardio == true){
                item.bodyPart = context.getString(R.string.exercise_cardio)

                binding.layoutExerciseNumbers.visibility = View.GONE
                binding.layoutExerciseSet.visibility = View.GONE

                binding.tvExerciseTime.text = item.cardioTime.toString()
                binding.layoutExerciseTime.visibility = View.VISIBLE

            }else{
                binding.tvExerciseNumbers.text = item.reps.toString()
                binding.layoutExerciseNumbers.visibility = View.VISIBLE

                binding.tvExerciseSet.text = item.exSetCount.toString()
                binding.layoutExerciseSet.visibility = View.VISIBLE

                binding.layoutExerciseTime.visibility = View.GONE
            }

            if(item.exerciseName != ""){
                binding.tvExerciseName.text = item.bodyPart + " - " + item.exerciseName
            }else{
                binding.tvExerciseName.text = item.bodyPart
            }

            binding.checkbox.setOnClickListener {
                itemList[adapterPosition].finished = binding.checkbox.isChecked
                diaryActivity.edited = true
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

    fun getListFormView(nList: MutableList<ExerciseItem>){
        itemList = nList
    }

    fun addItem(item: ExerciseItem){
        itemList.add(item)
        notifyDataSetChanged()
    }



    fun getExerciseList(): List<ExerciseItem> {
        var dataList = mutableListOf<ExerciseItem>()
        for(i in itemList){
            var item = i
            item.bodyPart = bodyPartHashMap[i.bodyPart].toString()
            dataList.add(item)
        }
        Log.d("dataList", dataList.toString())
        return dataList
    }

}