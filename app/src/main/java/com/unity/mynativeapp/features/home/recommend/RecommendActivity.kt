package com.unity.mynativeapp.features.home.recommend

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.NumberPicker
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityRecommendBinding
import com.unity.mynativeapp.model.DietFoodRequest
import com.unity.mynativeapp.model.RoutineRequest

class RecommendActivity : BaseActivity<ActivityRecommendBinding>(ActivityRecommendBinding::inflate) {

    var recommendType = ""
    lateinit var foodAdapter: FoodRvAdapter
    private val viewModel by viewModels<RecommendViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
        subscribeUi()
    }

    private fun setView(){
        with(binding.npickerHeight){
            wrapSelectorWheel = false // 순환 막기
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS // editText 해
            minValue = 130; maxValue = 200; value = 165 // 값
            textColor = Color.WHITE
            textSize = 46f
        }
        with(binding.npickerWeight){
            wrapSelectorWheel = false // 순환 막기
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS // editText 해
            minValue = 35; maxValue = 150; value = 60 // 값
            textColor = Color.WHITE
            textSize = 46f
        }
        with(binding.npickerDivision){
            wrapSelectorWheel = false // 순환 막기
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS // editText 해
            minValue = 2; maxValue = 5; value = 2 // 값
            textColor = Color.WHITE
            textSize = 46f
        }

        binding.layoutRecommendResult.visibility = View.GONE

        recommendType = intent.getStringExtra("recommend").toString()
        when(recommendType){
            "routine" -> {
                binding.tvRecommendType.text = getString(R.string.recommend_routine)
                binding.btnRecommend.text = getString(R.string.get_routine)
                binding.layoutDivision.visibility = View.VISIBLE
                binding.layoutEatenFood.visibility = View.GONE

            }
            "food" -> {
                binding.tvRecommendType.text = getString(R.string.recommend_todays_food)
                binding.btnRecommend.text = getString(R.string.get_diet_food)
                binding.layoutDivision.visibility = View.GONE
                binding.layoutEatenFood.visibility = View.VISIBLE

                foodAdapter = FoodRvAdapter(this)
                binding.rvEatenFood.adapter = foodAdapter
                binding.rvEatenFood.layoutManager = GridLayoutManager(this, 3)

                binding.btnAddFood.setOnClickListener {
                    foodAdapter.addItem()
                }
            }
        }

    }

    private  fun setUiEvent(){

        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.btnRecommend.setOnClickListener {
            var purpose = ""
            when(binding.rgPurpose.checkedRadioButtonId){
                R.id.rb_muscle_gain -> {purpose = "MUSCLE_GAIN"}
                R.id.rb_loosing_weight -> {purpose = "LOOSING_WEIGHT"}
            }

            when(recommendType){
                "routine" -> {
                    viewModel.recommendRoutine(
                        RoutineRequest(
                            healthPurpose = purpose,
                            height = binding.npickerHeight.value,
                            weight = binding.npickerWeight.value,
                            divisions = binding.npickerDivision.value
                        )
                    )
                }
                "food" -> {
                    val foodList = foodAdapter.getFoodList()
                    if(foodList.size <= 0){
                        showCustomToast("오늘 먹은 음식을 입력해 주세요.")
                    }else{
                        viewModel.recommendFood(
                            DietFoodRequest(
                                healthPurpose = purpose,
                                height = binding.npickerHeight.value,
                                weight = binding.npickerWeight.value,
                                food = foodList
                            )
                        )
                    }

                }
            }


        }

    }

    private fun subscribeUi(){

        viewModel.toastMessage.observe(this) { message ->
            showCustomToast(message)
        }

        viewModel.loading.observe(this){isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }

        viewModel.logout.observe(this) { logout ->
            if(logout) logout()
        }

        viewModel.recommendData.observe(this) {data ->
            if(data==null){
                binding.layoutRecommendResult.visibility = View.GONE
                return@observe
            }
            binding.layoutRecommendResult.visibility = View.VISIBLE
            binding.tvRecommendResult.text = data


            when(recommendType) {
                "routine" -> {
                    //var result = "1일: 바벨스쿼트 - 10회 3세트 - 40kg 2일: 덤벨 벤치프레스 - 10회 3세트 - 10kg 3일: 바벨 데드리프트 - 3세트 8회 반복 - 50kg 참고: 이것은 샘플 운동 루틴일 뿐이다."
                    var result = data
                    val divisions = binding.npickerDivision.value
                    val splitArr = result.split(":", "참고", "냉각") as MutableList<String>
                    var printResult = ""
                    printResult += "[1일]\n"
                    for(i in 1..divisions){
                        var str = ""
                        if(i != divisions){
                            val str1 = splitArr[i]
                            str = splitArr[i].substring(0, str1.length-3)
                        }else{
                            str = splitArr[i]
                        }
                        Log.d("SplitArray", str)
                        var split2Arr: MutableList<String>
                        if(str.contains("1.")){
                            printResult += "1."
                            split2Arr = str.split("1.", limit = 2) as MutableList<String>
                            for(j in 2..divisions){
                                split2Arr = split2Arr[1].split("${j}.", limit = 2) as MutableList<String>
                                printResult += "${split2Arr[0]}\n"
                                printResult += "${j}."
                            }
                            printResult += "${split2Arr[1]}\n"

                        }else if(str.contains("운동 1")){
                            printResult += "운동 1"
                            split2Arr = str.split("운동 1", limit = 2) as MutableList<String>
                            for(j in 2..divisions){
                                split2Arr = split2Arr[1].split("운동 ${j}", limit = 2) as MutableList<String>
                                printResult += "${split2Arr[0]}\n"
                                printResult += "운동 ${j}"
                            }
                            printResult += "${split2Arr[1]}\n"
                        }else{
                            printResult += "${str}\n"
                        }
                        if(i != divisions) printResult += "\n[${i+1}일]\n"

                    }
                    binding.tvRecommendResult.text = printResult
                }
                "food" -> {
                    val splitArr = data.split("탄수화물:", "단백질:", "지방:", limit=4) as MutableList<String>
                    var printResult = "각 음식 300g 기준으로 섭취한 영양소는 총:\n"
                    for(str in splitArr){
                        Log.d("SplitArray", str)
                    }
                    printResult += "탄수화물:${splitArr[1]}\n"
                    printResult += "단백질:${splitArr[2]}\n"
                    val splitArr2 = splitArr[3].split(".", limit=2)
                    printResult += "지방:${splitArr2[0]}입니다.\n${splitArr2[1]}"
                    binding.tvRecommendResult.text = printResult
                }
            }


        }
    }
}