package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.Toast
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryActivity
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvItem
import com.unity.mynativeapp.Main.home.Calender.Diary.diaryActivity
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityAddExerciseBinding
import com.unity.mynativeapp.util.hideKeyboard
import org.json.JSONArray
import org.json.JSONObject

class AddExerciseActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddExerciseBinding
    lateinit var date: String   // 다이어리 날짜
    var cbList = arrayListOf<CheckBox>()    // 운동 부위 체크박스 리스트
    var isCardio = false    // 유산소 or 무산소 여부
    val exdata = JSONObject()
    val Aexdata = JSONArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setView()
        setListener()


    }

    private fun setView(){
        date = intent.getStringExtra("date").toString() // 다이어리 날짜
        setCbList() // 운동 체크박스 리스트 세팅


    }

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

        // 등록하기
        binding.btnRegister.setOnClickListener {

            var selectedItem: CheckBox? = null
            for(cb in cbList){
                if(cb.isChecked){
                    selectedItem = cb
                    break
                }
            }
            if(selectedItem == null){
                Toast.makeText(this, getString(R.string.please_select_exercise_you_want_register), Toast.LENGTH_SHORT).show()
            }else{
                if(isCardio){
                    val cardioTime = binding.seekBarCardioTime.progress * 30
                    if(cardioTime == 0){
                        Toast.makeText(this, getString(R.string.please_set_cardio_time), Toast.LENGTH_SHORT).show()
                    }else{
                        val exerciseName = binding.edtDetailCardioName.text.toString()
                        val reps = 0
                        val exSetCount = 0
                        val bodyPart = selectedItem.text.toString()

                        diaryActivity.exerciseAdapter.addItem(
                            DiaryExerciseRvItem(exerciseName, reps, exSetCount, isCardio, cardioTime, bodyPart, false)
                        )

                        exdata.put("exerciseName", exerciseName)
                        exdata.put("reps", reps)
                        exdata.put("exSetCount", exSetCount)
                        exdata.put("isCardio", isCardio)
                        exdata.put("cardioTime", cardioTime)
                        exdata.put("bodyPart", bodyPart)
                        Aexdata.put(exdata)
                        val intent = Intent(this,DiaryActivity::class.java)
                        intent.putExtra("exerciseInfo", Aexdata.toString())
                        setResult(RESULT_OK, intent)
                       // val returnIntent:Intent = intent
                       // intent.putExtra("exerciseInfo", Aexdata.toString())

                        finish()
                    }

                }else{

                    val exerciseName = binding.edtDetailWeightTrainingName.text.toString()
                    val reps = binding.edtReps.text.toString()
                    val exSetCount = binding.edtSets.text.toString()
                    val bodyPart = selectedItem.text.toString()

                    if(reps == "" || exSetCount == "" || reps.toInt() == 0 || exSetCount.toInt() == 0){
                        Toast.makeText(this, getString(R.string.please_input_exercise_count), Toast.LENGTH_SHORT).show()
                    }else{
                        diaryActivity.exerciseAdapter.addItem(
                            DiaryExerciseRvItem(exerciseName, reps.toInt(), exSetCount.toInt(), isCardio, 0, bodyPart, false)
                        )
                        exdata.put("exerciseName", exerciseName)
                        exdata.put("reps", reps)
                        exdata.put("exSetCount", exSetCount)
                        exdata.put("isCardio", isCardio)
                        exdata.put("cardioTime", 0)
                        exdata.put("bodyPart", bodyPart)
                        Aexdata.put(exdata)
                        Log.d("post data",Aexdata.toString())
                        val intent = Intent(this, DiaryActivity::class.java)
                        intent.putExtra("Data", Aexdata.toString())
                        finish()
                    }
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

    private fun setCbList(){
        cbList.add(binding.cbBack)
        cbList.add(binding.cbChest)
        cbList.add(binding.cbShoulder)
        cbList.add(binding.cbLegs)
        cbList.add(binding.cbAbs)
        cbList.add(binding.cbBiceps)
        cbList.add(binding.cbTriceps)
        cbList.add(binding.cbCardio)
        cbList.add(binding.cbEtc)

    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.cb_cardio -> { // 유산소
                    if (checked) {
                        binding.layoutCardio.visibility = View.VISIBLE
                        binding.edtDetailCardioName.text = null
                        binding.seekBarCardioTime.progress = 0
                        binding.layoutWeightTraining.visibility = View.GONE
                        for(cb in cbList){
                            if(cb != view){
                                cb.isChecked = false
                            }
                        }
                        isCardio = true
                    } else {
                        binding.layoutCardio.visibility = View.GONE
                    }
                }
                else -> { // 무산소
                    if (checked) {
                        binding.layoutWeightTraining.visibility = View.VISIBLE
                        binding.edtDetailWeightTrainingName.text = null
                        binding.edtReps.text = null
                        binding.edtSets.text = null
                        binding.layoutCardio.visibility = View.GONE
                        for(cb in cbList){
                            if(cb != view){
                                cb.isChecked = false
                            }
                        }
                        isCardio = false
                    } else {
                        binding.layoutWeightTraining.visibility = View.GONE
                    }
                }
            }
        }



    }

}