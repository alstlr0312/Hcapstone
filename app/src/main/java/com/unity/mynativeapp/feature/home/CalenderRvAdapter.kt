package com.unity.mynativeapp.feature.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.model.DayItem
import com.unity.mynativeapp.model.HomePageResponse
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ItemRvCalenderBinding
import com.unity.mynativeapp.feature.diary.DiaryActivity

@RequiresApi(Build.VERSION_CODES.O)

class CalenderRvAdapter(val context: Context): RecyclerView.Adapter<CalenderRvAdapter.ViewHolder>(),
    HomeFragmentInterface {

    var list = mutableListOf<DayItem>()
    var bindingList = mutableListOf<ItemRvCalenderBinding>()

    inner class ViewHolder(val binding: ItemRvCalenderBinding): RecyclerView.ViewHolder(binding.root){
        init{
            bindingList.add(binding)
        }
        fun bind(item: DayItem){

            if(item.date == null){
                binding.tvDay.text = ""
            }else{
                binding.tvDay.text = item.date.dayOfMonth.toString()
            }

            // 선택한 날짜 focus
            if(item.selected == true){
                binding.layoutDay.background = getDrawable(context, R.color.main_gray)
            }else{
                if(item.date == null){
                    binding.layoutDay.background = ColorDrawable(Color.TRANSPARENT)
                }else{
                    binding.layoutDay.background = getDrawable(context, R.drawable.shape_edge_gray)
                }
            }

            // 일일 챌린지 수행 수치
            if(item.challenge != null){
                binding.tvDailyChallenge.visibility = View.VISIBLE
                binding.tvDailyChallenge.text = item.challenge.toString() + "%"
            }else{
                binding.tvDailyChallenge.visibility = View.GONE

            }

            // 일지 작성 여부
            if(item.memo != false){
                binding.tvMemo.visibility = View.VISIBLE
            }else{
                binding.tvMemo.visibility = View.GONE
            }


            binding.layoutDay.setOnClickListener {

                if(item.date != null){
                    if(item.selected == true){ // 선택된 날짜를 클릭했다면 일지 화면으로 이동
                        var intent = Intent(context, DiaryActivity::class.java)
                        var date = "${item.date.year}." + item.date.monthValue.toString().padStart(2,'0') + "." +
                                item.date.dayOfMonth.toString().padStart(2, '0')
                        intent.putExtra("date", date)
                        context.startActivity(intent)
                    }else{ // 선택되지 않은 날짜를 클릭했다면 선택된 날짜 ui로 변경
                        // change callender 요청

                        // focus ui 변경 (test)
                        for(i in 0 until bindingList.size){
                            if(bindingList[i].tvDay.visibility == View.VISIBLE){
                                //itemList[i].layoutDay.background = getDrawable(context, R.drawable.shape_edge_gray)
                                list[i].selected = false
                            }
                        }
                        //binding.layoutDay.background = getDrawable(context, R.color.main_gray)
                        item.selected = true
                        notifyDataSetChanged()


                        // 서버 연결
                        //HomeFragmentService(this@CalenderRvAdapter).tryGetHomePage(userId = 0)

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvCalenderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getListFormView(nList: MutableList<DayItem>){
        list = nList
    }

    override fun onGetHomePageSuccess(response: HomePageResponse) {
        TODO("Not yet implemented")
    }

    override fun onGetHomePageFailure(message: String) {
        TODO("Not yet implemented")
    }

}