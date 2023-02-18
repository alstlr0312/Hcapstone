package com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise

import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityAddExerciseBinding
import com.unity.mynativeapp.util.hideKeyboard
import java.time.LocalDate

class AddExerciseActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddExerciseBinding
    lateinit var date: String
    var cbList = arrayListOf<CheckBox>()
    var isCardio = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        date = intent.getStringExtra("date").toString()

        setCbList()

        binding.seekBarCardioTime.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var time = p1 * 30
                binding.tvCardioTime.text = "${time} " + getString(R.string.minute)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        binding.layoutActivity.setOnClickListener {
            this.hideKeyboard()
        }

        binding.ivClose.setOnClickListener {
            finish()
        }
    }

    fun setCbList(){
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