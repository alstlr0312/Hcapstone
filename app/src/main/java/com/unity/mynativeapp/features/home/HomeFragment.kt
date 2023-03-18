package com.unity.mynativeapp.features.home


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.model.CalenderRvItem
import com.unity.mynativeapp.features.login.SplashActivity
import com.unity.mynativeapp.databinding.FragmentHomeBinding
import com.unity.mynativeapp.model.HomePageResponse
import com.unity.mynativeapp.util.LoadingDialog
import com.unity.mynativeapp.util.X_ACCESS_TOKEN
import java.time.LocalDate
import kotlin.system.exitProcess


class HomeFragment : Fragment(), HomeFragmentInterface {

    val binding by lazy{FragmentHomeBinding.inflate(layoutInflater)}
    lateinit var todayDate: LocalDate
    lateinit var selectedDate: LocalDate
    lateinit var calenderRvAdapter: CalenderRvAdapter
    lateinit var loadingDialog: LoadingDialog
    lateinit var requestData: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadingDialog = LoadingDialog(requireContext())

        todayDate = LocalDate.now()
        selectedDate = todayDate

        setCalenderView()

        setListener()

        return binding.root
    }

    fun setCalenderView(){
        binding.tvMonth.text = selectedDate.monthValue.toString() // 월
        binding.tvYear.text = selectedDate.year.toString() // 년

        calenderRvAdapter = CalenderRvAdapter(requireContext())
        binding.recyclerViewCalendar.layoutManager = GridLayoutManager(context, 7)
        binding.recyclerViewCalendar.adapter = calenderRvAdapter


        // 홈화면 요청


    }

    fun setListener(){
        binding.ivPreviousMonth.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1).withDayOfMonth(1)

            if(selectedDate.year==todayDate.year && selectedDate.monthValue==todayDate.monthValue)
                selectedDate = todayDate
            setCalenderView()
            calenderRvAdapter.notifyDataSetChanged()
        }
        binding.ivNextMonth.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1).withDayOfMonth(1)

            if(selectedDate.year==todayDate.year && selectedDate.monthValue==todayDate.monthValue)
                selectedDate = todayDate
            setCalenderView()
            calenderRvAdapter.notifyDataSetChanged()
        }
    }

    override fun onGetHomePageSuccess(response: HomePageResponse) {

        if(MyApplication.prefUtil.getString(X_ACCESS_TOKEN, null) == null){
            requireActivity().finishAffinity()
            startActivity(Intent(requireActivity(), SplashActivity::class.java))
            exitProcess(0)
        }

        var dayList = mutableListOf<CalenderRvItem>()

        var firstDay = selectedDate.withDayOfMonth(1) // 첫번째 날
        var firstDayOfWeek = firstDay.dayOfWeek.value // 첫번째 날의 요일

        if(firstDayOfWeek != 7){ // 일요일
            for(i in 0 until firstDayOfWeek){
                dayList.add(CalenderRvItem())
            }
        }

        var compareDate = selectedDate.withDayOfMonth(1)

        if(response.status == 400){ // 다이어리 목록 없음
            for(i in 1 until selectedDate.lengthOfMonth()+1){
                if(i == selectedDate.dayOfMonth)
                    dayList.add(CalenderRvItem(compareDate, true))
                else
                    dayList.add(CalenderRvItem(compareDate, false))

                compareDate = compareDate.plusDays(1)
            }
            // 월 평균 퍼센트
            binding.progressBarMonthly.progress = 0
        }
        else{ // 다이어리 목록 있음
            val result = response.data?.calenders
            var j = 0
            for(i in 1 until selectedDate.lengthOfMonth()+1){
                if(j < result!!.size && result[j].exerciseDate!! == compareDate){ // 다이어리 있음
                    if(i == selectedDate.dayOfMonth){
                        dayList.add(CalenderRvItem(compareDate, true, result[j].dailyPercentage))
                    }else{
                        dayList.add(CalenderRvItem(compareDate, false, result[j].dailyPercentage))
                    }
                    j++
                }else{  // 다이어리 없음
                    if(i == selectedDate.dayOfMonth){
                        dayList.add(CalenderRvItem(compareDate, true))
                    }else{
                        dayList.add(CalenderRvItem(compareDate, false))
                    }
                }
                compareDate = compareDate.plusDays(1)
            }
            // 월 평균 퍼센트
            binding.progressBarMonthly.progress = response.data!!.monthlyPercentage
        }
        calenderRvAdapter.getListFormView(dayList)
        requireActivity().runOnUiThread {
            calenderRvAdapter.notifyDataSetChanged()
        }



    }

    override fun onGetHomePageFailure(message: String) {

        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            Log.d("homeFragment", message)
        }
    }


}

