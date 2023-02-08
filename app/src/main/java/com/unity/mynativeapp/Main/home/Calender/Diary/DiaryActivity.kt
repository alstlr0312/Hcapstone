package com.unity.mynativeapp.Main.home.Calender.Diary


import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvItem
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryPhotoRv.DiaryPhotoRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryPhotoRv.DiaryPhotoRvItem
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityDiaryBinding


class DiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryBinding
    lateinit var dailyChallengeAdapter: DiaryExerciseRvAdapter
    lateinit var additionalExerciseAdapter: DiaryExerciseRvAdapter
    lateinit var photoAdapter: DiaryPhotoRvAdapter
    lateinit var videoAdapter: DiaryPhotoRvAdapter
    lateinit var addExerciseDialog: AddExerciseDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dailyChallengeAdapter = DiaryExerciseRvAdapter(mutableListOf(), this)
        binding.recyclerViewDailyChallenge.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewDailyChallenge.adapter = dailyChallengeAdapter

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

        binding.tvDate.text = intent.getStringExtra("date")


        // 추가 운동 설정
        binding.btnAddExercise.setOnClickListener {
            addExerciseDialog = AddExerciseDialog(this)
            addExerciseDialog.show()
        }

        var challengeList = mutableListOf<DiaryExerciseRvItem>()
        if(challengeList.size == 0){
            binding.btnSetDailyChallenge.visibility = View.VISIBLE
        }else{
            binding.btnSetDailyChallenge.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
    }

    fun getExerciseList(): MutableList<DiaryExerciseRvItem>{

        var list = mutableListOf<DiaryExerciseRvItem>()
        list.add(DiaryExerciseRvItem(true, "스쿼트-하체", 10, 5))
        list.add(DiaryExerciseRvItem(false, "유산소-런닝", null, null, 30))
        return list
    }

    fun getPhotoList(): MutableList<DiaryPhotoRvItem>{
        var list = mutableListOf<DiaryPhotoRvItem>()
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        return list
    }

    inner class AddExerciseDialog(context: Context): Dialog(context){

        override fun create() {
            super.create()

            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_add_exercise)
            window!!.setBackgroundDrawable(ColorDrawable())
            window!!.setDimAmount(0.0f)
        }

        override fun show() {
            if(!this.isShowing) super.show()
        }

        override fun dismiss() {
            if(this.isShowing) super.dismiss()
        }
    }

}