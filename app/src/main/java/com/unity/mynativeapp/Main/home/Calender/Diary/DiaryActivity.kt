package com.unity.mynativeapp.Main.home.Calender.Diary


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise.AddExerciseActivity
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvItem
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryPhotoRv.DiaryPhotoRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryPhotoRv.DiaryPhotoRvItem
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryBinding
    lateinit var exerciseAdapter: DiaryExerciseRvAdapter
    lateinit var photoAdapter: DiaryPhotoRvAdapter
    lateinit var videoAdapter: DiaryPhotoRvAdapter
    lateinit var date: String
    var firstStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        date = intent.getStringExtra("date").toString()
        // 다이어리 날짜

        binding.tvDate.text = date
        exerciseAdapter = DiaryExerciseRvAdapter(getExerciseList(), this)
        binding.recyclerViewTodaysExercise.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTodaysExercise.adapter = exerciseAdapter
        photoAdapter = DiaryPhotoRvAdapter(getPhotoList(), this)
        binding.recyclerViewPhotos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPhotos.adapter = photoAdapter

        videoAdapter = DiaryPhotoRvAdapter(getPhotoList(), this)
        binding.recyclerViewVideos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewVideos.adapter = videoAdapter

        binding.edtMemo.setText("열심히 했다.")

        binding.btnBack.setOnClickListener {
            finish()
        }


        // 운동 추가
        binding.btnAddExercise.setOnClickListener {
            var intent = Intent(this, AddExerciseActivity::class.java)
            intent.putExtra("date", date)
            startActivity(intent)
        }

        // 사진 추가
        binding.btnAddPhoto.setOnClickListener {
            //openGallery()
        }

        // 영상 추가
        binding.btnAddVideo.setOnClickListener {

        }

        // 일지 정보가 있을 경우 -> 일지 조회(수정불가)
        binding.ivEdit.visibility = View.VISIBLE
        binding.ivSave.visibility = View.GONE
        binding.edtMemo.isClickable = false
        binding.btnAddExercise.visibility = View.GONE
        binding.btnAddPhoto.visibility = View.GONE
        binding.btnAddVideo.visibility = View.GONE
        // 운동 완료 체크박스 isClickable = false

        // 일지 정보가 있으며 수정 버튼을 누른 경우 (작성 후 저장 요청)
        //binding.ivSave.visibility = View.VISIBLE
        //binding.ivEdit.visibility = View.GONE
        //binding.edtMemo.isClickable = true
        //binding.btnAddExercise.visibility = View.VISIBLE
        //binding.btnAddPhoto.visibility = View.VISIBLE
        //binding.btnAddVideo.visibility = View.VISIBLE

        // 일지 정보가 없을 경우(작성 후 저장 요청)
        //binding.ivSave.visibility = View.VISIBLE
        //binding.ivEdit.visibility = View.GONE
        //binding.edtMemo.isClickable = true
        //binding.edtMemo.hint = getString(R.string.please_input)
        //binding.btnAddExercise.visibility = View.VISIBLE
        //binding.btnAddPhoto.visibility = View.VISIBLE
        //binding.btnAddVideo.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }

        // 일지 정보 요청
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


}