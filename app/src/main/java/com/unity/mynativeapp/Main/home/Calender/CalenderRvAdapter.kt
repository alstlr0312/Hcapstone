package com.unity.mynativeapp.Main.home.Calender

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvCalenderBinding
import java.time.format.DateTimeFormatter


class CalenderRvAdapter(val context: Context): RecyclerView.Adapter<CalenderRvAdapter.ViewHolder>(){

    var list = mutableListOf<CalenderRvItem>()
    lateinit var selectedDayBinding: ItemRvCalenderBinding
    var selectedDayIdx: Int = -1

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
                binding.layoutDay.background = getDrawable(context, R.color.main_gray)
            }else{
                if(item.exerciseDate == null){
                    binding.layoutDay.background = ColorDrawable(Color.TRANSPARENT)
                }else{
                    binding.layoutDay.background = getDrawable(context, R.drawable.shape_edge_gray)
                }
            }

            // 일일 운동 수행 퍼센트
            if(item.dailyPercentage != -1){ // 작성한 일지가 있음
                binding.tvDailyPercentage.visibility = View.VISIBLE
                binding.tvDailyPercentage.text = item.dailyPercentage.toString() + "%"
                binding.tvDiary.visibility = View.VISIBLE

            }else{ // 작성한 일지가 없음
                binding.tvDailyPercentage.visibility = View.GONE
                binding.tvDiary.visibility = View.GONE
            }

            binding.layoutDay.setOnClickListener {

                if(item.exerciseDate != null){
                    if(item.isSelectedDay == true){ // 선택된 날짜를 클릭했다면 일지 화면으로 이동
                        var intent = Intent(context, DiaryActivity::class.java)

                        var date = item.exerciseDate.format(DateTimeFormatter.ofPattern("YYYY.MM.dd"))
                        intent.putExtra("date", date)

                        var status: Int = if(item.dailyPercentage != -1){
                            0
                        }else 1
                        intent.putExtra("mode", status)

                        context.startActivity(intent)
                    }else{ // 선택 되지 않은 날짜를 클릭했다면 ui 변경

                        selectedDayBinding.layoutDay.background = getDrawable(context, R.drawable.shape_edge_gray)
                        binding.layoutDay.background = getDrawable(context, R.color.main_gray)
                        selectedDayBinding = binding

                        list[selectedDayIdx].isSelectedDay = false
                        item.isSelectedDay = true

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderRvAdapter.ViewHolder {
        return ViewHolder(ItemRvCalenderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CalenderRvAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
        if(list[position].isSelectedDay == true){
            selectedDayIdx = position
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getListFormView(nList: MutableList<CalenderRvItem>){
        list = nList
    }

}