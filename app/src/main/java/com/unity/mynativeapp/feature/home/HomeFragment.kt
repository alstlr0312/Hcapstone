package com.unity.mynativeapp.feature.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.unity.mynativeapp.model.HomePageResponse
import com.unity.mynativeapp.databinding.FragmentHomeBinding
import com.unity.mynativeapp.model.CalenderRvItem
import com.unity.mynativeapp.network.RetrofitClient
import com.unity.mynativeapp.util.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeFragmentViewModel>()
    lateinit var binding: FragmentHomeBinding
    lateinit var todayDate: LocalDate
    lateinit var selectedDate: LocalDate
    lateinit var calenderRvAdapter: CalenderRvAdapter
    lateinit var loadingDialog: LoadingDialog
    lateinit var requestData: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        requestData = selectedDate.format(DateTimeFormatter.ofPattern("YYYY-MM"))
        viewModel.gethome(requestData)
        connect(requestData)

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

    fun connect(requestData: String) {
        RetrofitClient.getApiService().getHomePage(requestData).enqueue(object :
            Callback<HomePageResponse> {
            override fun onResponse(
                call: Call<HomePageResponse>,
                response: Response<HomePageResponse>
            ) {
                val code = response.code()
                val data = response.body()?.data

              /*  if (code == 200) {
                    if (data == null) return
                        var dayList = mutableListOf<CalenderRvItem>()

                        var firstDay = selectedDate.withDayOfMonth(1) // 첫번째 날
                        var firstDayOfWeek = firstDay.dayOfWeek.value // 첫번째 날의 요일

                        if(firstDayOfWeek != 7){ // 일요일
                            for(i in 0 until firstDayOfWeek){
                                dayList.add(CalenderRvItem())
                            }
                        }

                        var compareDate = selectedDate.withDayOfMonth(1)
                        val result = response.body()?.data?.calenders
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
                        binding.progressBarMonthly.progress = response.body()?.data!!.monthlyPercentage

                        calenderRvAdapter.getListFormView(dayList)
                        requireActivity().runOnUiThread {
                            calenderRvAdapter.notifyDataSetChanged()
                        }
                }*/
                if (code == 400) {
                    if (data == null) return

                    var dayList = mutableListOf<CalenderRvItem>()

                    var firstDay = selectedDate.withDayOfMonth(1) // 첫번째 날
                    var firstDayOfWeek = firstDay.dayOfWeek.value // 첫번째 날의 요일

                    if(firstDayOfWeek != 7){ // 일요일
                        for(i in 0 until firstDayOfWeek){
                            dayList.add(CalenderRvItem())
                        }
                    }

                    var compareDate = selectedDate.withDayOfMonth(1)

                    for(i in 1 until selectedDate.lengthOfMonth()+1){
                        if(i == selectedDate.dayOfMonth)
                            dayList.add(CalenderRvItem(compareDate, true))
                        else
                            dayList.add(CalenderRvItem(compareDate, false))

                        compareDate = compareDate.plusDays(1)
                    }
                    // 월 평균 퍼센트
                    binding.progressBarMonthly.progress = 0
                    calenderRvAdapter.getListFormView(dayList)
                    requireActivity().runOnUiThread {
                        calenderRvAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<HomePageResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
   /* private fun responseNoDiary(){
        viewModel.noDiary.observe(this) { response->
            var dayList = mutableListOf<CalenderRvItem>()

            var firstDay = selectedDate.withDayOfMonth(1) // 첫번째 날
            var firstDayOfWeek = firstDay.dayOfWeek.value // 첫번째 날의 요일

            if(firstDayOfWeek != 7){ // 일요일
                for(i in 0 until firstDayOfWeek){
                    dayList.add(CalenderRvItem())
                }
            }

            var compareDate = selectedDate.withDayOfMonth(1)

            for(i in 1 until selectedDate.lengthOfMonth()+1){
                if(i == selectedDate.dayOfMonth)
                    dayList.add(CalenderRvItem(compareDate, true))
                else
                    dayList.add(CalenderRvItem(compareDate, false))

                compareDate = compareDate.plusDays(1)
            }
                // 월 평균 퍼센트
            binding.progressBarMonthly.progress = 0
            calenderRvAdapter.getListFormView(dayList)
            requireActivity().runOnUiThread {
                calenderRvAdapter.notifyDataSetChanged()
            }
        }
}

    private fun  responseDiary() {
        viewModel.haveDiary.observe(this) { response->
            var dayList = mutableListOf<CalenderRvItem>()

            var firstDay = selectedDate.withDayOfMonth(1) // 첫번째 날
            var firstDayOfWeek = firstDay.dayOfWeek.value // 첫번째 날의 요일

            if(firstDayOfWeek != 7){ // 일요일
                for(i in 0 until firstDayOfWeek){
                    dayList.add(CalenderRvItem())
                }
            }

            var compareDate = selectedDate.withDayOfMonth(1)
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

            calenderRvAdapter.getListFormView(dayList)
            requireActivity().runOnUiThread {
                calenderRvAdapter.notifyDataSetChanged()
            }


        }

        viewModel.loading.observe(this) { isLoading ->
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                Log.d("homeFragment", message)
            }
        }
    }*/



}
