package com.unity.mynativeapp.feature.diary

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityDiaryBinding
import com.unity.mynativeapp.model.DiaryExerciseRvItem
import com.unity.mynativeapp.model.DiaryPhotoRvItem

class DiaryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }

    private lateinit var dailyChallengeAdapter: DiaryExerciseRvAdapter
    private lateinit var additionalExerciseAdapter: DiaryExerciseRvAdapter
    private lateinit var photoAdapter: DiaryPhotoRvAdapter
    private lateinit var videoAdapter: DiaryPhotoRvAdapter
    private lateinit var addExerciseDialog: AddExerciseDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding.rvDailyChallenge) {
            setHasFixedSize(true)
            adapter = DiaryExerciseRvAdapter(mutableListOf(), this@DiaryActivity)
            layoutManager = LinearLayoutManager(this@DiaryActivity)
        }

        additionalExerciseAdapter = DiaryExerciseRvAdapter(mutableListOf(), this)
        binding.recyclerViewAdditionalExercise.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewAdditionalExercise.adapter = additionalExerciseAdapter

        photoAdapter = DiaryPhotoRvAdapter(getPhotoList(), this)
        binding.recyclerViewPhotos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPhotos.adapter = photoAdapter

        videoAdapter = DiaryPhotoRvAdapter(getPhotoList(), this)
        binding.recyclerViewVideos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewVideos.adapter = videoAdapter

        binding.btnBack.setOnClickListener {
            finish()
        }
