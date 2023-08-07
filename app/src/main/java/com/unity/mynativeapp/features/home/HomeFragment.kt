package com.unity.mynativeapp.features.home


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseFragment
import com.unity.mynativeapp.databinding.FragmentHomeBinding
import com.unity.mynativeapp.features.home.recommend.RecommendActivity
import com.unity.mynativeapp.model.CalenderRvItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter


interface OnItemClick {
    fun diaryDeleteListener(num: Int)
}
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::bind, R.layout.fragment_home), OnItemClick  {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var todayDate: LocalDate
    private lateinit var selectedDate: LocalDate
    private lateinit var calenderRvAdapter: CalenderRvAdapter
    private lateinit var requestData: String
    private var firstStart = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        todayDate = LocalDate.now()
        selectedDate = todayDate

        binding.tvUsername.text = MyApplication.prefUtil.getString("username", "")

        calenderRvAdapter = CalenderRvAdapter(requireContext(), this)
        binding.recyclerViewCalendar.layoutManager = GridLayoutManager(context, 7)
        binding.recyclerViewCalendar.adapter = calenderRvAdapter

        // UI Event를 정리한 함수
        setUiEvent()

        // viewModel의 Data를 Observe하는 이벤트 모음 함수
        subscribeUI()


    }

    override fun onResume() {
        super.onResume()

        setCalenderView()
    }

    fun setCalenderView(){
        binding.tvMonth.text = selectedDate.monthValue.toString() // 월
        binding.tvYear.text = selectedDate.year.toString() // 년

        // 홈화면 요청
        requestData = selectedDate.format(DateTimeFormatter.ofPattern("YYYY-MM"))
        viewModel.home(requestData)
    }

    fun setUiEvent(){
        binding.ivPreviousMonth.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1).withDayOfMonth(1)

            if(selectedDate.year==todayDate.year && selectedDate.monthValue==todayDate.monthValue)
                selectedDate = todayDate
            setCalenderView()
        }
        binding.ivNextMonth.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1).withDayOfMonth(1)

            if(selectedDate.year==todayDate.year && selectedDate.monthValue==todayDate.monthValue)
                selectedDate = todayDate
            setCalenderView()
        }

        // 추천 받기 화면으로 이동
        binding.btnRecommend.setOnClickListener {
            var recommendStr = ""
            when(binding.rgPurpose.checkedRadioButtonId){
                R.id.rb_routine -> { // 운동 루틴 추천
                    recommendStr = "routine"
                }
                R.id.rb_food -> { // 식단 추천
                    recommendStr = "food"
                }
            }

            if(recommendStr != ""){
                val intent = Intent(requireContext(), RecommendActivity::class.java)
                intent.putExtra("recommend", recommendStr)
                startActivity(intent)
            }

        }
    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            showCustomToast(message)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoadingDialog(requireContext()) else dismissLoadingDialog()
        }

        viewModel.logout.observe(viewLifecycleOwner){
            if(it) logout()
        }

        viewModel.homeData.observe(viewLifecycleOwner) { data ->

            var dayList = mutableListOf<CalenderRvItem>()

            var firstDay = selectedDate.withDayOfMonth(1) // 첫번째 날
            var firstDayOfWeek = firstDay.dayOfWeek.value // 첫번째 날의 요일

            if(firstDayOfWeek != 7){ // 일요일
                for(i in 0 until firstDayOfWeek){
                    dayList.add(CalenderRvItem())
                }
            }

            var compareDate = selectedDate.withDayOfMonth(1)


            if (data == null){  // 다이어리 목록 없음

                for(i in 1 until selectedDate.lengthOfMonth()+1){
                    if(i == selectedDate.dayOfMonth)
                        dayList.add(CalenderRvItem(compareDate, true))
                    else
                        dayList.add(CalenderRvItem(compareDate, false))

                    compareDate = compareDate.plusDays(1)
                }
                // 월 평균 퍼센트
                binding.progressBarMonthly.progress = 0

            }else{
                val result = data.calenders
                var j = 0
                for(i in 1 until selectedDate.lengthOfMonth()+1){
                    if(j < result!!.size && result[j].exerciseDate!! == compareDate.toString()){ // 다이어리 있음
                        if(i == selectedDate.dayOfMonth){
                            dayList.add(CalenderRvItem(compareDate, true, result[j].dailyPercentage, result[j].diaryId))
                        }else{
                            dayList.add(CalenderRvItem(compareDate,false, result[j].dailyPercentage, result[j].diaryId))
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
                binding.progressBarMonthly.progress = data.monthlyPercentage
            }

            calenderRvAdapter.getListFormView(dayList)
            requireActivity().runOnUiThread {
                calenderRvAdapter.notifyDataSetChanged()
            }
        }
        viewModel.diaryDeleteData.observe(viewLifecycleOwner){ data ->
            if(data == null) return@observe

            calenderRvAdapter.setDiaryDeleteSuccess(data)
        }
    }

    override fun diaryDeleteListener(num: Int) {
        viewModel.diaryDelete(num)
    }


}

