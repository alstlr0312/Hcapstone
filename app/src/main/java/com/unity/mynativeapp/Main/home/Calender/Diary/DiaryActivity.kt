package com.unity.mynativeapp.Main.home.Calender

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise.AddExerciseActivity
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvItem
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryPhotoRv.DiaryPhotoRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryPhotoRv.DiaryPhotoRvItem
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityDiaryBinding
import java.io.File


lateinit var diaryActivity: DiaryActivity
class DiaryActivity : AppCompatActivity() {
    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
    lateinit var date: String   // 다이어리 날짜
    lateinit var exerciseAdapter: DiaryExerciseRvAdapter // 오늘의 운동 Rv 어댑터
    lateinit var photoAdapter: DiaryPhotoRvAdapter      // 사진 Rv 어댑터
    lateinit var videoAdapter: DiaryPhotoRvAdapter      // 영상 Rv 어댑터
    var firstStart = true
    var status = 0 // 0(read), 1(write)
    var galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        photoAdapter.addItem(uri!!)
    }
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == RESULT_OK){
            val imageUri = it.data?.data
            imageUri?.let{
                val imagePath = getRealPathFromUri(imageUri)
                photoAdapter.addItem(imageUri)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        diaryActivity = this
        // 다이어리 날짜 설정
        date = intent.getStringExtra("date").toString()
        binding.tvDate.text = date

        // 화면 모드
        status = intent.getIntExtra("mode", -1)

        exerciseAdapter = DiaryExerciseRvAdapter(this)
        binding.recyclerViewTodaysExercise.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTodaysExercise.adapter = exerciseAdapter

        photoAdapter = DiaryPhotoRvAdapter(this)
        binding.recyclerViewPhotos.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewPhotos.adapter = photoAdapter

        videoAdapter = DiaryPhotoRvAdapter(this)
        binding.recyclerViewVideos.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewVideos.adapter = videoAdapter




        setView()

        setClickListener()





    }

    private fun setView(){

        // 일지 상세 정보 조회 요청

        if(status == 0) setReadView()
        else setWriteView()


    }

    private fun setReadView(){ // 일지 정보가 있을 경우 -> 일지 조회 화면 (read)
        binding.ivEdit.visibility = View.VISIBLE            // 수정 아이콘 보이기
        binding.ivSave.visibility = View.INVISIBLE          // 저장 아이콘 숨기기
        binding.btnAddExercise.visibility = View.INVISIBLE  // 추가 버튼 숨기기
        binding.btnAddPhoto.visibility = View.INVISIBLE     //
        binding.btnAddVideo.visibility = View.INVISIBLE     //
        binding.edtMemo.hint = ""
        binding.edtMemo.isEnabled = false                   // 메모 수정 불가능
        exerciseAdapter.checkBoxIsClickable(false)       // 체크박스 수정 불가능

    }

    private fun setWriteView(){ // 일지 정보가 없거나 수정 버튼을 클릭한 경우 (Write)
        binding.ivSave.visibility = View.VISIBLE                // 저장 아이콘 보이기
        binding.ivEdit.visibility = View.GONE                   // 수정 아이콘 숨기기
        binding.btnAddExercise.visibility = View.VISIBLE        // 추가 버튼 보이기
        binding.btnAddPhoto.visibility = View.VISIBLE           //
        binding.btnAddVideo.visibility = View.VISIBLE           //
        binding.edtMemo.isEnabled = true                        // 메모 수정 가능
        exerciseAdapter.checkBoxIsClickable(true)            // 체크박스 수정 가능

        if(binding.edtMemo.text.toString() == "")
            binding.edtMemo.hint = getString(R.string.please_input)
    }

    private fun setClickListener(){

        // 운동 추가
        binding.btnAddExercise.setOnClickListener {
            var intent = Intent(this, AddExerciseActivity::class.java)
            intent.putExtra("date", date)
            startActivity(intent)
        }

        // 사진 추가
        binding.btnAddPhoto.setOnClickListener {
            if(photoAdapter.itemCount == 4){
                Toast.makeText(this, getString(R.string.you_can_register_four_medias), Toast.LENGTH_SHORT).show()
            }else{
                galleryLauncher.launch("image/*")
            }
        }



        // 영상 추가
        binding.btnAddVideo.setOnClickListener {

        }


        // 수정 아이콘 클릭
        binding.ivEdit.setOnClickListener {
            setWriteView()
        }

        // 저장 아이콘 클릭
        binding.ivSave.setOnClickListener {
            setReadView()
        }

        // 뒤로 가기
        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    // 이미지 실제 경로 변환
    fun getRealPathFromUri(uri: Uri): String {

        var buildName = Build.MANUFACTURER
        if (buildName.equals("Xiaomi")) {
            return uri.path!!
        }

        var columnIndex = 0
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }

        exerciseAdapter.notifyDataSetChanged()

    }

    // 더미
    fun getExerciseList(): MutableList<DiaryExerciseRvItem>{

        var list = mutableListOf<DiaryExerciseRvItem>()
        list.add(DiaryExerciseRvItem("스쿼트", 20, 10, false, 0, "하체", true))
        list.add(DiaryExerciseRvItem("런닝", 0, 0, true, 30, "유산소", false))
        return list
    }

    // 더미
    fun getPhotoList(): MutableList<DiaryPhotoRvItem>{
        var list = mutableListOf<DiaryPhotoRvItem>()
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        list.add(DiaryPhotoRvItem(R.drawable.photo01))
        return list
    }

    fun resizeDialog(dialog: Dialog, width: Double, height: Double){
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog.window?.attributes
        params?.width = (size.x * width).toInt()
        params?.height = (size.y * height).toInt()
        dialog.window?.attributes = params as WindowManager.LayoutParams

    }

}