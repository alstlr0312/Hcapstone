package com.unity.mynativeapp.features.home.recommend

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
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

            /*
            when(recommendType) {
                "routine" -> {
                    //var result = "1일: 바벨스쿼트 - 10회 3세트 - 40kg 2일: 덤벨 벤치프레스 - 10회 3세트 - 10kg 3일: 바벨 데드리프트 - 3세트 8회 반복 - 50kg 참고: 이것은 샘플 운동 루틴일 뿐이다."
                    var result = data
                    val divisions = binding.npickerDivision.value
                    var resultArr = arrayListOf<String>()
                    for(i in 0 until divisions){
                        val splitArr = result!!.split("${i+1}일: ", limit = 2)
                        if(i > 0) {
                            resultArr.add(splitArr[0]+"\n")
                        }
                        resultArr.add("${i+1}일:\n")
                        result = splitArr[1]
                    }
                    val splitArr2 = result!!.split("참고: ", limit = 2)
                    resultArr.add(splitArr2[0]+"\n")
                    resultArr.add("참고: \n")
                    resultArr.add(splitArr2[1])
                    var printResult = ""
                    for(str in resultArr){
                        printResult += str
                    }
                    binding.tvRecommendResult.text = printResult
                }
                "food" -> {
                    binding.tvRecommendResult.text = data
                }
            }

             */
        }
    }

}