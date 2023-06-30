package com.unity.mynativeapp.features.diary


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
        loadingDialog = LoadingDialog(this)


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
    }






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
                val exdata = createPartFromString(requestBodyWithoutBackslashes)

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
                    viewModel.diaryEdit(exdata, imageList, diaryId)


                }else{ // 다이어리 작성
                    for (element in mediaList) {
                        if(element.uri != null){
                            val file = File(getRealPathFromUri(element.uri))
                            val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                            val uploadFile: MultipartBody.Part = MultipartBody.Part.createFormData("files",  file.name, requestFile)
                            imageList.add(uploadFile)
                        }
                    }
                    viewModel.diaryWrite(exdata, imageList) // 다이어리 작성 요청
                }
            }else{
                finish()
                //Toast.makeText(this, "오늘의 운동을 추가해 주세요", Toast.LENGTH_SHORT).show()
            }

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
            if (isLoading) loadingDialog.show() else loadingDialog.dismiss()
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