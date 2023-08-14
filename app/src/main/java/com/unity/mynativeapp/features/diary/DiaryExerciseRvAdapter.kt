package com.unity.mynativeapp.features.diary


import android.content.Context
import android.content.Intent
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
import com.unity.mynativeapp.network.util.SimpleDialog


class DiaryExerciseRvAdapter(var context: Context, val listener: OnEditDiary)
    : RecyclerView.Adapter<DiaryExerciseRvAdapter.ViewHolder>() {

    private var itemList = mutableListOf<ExerciseItem>()
    private var bindingList = mutableListOf<ItemRvDiaryExerciseBinding>()
    inner class ViewHolder(val binding: ItemRvDiaryExerciseBinding): RecyclerView.ViewHolder(binding.root){

        init{
            bindingList.add(binding)
            binding.root.setOnLongClickListener OnLongClickListener@{
                val dialog = SimpleDialog(context, context.getString(R.string.you_want_delete_exercise))
                dialog.show()

                dialog.setOnDismissListener {
                    if(dialog.resultCode == 1){
                        itemList.removeAt(adapterPosition)
                        bindingList.remove(binding)
                        notifyItemRemoved(adapterPosition)
                        listener.diaryEditListener()
                    }
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


            if(item.cardio){
                item.bodyPart = context.getString(R.string.exercise_cardio)

                binding.tvExerciseNumbers.visibility = View.GONE
                binding.tvExerciseSet.visibility = View.GONE

                binding.tvExerciseTime.text = "${item.cardioTime}분"
                binding.tvExerciseTime.visibility = View.VISIBLE

            }else{
                binding.tvExerciseNumbers.text = "${item.reps}회"
                binding.tvExerciseNumbers.visibility = View.VISIBLE

                binding.tvExerciseSet.text = "${item.exSetCount}세트"
                binding.tvExerciseSet.visibility = View.VISIBLE

                binding.tvExerciseTime.visibility = View.GONE
            }

            binding.tvExerciseName.text = "${item.exerciseName} - ${item.bodyPart}"

            binding.checkbox.setOnClickListener {
                itemList[adapterPosition].finished = binding.checkbox.isChecked
                listener.diaryEditListener()
            }

            // 운동 자극 피드백 페이지로 이동
            binding.root.setOnClickListener {
                val intent = Intent(context, FeedbackPostureActivity::class.java)
                intent.putExtra("exerciseName", item.exerciseName)
                intent.putExtra("bodyPart", item.bodyPart)
                context.startActivity(intent)
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

    fun setDataList(list: List<ExerciseItem>){
        for(data in list){
            itemList.add(data)
        }
        notifyDataSetChanged()

    }
    fun addItem(item: ExerciseItem){
        listener.diaryEditListener()

        itemList.add(item)
        notifyItemChanged(itemCount-1)

    }

    fun getExerciseList(): List<ExerciseItem> {
        return itemList
    }

}