package com.unity.mynativeapp.features.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvCalenderBinding
import com.unity.mynativeapp.features.diary.DiaryActivity
import com.unity.mynativeapp.model.CalenderRvItem
import com.unity.mynativeapp.network.util.DeleteDialog
import java.time.format.DateTimeFormatter


class CalenderRvAdapter(val context: Context, listener: OnItemClick): RecyclerView.Adapter<CalenderRvAdapter.ViewHolder>(){

    var itemList = mutableListOf<CalenderRvItem>()
    lateinit var selectedDayBinding: ItemRvCalenderBinding
    var selectedDayIdx: Int = -1
    private val diaryDeleteCallback = listener

    inner class ViewHolder(val binding: ItemRvCalenderBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: CalenderRvItem){

            if(item.exerciseDate == null){
                binding.tvDay.text = ""
            }else{
                binding.tvDay.text = item.exerciseDate.dayOfMonth.toString()
            }

            // 선택한 날짜 focus
            if(item.isSelectedDay == true){
                selectedDayBinding = binding
                binding.layoutDay.background = getDrawable(context, R.color.main_red_bg)
                binding.tvDailyPercentage.setTextColor(getColor(context, R.color.main_gray_light))
            }else{
                if(item.exerciseDate == null){
                    binding.layoutDay.background = ColorDrawable(Color.TRANSPARENT)
                }else{
                    binding.layoutDay.background = getDrawable(context, R.drawable.shape_edge_gray)
                }
                binding.tvDailyPercentage.setTextColor(getColor(context, R.color.main_red))
            }

            // 일일 운동 수행 퍼센트
            if(item.dailyPercentage != -1){ // 작성한 일지가 있음
                binding.tvDailyPercentage.visibility = View.VISIBLE
                binding.tvDailyPercentage.text = item.dailyPercentage.toString() + "%"
               // binding.tvDiary.visibility = View.VISIBLE

            }else{ // 작성한 일지가 없음
                binding.tvDailyPercentage.visibility = View.GONE
                //binding.tvDiary.visibility = View.GONE
            }

            binding.layoutDay.setOnClickListener {

                if(item.exerciseDate != null){
                    if(item.isSelectedDay == true){ // 선택된 날짜를 클릭했다면 일지 화면으로 이동
                        var intent = Intent(context, DiaryActivity::class.java)

                        var formatDate = item.exerciseDate.format(DateTimeFormatter.ofPattern("YYYY.MM.dd"))
                        intent.putExtra("formatDate", formatDate)
                        intent.putExtra("exerciseDate", item.exerciseDate.toString())
                        intent.putExtra("diaryId", item.diaryId)

                        context.startActivity(intent)
                    }else{ // 선택 되지 않은 날짜를 클릭했다면 ui 변경
                        binding.tvDailyPercentage.setTextColor(getColor(context, R.color.main_gray_light))

                        itemList[selectedDayIdx].isSelectedDay = false
                        item.isSelectedDay = true
                        selectedDayIdx = this.adapterPosition

                        selectedDayBinding.layoutDay.background = getDrawable(context, R.drawable.shape_edge_gray)
                        selectedDayBinding.tvDailyPercentage.setTextColor(getColor(context, R.color.main_red))
                        binding.layoutDay.background = getDrawable(context, R.color.main_red_bg)
                        selectedDayBinding = binding



                    }
                }
            }

            binding.layoutDay.setOnLongClickListener OnLongClickListener@{

                if(item.exerciseDate != null){
                    if(item.isSelectedDay == true && item.dailyPercentage != -1) {
                        var dialog = DeleteDialog(context, context.getString(R.string.delete_diary), item.exerciseDate.toString())
                        dialog.show()

                        var btnYes = dialog.findViewById<TextView>(R.id.btn_yes)
                        var btnNo = dialog.findViewById<TextView>(R.id.btn_no)

                        btnYes.setOnClickListener {

                            diaryDeleteCallback.diaryDeleteListener(item.diaryId)


                            dialog.dismiss()

                        }
                        btnNo.setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                }
                return@OnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvCalenderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
        if(itemList[position].isSelectedDay == true){
            selectedDayIdx = position
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getListFormView(nList: MutableList<CalenderRvItem>){
        itemList = nList
    }

    fun setDiaryDeleteSuccess(diaryId: Int){
        for(item in itemList){
            if(item.diaryId == diaryId){
                item.dailyPercentage = -1
                notifyDataSetChanged()
            }
        }
    }


}