package com.unity.mynativeapp.features.diary


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityDiaryBinding
import com.unity.mynativeapp.model.*
import com.unity.mynativeapp.network.util.LoadingDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


lateinit var diaryActivity: DiaryActivity
class DiaryActivity : BaseActivity<ActivityDiaryBinding>(ActivityDiaryBinding::inflate){
    private val viewModel by viewModels<DiaryViewModel>()

    private lateinit var exerciseDate: String               // 운동 날짜
    lateinit var exerciseAdapter: DiaryExerciseRvAdapter    // 오늘의 운동 Rv 어댑터
    lateinit var mediaAdapter: DiaryMediaRvAdapter          // 미디어 Rv 어댑터
    private var firstStart = true
    private var diaryId = -1
    private var tempMediaPathArr = arrayListOf<String>()
    private var oldData: DiaryResponse? = null
    var edited = false

    var mediaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK){
            val imageUri = result.data?.data
            imageUri?.let{
                if(it.toString().contains("image")){
                    mediaAdapter.addItem(MediaRvItem(1, it, null))
                }else if(it.toString().contains("video")){
                    mediaAdapter.addItem(MediaRvItem(2, it, null))
                }else{}
                //Log.d("aaaaa", getRealPathFromUri(it))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        diaryActivity = this
        //loadingDialog = LoadingDialog(this)


        setView()

        setUiEvent()

        subscribeUI()

    }

    private fun setView(){


        // 다이어리 날짜 설정
        val formatDate = intent.getStringExtra("formatDate").toString()
        binding.tvDate.text = formatDate
        exerciseDate = intent.getStringExtra("exerciseDate").toString()

        // 오늘의 운동 리사이클러 뷰
        exerciseAdapter = DiaryExerciseRvAdapter(this)
        binding.recyclerViewTodaysExercise.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTodaysExercise.adapter = exerciseAdapter

        // 미디어 리사이클러 뷰
        mediaAdapter = DiaryMediaRvAdapter(this)
        binding.recyclerViewMedia.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewMedia.adapter = mediaAdapter

        diaryId = intent.getIntExtra("diaryId", -1)
        if(diaryId != -1){ // 다이어리 상세 조회 요청
            viewModel.diaryDetail(exerciseDate)
        }

        //muscleFrontView = MuscleView(binding.viewMuscleFront.context, 0)
        //muscleBackView = MuscleView(binding.viewMuscleBack.context, 0)

    }





    @SuppressLint("ClickableViewAccessibility")
    private fun setUiEvent(){

        // 운동 추가
        binding.btnAddExercise.setOnClickListener {
            var intent = Intent(this, AddExerciseActivity::class.java)
            intent.putExtra("exerciseDate", exerciseDate)
            startActivity(intent)
        }

        // 미디어 추가
        binding.btnAddMedia.setOnClickListener {
            if(mediaAdapter.itemCount == 6){
                Toast.makeText(this, getString(R.string.you_can_register_six_medias), Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.AUTHORITY_URI, "image/* video/*")
                mediaResult.launch(intent)
            }
        }

        mediaAdapter.registerAdapterDataObserver(object: AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                edited = true
            }
        })

        // 뒤로 가기
        binding.btnBack.setOnClickListener {

            if(exerciseAdapter.itemCount != 0) {
                // 운동일지 작성 or 수정 요청

                // 운동
                val jsonRequest = DiaryWriteRequest(
                    exerciseAdapter.getExerciseList(),
                    binding.edtMemo.text.toString(),
                    exerciseDate
                )

                val gson = GsonBuilder().serializeNulls().create()
                val requestBodyString = gson.toJson(jsonRequest).toString()
                val requestBodyWithoutBackslashes = requestBodyString.replace("\\", "")
                val diaryData = createPartFromString(requestBodyWithoutBackslashes)
                // 미디어
                val mediaList = mediaAdapter.getMediaList()
                val imageList: ArrayList<MultipartBody.Part> = ArrayList()

                if(diaryId != -1){ // 다이어리 수정

                    // 수정 데이터 있는지 확인
                    if(!edited && oldData?.review == binding.edtMemo.text.toString()){
                        finish()
                    }
                    for(i in mediaList.indices){
                        val bitmap = mediaList[i].bitmap
                        val uri = mediaList[i].uri
                        var file = File("")
                        if(bitmap != null){
                            val realPath = saveMedia(bitmap, "temp${i}")
                            file = File(realPath)
                        }else if(uri != null){
                            file = File(getRealPathFromUri(uri))
                        }
                        val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                        val uploadFile: MultipartBody.Part = MultipartBody.Part.createFormData("files",  file.name, requestFile)
                        imageList.add(uploadFile)
                    }
                    viewModel.diaryEdit(diaryId, diaryData, imageList)


                }else{ // 다이어리 작성
                    for (element in mediaList) {
                        if(element.uri != null){
                            val file = File(getRealPathFromUri(element.uri))
                            val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                            val uploadFile: MultipartBody.Part = MultipartBody.Part.createFormData("files",  file.name, requestFile)
                            imageList.add(uploadFile)
                        }
                    }
                    viewModel.diaryWrite(diaryData, imageList) // 다이어리 작성 요청
                }
            }else{
                finish()
                //Toast.makeText(this, "오늘의 운동을 추가해 주세요", Toast.LENGTH_SHORT).show()
            }

        }

        // 근육 버튼 이벤트
        // 신체 앞면
        binding.layoutMuscleFront.setOnTouchListener { _, event ->
            when(event.action){ // 260x554
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x; val y = event.y
                    binding.tvFrontMuscleName.visibility = View.VISIBLE
                    //240
                    if (x in 160.0..320.0 && y in 200.0..275.0){ // 가슴
                        Log.d("MuscleView","MuscleView ($x, $y): 대흉근(가슴)")
                        binding.ivFrontMuscle.setImageResource(R.drawable.img_muscle_front_chest)
                        binding.tvFrontMuscleName.text = getString(R.string.muscle_chest)
                    }else if((x in 90.0..160.0 || x in 320.0..390.0) && y in 170.0..250.0){ // 어깨
                        Log.d("MuscleView","MuscleView ($x, $y): 삼각근(어깨)")
                        binding.ivFrontMuscle.setImageResource(R.drawable.img_muscle_front_shoulder)
                        binding.tvFrontMuscleName.text = getString(R.string.muscle_shoulder)

                    }else if((x in 80.0..155.0 || x in 325.0..400.0) && y in 250.0..340.0){ // 이두근
                        Log.d("MuscleView","MuscleView ($x, $y): 이두근")
                        binding.ivFrontMuscle.setImageResource(R.drawable.img_muscle_front_biceps)
                        binding.tvFrontMuscleName.text = getString(R.string.muscle_biceps)

                    }else if((x in 50.0..140.0 || x in 340.0..420.0) && y in 340.0..470.0){ // 전완근
                        Log.d("MuscleView","MuscleView ($x, $y): 전완근")
                        binding.ivFrontMuscle.setImageResource(R.drawable.img_muscle_front_forearms)
                        binding.tvFrontMuscleName.text = getString(R.string.muscle_forearms)

                    }else if(x in 170.0..310.0 && y in 290.0..470.0){ // 복부근
                        Log.d("MuscleView","MuscleView ($x, $y): 복부")
                        binding.ivFrontMuscle.setImageResource(R.drawable.img_muscle_front_abs)
                        binding.tvFrontMuscleName.text = getString(R.string.muscle_abs)

                    }else if(x in 130.0..350.0 && y in 470.0..670.0){ // 허벅지
                        Log.d("MuscleView","MuscleView ($x, $y): 대퇴근(허벅지)")
                        binding.ivFrontMuscle.setImageResource(R.drawable.img_muscle_front_quadriceps)
                        binding.tvFrontMuscleName.text = getString(R.string.muscle_quadriceps)

                    }else if(x in 130.0..350.0 && y in 670.0..840.0){ // 종아리
                        Log.d("MuscleView","MuscleView ($x, $y): 비복근(종아리)")
                        binding.ivFrontMuscle.setImageResource(R.drawable.img_muscle_front_calves)
                        binding.tvFrontMuscleName.text = getString(R.string.muscle_calves)

                    }else{
                        Log.d("MuscleView","MuscleView ($x, $y): 밖")
                        binding.ivFrontMuscle.setImageResource(R.drawable.img_muscle_front_body)
                        binding.tvFrontMuscleName.visibility = View.GONE

                    }

                }
            }
            return@setOnTouchListener true
        }

        // 신체 앞면
        binding.layoutMuscleBack.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x; val y = event.y
                    binding.tvBackMuscleName.visibility = View.VISIBLE

                    if (x in 190.0..280.0 && y in 130.0..270.0){ // 승모근
                        Log.d("MuscleView","MuscleView ($x, $y): 승모근")
                        binding.ivBackMuscle.setImageResource(R.drawable.img_muscle_back_trapezius)
                        binding.tvBackMuscleName.text = getString(R.string.muscle_trapezius)
                    }else if (x in 170.0..310.0 && y in 300.0..380.0){ // 광배근
                        Log.d("MuscleView","MuscleView ($x, $y): 광배근")
                        binding.ivBackMuscle.setImageResource(R.drawable.img_muscle_back_lat)
                        binding.tvBackMuscleName.text = getString(R.string.muscle_lat)
                    }else if (x in 180.0..310.0 && y in 440.0..520.0){ // 대둔근
                        Log.d("MuscleView","MuscleView ($x, $y): 대둔근")
                        binding.ivBackMuscle.setImageResource(R.drawable.img_muscle_back_glutes)
                        binding.tvBackMuscleName.text = getString(R.string.muscle_glutes)
                    }else if ((x in 90.0..160.0 || x in 320.0..390.0) && y in 190.0..260.0){ // 삼각근
                        Log.d("MuscleView","MuscleView ($x, $y): 삼각근")
                        binding.ivBackMuscle.setImageResource(R.drawable.img_muscle_back_shoulder)
                        binding.tvBackMuscleName.text = getString(R.string.muscle_shoulder)
                    }else if ((x in 70.0..150.0 || x in 330.0..410.0) && y in 270.0..340.0){ // 삼두근
                        Log.d("MuscleView","MuscleView ($x, $y): 삼두근")
                        binding.ivBackMuscle.setImageResource(R.drawable.img_muscle_back_triceps)
                        binding.tvBackMuscleName.text = getString(R.string.muscle_triceps)
                    }else if ((x in 65.0..130.0 || x in 350.0..415.0) && y in 350.0..455.0){ // 전완근
                        Log.d("MuscleView","MuscleView ($x, $y): 전완근")
                        binding.ivBackMuscle.setImageResource(R.drawable.img_muscle_back_forearms)
                        binding.tvBackMuscleName.text = getString(R.string.muscle_forearms)
                    }else if (x in 140.0..400.0 && y in 530.0..690.0){ // 햄스트링
                        Log.d("MuscleView","MuscleView ($x, $y): 햄스트링")
                        binding.ivBackMuscle.setImageResource(R.drawable.img_muscle_back_hamstrings)
                        binding.tvBackMuscleName.text = getString(R.string.muscle_hamstrings)
                    }else if (x in 140.0..400.0 && y in 710.0..850.0){ // 비복근
                        Log.d("MuscleView","MuscleView ($x, $y): 비복근")
                        binding.ivBackMuscle.setImageResource(R.drawable.img_muscle_back_calves)
                        binding.tvFrontMuscleName.text = getString(R.string.muscle_calves)
                    }else{
                        Log.d("MuscleView","MuscleView ($x, $y): 밖")
                        binding.ivBackMuscle.setImageResource(R.drawable.img_muscle_back_body)
                        binding.tvBackMuscleName.visibility = View.GONE
                    }
                }
            }
            return@setOnTouchListener true
        }
    }



    override fun finish() {
        super.finish()
        deleteMediaFile()
    }

    fun createPartFromString(stringData: String): RequestBody {
        return stringData.toRequestBody("application/json".toMediaTypeOrNull())
    }

    override fun onRestart() {
        super.onRestart()

    }


    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }

        exerciseAdapter.notifyDataSetChanged()

    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            showCustomToast(message)
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }

        viewModel.logout.observe(this) { logout ->
            if(logout) logout()
        }

        // 다이어리 상세 조회
        viewModel.diaryData.observe(this) { data ->

            if (data == null){  // 다이어리 상세조회 실패
                //setWriteView()
            }else{ // 다이어리 상세조회 성공

                //setReadView()   // 읽기 모드
                oldData = data

                diaryId = data.diaryId
                binding.edtMemo.setText(data.review)

                for(item in data.exerciseInfo){
                    /*
                    val exerciseName = x.exerciseName
                    val reps = x.reps
                    val exSetCount = x.exSetCount
                    val isCardio = x.cardio
                    val cardioTime = x.cardioTime
                    var bodyPart = x.bodyPart
                    val finished = x.finished

                    exerciseAdapter.addItem(DiaryExerciseRvItem(exerciseName, reps, exSetCount,
                        isCardio, cardioTime, bodyPart, finished))

                     */
                    exerciseAdapter.addItem(item)
                }

                for(x in data.mediaList){
                    val lastSegment = x.substringAfterLast("/").toInt()
                    viewModel.media(lastSegment)
                }
            }
        }

        viewModel.mediaData.observe(this) { data ->
            if (data != null) {
                val byteArray = data.bytes()
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                mediaAdapter.addItem(MediaRvItem(3, null, bitmap))
                edited = false
            }
        }

        // 다이어리 작성
        viewModel.diaryWriteSuccess.observe(this) { isSuccess ->

            if(!isSuccess) return@observe

            finish()
        }

        // 다이어리 수정
        viewModel.diaryEditSuccess.observe(this) { isSuccess ->



            if(!isSuccess){ return@observe }

            finish()

        }
    }

    companion object{
        const val TAG = "DiaryActivity"
    }



    // 이미지 절대 경로 얻기 위해, 서버로 부터 받은 이미지 캐쉬에 임시 저장
    private fun saveMedia(bitmap: Bitmap, name: String): String {
        val tempFile = File(cacheDir, "$name.jpg")

        try {
            tempFile.createNewFile()

            val out = FileOutputStream(tempFile)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

            out.close()
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "FileNotFoundException : " + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "IOException : " + e.message)
        }
        tempMediaPathArr.add(tempFile.path)
        return tempFile.absolutePath
    }

    // 임시로 저장 해놓은 이미지 삭제
    private fun deleteMediaFile(){ // 임시 미디어 삭제
        for(path in tempMediaPathArr){
            val file = File(path)
            if(file.exists()){
                Log.d(TAG, "file name: ${path}")
                file.delete()
            }
        }
    }

}