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
import com.example.capstone.Main.home.homeModels.HomePageResponse
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryActivity
import com.unity.mynativeapp.Main.home.HomeFragmentInterface
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvCalenderBinding

class CalenderRvAdapter(val context: Context): RecyclerView.Adapter<CalenderRvAdapter.ViewHolder>(),
    HomeFragmentInterface {

    var list = mutableListOf<CalenderRvItem>()
    var bindingList = mutableListOf<ItemRvCalenderBinding>()
    lateinit var selectedDayBinding: ItemRvCalenderBinding
    var selectedDayIdx: Int = -1

    inner class ViewHolder(val binding: ItemRvCalenderBinding): RecyclerView.ViewHolder(binding.root){
        init{
            bindingList.add(binding)
        }
        fun bind(item: CalenderRvItem){

            if(item.date == null){
                binding.tvDay.text = ""
            }else{
                binding.tvDay.text = item.date.dayOfMonth.toString()
            }

            // 선택한 날짜 focus
            if(item.isSelectedDay == true){
                selectedDayBinding = binding
                binding.layoutDay.background = getDrawable(context, R.color.main_gray)
            }else{
                if(item.date == null){
                    binding.layoutDay.background = ColorDrawable(Color.TRANSPARENT)
                }else{
                    binding.layoutDay.background = getDrawable(context, R.drawable.shape_edge_gray)
                }
            }

            // 일일 운동 수행 퍼센트
            if(item.percentage != null){
                binding.tvDailyPercentage.visibility = View.VISIBLE
                binding.tvDailyPercentage.text = item.percentage.toString() + "%"
            }else{
                binding.tvDailyPercentage.visibility = View.GONE

            }

            // 일지 작성 여부
            if(item.diary != false){
                binding.tvDiary.visibility = View.VISIBLE
            }else{
                binding.tvDiary.visibility = View.GONE
            }


            binding.layoutDay.setOnClickListener {

                if(item.date != null){
                    if(item.isSelectedDay == true){ // 선택된 날짜를 클릭했다면 일지 화면으로 이동
                        var intent = Intent(context, DiaryActivity::class.java)
                        var date = "${item.date.year}." + item.date.monthValue.toString().padStart(2,'0') + "." +
                                item.date.dayOfMonth.toString().padStart(2, '0')
                        intent.putExtra("date", date)
                        context.startActivity(intent)
                    }else{ // 선택되지 않은 날짜를 클릭했다면 선택된 날짜 ui로 변경
                        // change callender 요청

                        // focus ui 변경 (test)
                        selectedDayBinding.layoutDay.background = getDrawable(context, R.drawable.shape_edge_gray)
                        binding.layoutDay.background = getDrawable(context, R.color.main_gray)
                        selectedDayBinding = binding

                        list[selectedDayIdx].isSelectedDay = false
                        item.isSelectedDay = true

                        /*for(i in 0 until bindingList.size){
                            if(bindingList[i].tvDay.visibility == View.VISIBLE){
                                //itemList[i].layoutDay.background = getDrawable(context, R.drawable.shape_edge_gray)
                                list[i].isSelectedDay = false
                            }
                        }
                        //binding.layoutDay.background = getDrawable(context, R.color.main_gray)
                        item.isSelectedDay = true
                        notifyDataSetChanged()*/


                        // 서버 연결
                        //HomeFragmentService(this@CalenderRvAdapter).tryGetHomePage(userId = 0)

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

    override fun onGetHomePageSuccess(response: HomePageResponse) {
        TODO("Not yet implemented")
    }

    override fun onGetHomePageFailure(message: String) {
        TODO("Not yet implemented")
    }

}