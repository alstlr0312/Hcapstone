package com.unity.mynativeapp.features.diary

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityDiaryBinding
import com.unity.mynativeapp.features.home.HomeViewModel
import com.unity.mynativeapp.model.DiaryExerciseRvItem
import com.unity.mynativeapp.model.DiaryWriteRequest
import com.unity.mynativeapp.model.MediaRvItem
import com.unity.mynativeapp.network.util.LoadingDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


lateinit var diaryActivity: DiaryActivity
class DiaryActivity : BaseActivity<ActivityDiaryBinding>(ActivityDiaryBinding::inflate){
    private val viewModel by viewModels<DiaryViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var exerciseDate: String               // 운동 날짜
    lateinit var exerciseAdapter: DiaryExerciseRvAdapter    // 오늘의 운동 Rv 어댑터
    lateinit var mediaAdapter: DiaryMediaRvAdapter          // 미디어 Rv 어댑터
    //lateinit var mediaAdapter2: DiaryMediaRvAdapter2        // 미디어 Rv 어댑터
    private var firstStart = true
    private var status = -1 // 0(read), 1(write)
    private var diaryId = -1
    private var isEditing = false

    var imageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == RESULT_OK){
            val imageUri = result.data?.data
            imageUri?.let{
                mediaAdapter.addItem(MediaRvItem(1, it, null))
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

        // 화면 모드
        status = intent.getIntExtra("mode", -1)

        exerciseAdapter = DiaryExerciseRvAdapter(this)
        binding.recyclerViewTodaysExercise.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTodaysExercise.adapter = exerciseAdapter

        mediaAdapter = DiaryMediaRvAdapter(this)
        binding.recyclerViewMedia.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewMedia.adapter = mediaAdapter


        if(status == 0) {
            setReadView()
            viewModel.diaryDetail(exerciseDate) // 다이어리 상세 조회 요청
        }else if(status == 1){
            setWriteView()
        } else{
            Log.d(TAG, "$status")
        }

    }

    private fun setReadView(){ // 일지 정보가 있을 경우 -> 일지 조회 화면 (read)
        binding.ivEdit.visibility = View.VISIBLE            // 수정 아이콘 보이기
        binding.ivSave.visibility = View.INVISIBLE          // 저장 아이콘 숨기기
        binding.btnAddExercise.visibility = View.INVISIBLE  // 추가 버튼 숨기기
        binding.btnAddMedia.visibility = View.INVISIBLE     //
        binding.edtMemo.hint = ""
        binding.edtMemo.isEnabled = false                   // 메모 수정 불가능
        exerciseAdapter.checkBoxIsClickable(false)       // 체크박스 수정 불가능
    }

    private fun setWriteView(){ // 일지 정보가 없거나 수정 버튼을 클릭한 경우 (Write)
        binding.ivSave.visibility = View.VISIBLE                // 저장 아이콘 보이기
        binding.ivEdit.visibility = View.GONE                   // 수정 아이콘 숨기기
        binding.btnAddExercise.visibility = View.VISIBLE        // 추가 버튼 보이기
        binding.btnAddMedia.visibility = View.VISIBLE           //
        binding.edtMemo.isEnabled = true                        // 메모 수정 가능
        exerciseAdapter.checkBoxIsClickable(true)            // 체크박스 수정 가능

        if(binding.edtMemo.text.toString() == "")
            binding.edtMemo.hint = getString(R.string.please_input)
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
            if(mediaAdapter.itemCount == 4){
                Toast.makeText(this, getString(R.string.you_can_register_four_medias), Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                imageResult.launch(intent)
            }
        }


        // 수정 아이콘 클릭
        binding.ivEdit.setOnClickListener {
            isEditing = true
            setWriteView()
        }

        // 저장 아이콘 클릭
        binding.ivSave.setOnClickListener {

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
                val imageList: ArrayList<MultipartBody.Part> = ArrayList()
                for (element in mediaAdapter.getMediaList()) {
                    val file = File(element)
                    val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    val uploadFile: MultipartBody.Part = MultipartBody.Part.createFormData("files",  file.name, requestFile)
                    imageList.add(uploadFile)
                }

                if(isEditing){ // 다이어리 수정
                    viewModel.diaryEdit(exdata, imageList, diaryId)
                }else{ // 다이어리 작성
                    viewModel.diaryWrite(exdata, imageList) // 다이어리 작성 요청
                }

            }else{
                Toast.makeText(this, "오늘의 운동을 추가해 주세요", Toast.LENGTH_SHORT).show()
            }
        }

        // 뒤로 가기
        binding.btnBack.setOnClickListener {
            finish()
        }


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
                setWriteView()
            }else{ // 다이어리 상세조회 성공

                setReadView()   // 읽기 모드

                diaryId = data.diaryId
                binding.edtMemo.setText(data.review)

                Log.d("aaaaa", data.exerciseInfo.toString())

                for(x in data.exerciseInfo){


                    val exerciseName = x.exerciseName
                    val reps = x.reps
                    val exSetCount = x.exSetCount
                    val isCardio = x.cardio
                    val cardioTime = x.cardioTime
                    var bodyPart = x.bodyPart
                    val finished = x.finished

                    exerciseAdapter.addItem(DiaryExerciseRvItem(exerciseName, reps, exSetCount,
                        isCardio, cardioTime, bodyPart, finished))
                }

                for(x in data.mediaList){
                    val lastSegment = x.substringAfterLast("/").toInt()
                    viewModel.media(lastSegment)
                    viewModel.mediaData.observe(this) { data2 ->
                        if (data2 != null) {
                            Log.d("bodyPartsdfasd",data2.toString())
                            mediaAdapter.addItem(MediaRvItem(2, null, data2.bytes()))
                        }
                    }
                }
            }
        }

        // 다이어리 작성
        viewModel.diaryWriteSuccess.observe(this) { isSuccess ->

            if(!isSuccess) return@observe

            setReadView()
        }

        // 다이어리 수정
        viewModel.diaryEditSuccess.observe(this) { isSuccess ->

            if(!isSuccess) return@observe

            setReadView()
            isEditing = false

        }
    }

    companion object{
        const val TAG = "DiaryActivity"
    }

}