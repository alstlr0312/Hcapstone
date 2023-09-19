package com.unity.mynativeapp.features.diary

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityAddExerciseBinding
import com.unity.mynativeapp.model.DiaryExerciseRvItem
import com.unity.mynativeapp.model.ExerciseItem
import com.unity.mynativeapp.network.util.*

class AddExerciseActivity : BaseActivity<ActivityAddExerciseBinding>(ActivityAddExerciseBinding::inflate) {
    lateinit var date: String   // 다이어리 날짜
    private var isCardio: Boolean? = null    // 유산소 or 무산소 여부
    private var selectedMuscleList = mutableListOf<String>()
    private var bodyParts = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setListener()
    }

    private fun setView(){
        date = intent.getStringExtra("date").toString() // 다이어리 날짜
    }
    @SuppressLint("ClickableViewAccessibility")

    private fun setListener(){

        // 유산소 시간 설정 seekbar
        binding.seekBarCardioTime.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var time = p1 * 30 // 30분 간격
                binding.tvCardioTime.text = "${time} " + getString(R.string.minute)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        // 무산소
        // 근육 버튼 이벤트
        // 신체 앞면
        binding.layoutMuscleFront.setOnTouchListener { _, event ->
            when(event.action){ // 260x554
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x; val y = event.y
                    //240
                    if (x in 160.0..320.0 && y in 200.0..275.0){ // 가슴
                        Log.d("MuscleView","MuscleView ($x, $y): 대흉근(가슴)")
                        setSelectedMuscle(binding.ivFrontChest, getString(R.string.muscle_chest))
                    }else if((x in 90.0..160.0 || x in 320.0..390.0) && y in 170.0..250.0){ // 어깨
                        Log.d("MuscleView","MuscleView ($x, $y): 삼각근(어깨)")
                        setSelectedMuscle(binding.ivFrontShoulder, getString(R.string.muscle_shoulder))
                    }else if((x in 80.0..155.0 || x in 325.0..400.0) && y in 250.0..340.0){ // 이두근
                        Log.d("MuscleView","MuscleView ($x, $y): 이두근")
                        setSelectedMuscle(binding.ivFrontBiceps, getString(R.string.muscle_biceps))
                    }else if((x in 50.0..140.0 || x in 340.0..420.0) && y in 340.0..470.0){ // 전완근
                        Log.d("MuscleView","MuscleView ($x, $y): 전완근")
                        setSelectedMuscle(binding.ivFrontForearms, getString(R.string.muscle_forearms))
                    }else if(x in 170.0..310.0 && y in 290.0..470.0){ // 복부근
                        Log.d("MuscleView","MuscleView ($x, $y): 복부")
                        setSelectedMuscle(binding.ivFrontAbs, getString(R.string.muscle_abs))
                    }else if(x in 130.0..350.0 && y in 470.0..670.0){ // 허벅지
                        Log.d("MuscleView","MuscleView ($x, $y): 대퇴근(허벅지)")
                        setSelectedMuscle(binding.ivFrontQuadriceps, getString(R.string.muscle_quadriceps))
                    }else if(x in 130.0..350.0 && y in 670.0..840.0){ // 종아리
                        Log.d("MuscleView","MuscleView ($x, $y): 비복근(종아리)")
                        setSelectedMuscle(binding.ivFrontCalves, getString(R.string.muscle_calves))
                    }else{
                        Log.d("MuscleView","MuscleView ($x, $y): ")
                    }
                }
            }
            return@setOnTouchListener true
        }

        // 신체 뒷면
        binding.layoutMuscleBack.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x; val y = event.y

                    if (x in 190.0..280.0 && y in 130.0..270.0){ // 승모근
                        Log.d("MuscleView","MuscleView ($x, $y): 승모근")
                        setSelectedMuscle(binding.ivBackTrap, getString(R.string.muscle_trap))
                    }else if (x in 170.0..310.0 && y in 300.0..380.0){ // 광배근
                        Log.d("MuscleView","MuscleView ($x, $y): 광배근")
                        setSelectedMuscle(binding.ivBackLat, getString(R.string.muscle_lat))
                    }else if (x in 180.0..310.0 && y in 440.0..520.0){ // 대둔근
                        Log.d("MuscleView","MuscleView ($x, $y): 대둔근")
                        setSelectedMuscle(binding.ivBackGlutes, getString(R.string.muscle_glutes))
                    }else if ((x in 90.0..160.0 || x in 320.0..390.0) && y in 190.0..260.0){ // 삼각근
                        Log.d("MuscleView","MuscleView ($x, $y): 삼각근")
                        setSelectedMuscle(binding.ivBackShoulder, getString(R.string.muscle_shoulder))
                    }else if ((x in 70.0..150.0 || x in 330.0..410.0) && y in 270.0..340.0){ // 삼두근
                        Log.d("MuscleView","MuscleView ($x, $y): 삼두근")
                        setSelectedMuscle(binding.ivBackTriceps, getString(R.string.muscle_triceps))
                    }else if ((x in 65.0..130.0 || x in 350.0..415.0) && y in 350.0..455.0){ // 전완근
                        Log.d("MuscleView","MuscleView ($x, $y): 전완근")
                        setSelectedMuscle(binding.ivBackForearms, getString(R.string.muscle_forearms))
                    }else if (x in 140.0..400.0 && y in 530.0..690.0){ // 햄스트링
                        Log.d("MuscleView","MuscleView ($x, $y): 햄스트링")
                        setSelectedMuscle(binding.ivBackHamstrings, getString(R.string.muscle_hamstrings))
                    }else if (x in 140.0..400.0 && y in 710.0..850.0){ // 비복근
                        Log.d("MuscleView","MuscleView ($x, $y): 비복근")
                        setSelectedMuscle(binding.ivBackCalves, getString(R.string.muscle_calves))
                    }else{
                        Log.d("MuscleView","MuscleView ($x, $y): ")
                    }
                }
            }
            return@setOnTouchListener true
        }

        // 등록하기
        binding.btnRegister.setOnClickListener {

            if(isCardio == null){
                showCustomToast(SELECT_CARDIO_WEIGHT)
            }else{
                if(isCardio!!){ // 유산소
                    val cardioTime = binding.seekBarCardioTime.progress * 30
                    val exerciseName = binding.edtCardioExerciseName.text.toString()
                    val reps = null
                    val exSetCount = null
                    val bodyPart = getString(R.string.exercise_cardio)

                    if(cardioTime == 0){
                        showCustomToast(SELECT_CARDIO_TIME)
                        return@setOnClickListener
                    }
                    if(exerciseName.isEmpty()){
                        showCustomToast(INPUT_EXERCISE_NAME)
                        return@setOnClickListener
                    }
                    diaryActivity.exerciseAdapter.addItem(
                        ExerciseItem(exerciseName, reps, exSetCount, isCardio!!, cardioTime, bodyPart, false)
                    )
                    finish()

                }else{ // 무산소
                    val exerciseName = binding.edtWeightExerciseName.text.toString()
                    val reps = binding.edtReps.text.toString()
                    val exSetCount = binding.edtSets.text.toString()

                    if(bodyParts.isEmpty()){
                        showCustomToast(SELECT_WEIGHT_PART)
                        return@setOnClickListener
                    }
                    if(reps.isEmpty() || exSetCount.isEmpty() || reps.toInt() == 0 || exSetCount.toInt() == 0){
                        showCustomToast(INPUT_WEIGHT_COUNT)
                        return@setOnClickListener
                    }
                    diaryActivity.exerciseAdapter.addItem(
                        ExerciseItem(exerciseName, reps.toInt(), exSetCount.toInt(), isCardio!!, null, bodyParts, false)
                    )
                    finish()
                }

            }
        }


        // 키보드 숨기기
        binding.layoutActivity.setOnClickListener {
            this.hideKeyboard()
        }

        // 닫기
        binding.ivClose.setOnClickListener {
            finish()
        }
    }



    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.cb_cardio -> { // 유산소
                    if (checked) {
                        binding.layoutCardio.visibility = View.VISIBLE
                        binding.edtCardioExerciseName.text = null
                        binding.seekBarCardioTime.progress = 0
                        binding.layoutWeightTraining.visibility = View.GONE
                        binding.cbWeight.isChecked = false
                        isCardio = true
                    } else {
                        binding.layoutCardio.visibility = View.GONE
                    }
                }
                else -> { // 무산소
                    if (checked) {
                        binding.layoutWeightTraining.visibility = View.VISIBLE
                        binding.edtWeightExerciseName.text = null
                        binding.edtReps.text = null
                        binding.edtSets.text = null
                        binding.layoutCardio.visibility = View.GONE
                        binding.cbCardio.isChecked = false
                        isCardio = false
                    } else {
                        binding.layoutWeightTraining.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setSelectedMuscle(view: ImageView, muscleName: String){ // 0: add, 1: remove

        if(view.visibility == View.VISIBLE){

            when(muscleName){
                getString(R.string.muscle_shoulder) -> {
                    binding.ivFrontShoulder.visibility = View.INVISIBLE
                    binding.ivBackShoulder.visibility = View.INVISIBLE
                }
                getString(R.string.muscle_forearms) -> {
                    binding.ivFrontForearms.visibility = View.INVISIBLE
                    binding.ivBackForearms.visibility = View.INVISIBLE
                }
                getString(R.string.muscle_calves) -> {
                    binding.ivFrontCalves.visibility = View.INVISIBLE
                    binding.ivBackCalves.visibility = View.INVISIBLE
                }
                else -> {
                    view.visibility = View.INVISIBLE
                }
            }
            selectedMuscleList.remove(muscleName)
        }else{
            if(selectedMuscleList.size >= 3){
                showCustomToast(MUSCLE_SELECT_MAX)
            }else{
                selectedMuscleList.add(muscleName)
                when(muscleName){
                    getString(R.string.muscle_shoulder) -> {
                        binding.ivFrontShoulder.visibility = View.VISIBLE
                        binding.ivBackShoulder.visibility = View.VISIBLE
                    }
                    getString(R.string.muscle_forearms) -> {
                        binding.ivFrontForearms.visibility = View.VISIBLE
                        binding.ivBackForearms.visibility = View.VISIBLE
                    }
                    getString(R.string.muscle_calves) -> {
                        binding.ivFrontCalves.visibility = View.VISIBLE
                        binding.ivBackCalves.visibility = View.VISIBLE
                    }
                    else -> {
                        view.visibility = View.VISIBLE
                    }
                }
            }
        }

        bodyParts = ""
        for(i in selectedMuscleList.indices){
            bodyParts += if(i == 0){
                selectedMuscleList[i]
            }else{
                ", ${selectedMuscleList[i]}"
            }
        }

        binding.tvSelectedMuscle.text = bodyParts
    }

}