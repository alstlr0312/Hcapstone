package com.unity.mynativeapp.features.diary


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.MyApplication.Companion.bodyPartHashMap
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvDiaryExerciseBinding
import com.unity.mynativeapp.model.DiaryExerciseRvItem
import com.unity.mynativeapp.model.DiaryWriteJson
import com.unity.mynativeapp.util.DeleteDialog


class DiaryExerciseRvAdapter(var context: Context)
    : RecyclerView.Adapter<DiaryExerciseRvAdapter.ViewHolder>() {

    private var itemList = mutableListOf<DiaryExerciseRvItem>()
    private var bindingList = mutableListOf<ItemRvDiaryExerciseBinding>()

    inner class ViewHolder(val binding: ItemRvDiaryExerciseBinding): RecyclerView.ViewHolder(binding.root){

        init{
            bindingList.add(binding)
            binding.root.setOnLongClickListener OnLongClickListener@{
                var dialog = DeleteDialog(context, context.getString(R.string.delete_exercise))
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
        fun bind(item: DiaryExerciseRvItem){

            binding.checkbox.isChecked = item.finished

            if(item.exerciseName != ""){
                binding.tvExerciseName.text = item.bodyPart + " - " + item.exerciseName
            }else{
                binding.tvExerciseName.text = item.bodyPart
            }

            if(item.reps != null){
                binding.tvExerciseNumbers.text = item.reps.toString()
                binding.layoutExerciseNumbers.visibility = View.VISIBLE
            }else{
                binding.layoutExerciseNumbers.visibility = View.GONE
            }

            if(item.exSetCount != null){
                binding.tvExerciseSet.text = item.exSetCount.toString()
                binding.layoutExerciseSet.visibility = View.VISIBLE
            }else{
                binding.layoutExerciseSet.visibility = View.GONE
            }

            if(item.cardioTime != null){
                binding.tvExerciseTime.text = item.cardioTime.toString()
                binding.layoutExerciseTime.visibility = View.VISIBLE
            }else{
                binding.layoutExerciseTime.visibility = View.GONE
            }


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
            for(item in bindingList){
                item.checkbox.isEnabled = b
            }
            notifyDataSetChanged()
        }
    }

    fun getExerciseList(): List<DiaryExerciseRvItem> {
        var dataList = mutableListOf<DiaryExerciseRvItem>()
        for(i in itemList){
            var item = i
            item.bodyPart = bodyPartHashMap[i.bodyPart].toString()
            dataList.add(item)
        }
        Log.d("dataList", dataList.toString())
        return dataList
    }

}