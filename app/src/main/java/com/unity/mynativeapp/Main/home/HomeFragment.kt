package com.unity.mynativeapp.Main.home


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.capstone.Main.home.homeModels.HomePageResponse
import com.unity.mynativeapp.Main.home.Calender.CalenderRvAdapter
import com.unity.mynativeapp.Main.home.Calender.CalenderRvItem
import com.unity.mynativeapp.databinding.FragmentHomeBinding
import com.unity.mynativeapp.util.LoadingDialog
import java.time.LocalDate
import java.time.YearMonth

lateinit var testList: MutableList<CalenderRvItem>


class HomeFragment : Fragment(), HomeFragmentInterface {

    lateinit var binding: FragmentHomeBinding
    lateinit var todayDate: LocalDate
    lateinit var selectedDate: LocalDate
    lateinit var calenderRvAdapter: CalenderRvAdapter
    lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog(requireContext())

        todayDate = LocalDate.now()
        selectedDate = todayDate

        testList = mutableListOf()

        // 서버 통신
        //loadingDialog.show()
        //HomeFragmentService(this).tryGetHomePage(userId = 0)



        for(i in 1 until todayDate.lengthOfMonth()+1){
            if(i == todayDate.dayOfMonth){
                testList.add(
                    CalenderRvItem(LocalDate.of(2023, todayDate.monthValue, i), true, 50, true)
                )
            }else{
                testList.add(
                    CalenderRvItem(LocalDate.of(2023, todayDate.monthValue, i), false, 50, true)
                )
            }

        }

        setCalenderView()



        binding.ivPreviousMonth.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            selectedDate.atStartOfDay()

            calenderRvAdapter.notifyDataSetChanged()
        }
        binding.ivNextMonth.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            selectedDate.atStartOfDay()

            calenderRvAdapter.notifyDataSetChanged()
        }



        return binding.root
    }

    fun setCalenderView(){
        binding.tvMonth.text = selectedDate.monthValue.toString() // 월
        binding.tvYear.text = selectedDate.year.toString() // 년

        calenderRvAdapter = CalenderRvAdapter(requireContext())
        binding.recyclerViewCalendar.layoutManager = GridLayoutManager(context, 7)
        binding.recyclerViewCalendar.adapter = calenderRvAdapter

        // 더미데이터 (서버 연결 시 삭제)
        calenderRvAdapter.getListFormView(setDayList(testList))
        calenderRvAdapter.notifyDataSetChanged()
    }

    fun setDayList(calender: MutableList<CalenderRvItem>): MutableList<CalenderRvItem>{
        var dayList = mutableListOf<CalenderRvItem>()

        var yearMonth = YearMonth.from(selectedDate) // 월
        var length = yearMonth.lengthOfMonth() // 월 길이 (28.30.31)

        var firstDay = selectedDate.withDayOfMonth(1) // 첫번째 날
        var firstDayOfWeek = firstDay.dayOfWeek.value // 첫번째 날 요일

        /*var lastDay = selectedDate.withDayOfMonth(length) // 마지막 날
        var lastDayOfWeek = lastDay.dayOfWeek.value // 마지막 날 요일


        for(i in 1 until 43){
            if(i <= firstDayOfWeek){
                if(firstDayOfWeek != 7){
                    dayList.add(CalenderRvItem())
                }
            }else if(i > firstDayOfWeek+length){
                if(lastDayOfWeek != 6){
                    dayList.add(CalenderRvItem())
                    lastDayOfWeek++
                }else {
                    break
                }
            }else{
                val date = DateItem(selectedDate.year, selectedDate.monthValue, i-firstDayOfWeek)
                if(selectedDate.monthValue == yearMonth.monthValue
                    && selectedDate.dayOfMonth == i-firstDayOfWeek){ // 선택된 날짜
                    dayList.add(CalenderRvItem(date, true, 75, "memo"))
                }else{
                    dayList.add(CalenderRvItem(date))

                }
            }
        }*/


        if(firstDayOfWeek == 7){ // 일요일
            dayList = calender

        }else{
            for(i in 0 until firstDayOfWeek){
                dayList.add(CalenderRvItem())
            }
            dayList.addAll(firstDayOfWeek, calender)
        }

        return dayList
    }

    override fun onGetHomePageSuccess(response: HomePageResponse) {
        loadingDialog.dismiss()
        calenderRvAdapter.getListFormView(setDayList(response.data.calender))
    }

    override fun onGetHomePageFailure(message: String) {
        loadingDialog.dismiss()
        Toast.makeText(requireContext(), "홈화면 요청 실패: $message", Toast.LENGTH_SHORT).show()
    }

}


