package com.unity.mynativeapp.feature.daily_challenge

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.unity.mynativeapp.databinding.ActivityDailyChallengeBinding
import java.time.LocalDate

class DailyChallengeActivity : AppCompatActivity() {
    lateinit var binding: ActivityDailyChallengeBinding
    lateinit var selectedDate: LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var selectedYear = intent.getIntExtra("year", 0)
        var selectedMonth = intent.getIntExtra("month", 0)
        var selectedDay = intent.getIntExtra("dayOfMonth", 0)
        selectedDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)

        binding.seekBarAerobicTime.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var time = p1.toDouble() / 2
                binding.tvAerobicTime.text = time.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        }

        )
    }
}