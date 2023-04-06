package com.unity.mynativeapp.features.diary

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.model.DiaryWriteRequest
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityDiaryBinding
import com.unity.mynativeapp.model.DiaryExerciseRvItem
import com.unity.mynativeapp.util.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

lateinit var diaryActivity: DiaryActivity

class DiaryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DiaryViewModel>()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var exerciseDate: String   // 운동 날짜
    lateinit var exerciseAdapter: DiaryExerciseRvAdapter // 오늘의 운동 Rv 어댑터


    lateinit var mediaAdapter: DiaryMediaRvAdapter      // 미디어 Rv 어댑터
    private lateinit var mediaAdapter2: DiaryMediaRvAdapter2      // 미디어 Rv 어댑터
    private var firstStart = true
    private var status = 1 // 0(read), 1(write)

    var imageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK){
            val imageUri = result.data?.data
            imageUri?.let{
                mediaAdapter.addItem(it)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        diaryActivity = this
        loadingDialog = LoadingDialog(this)

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


        mediaAdapter2 = DiaryMediaRvAdapter2(this)
        binding.exrv.layoutManager = GridLayoutManager(this, 2)
        binding.exrv.adapter = mediaAdapter2
        viewModel.home(exerciseDate)
        subscribeUI()
        // setView()

        setUiEvent()


    }


    private fun setView(){

        if(status == 0) setReadView()
        else setWriteView()
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

    private fun setUiEvent() {

        // 운동 추가
        binding.btnAddExercise.setOnClickListener {
            var intent = Intent(this, AddExerciseActivity::class.java)
            intent.putExtra("exerciseDate", exerciseDate)
            startActivity(intent)
        }

        // 미디어 추가
        binding.btnAddMedia.setOnClickListener {
            if (mediaAdapter.itemCount == 4) {
                Toast.makeText(this,
                    getString(R.string.you_can_register_four_medias), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                imageResult.launch(intent)
            }
        }


        // 수정 아이콘 클릭
        binding.ivEdit.setOnClickListener {
            setWriteView()
        }

        // 저장 아이콘 클릭
        binding.ivSave.setOnClickListener {

            if (exerciseAdapter.itemCount != 0) {
                // 운동일지 작성 or 수정 요청

                // 운동
                val jsonRequest = DiaryWriteRequest(
                    exerciseAdapter.getExerciseList(),
                    binding.edtMemo.text.toString(),
                    exerciseDate
                )
                //    val jsonBody = RequestBody.create(parse("application/json"),jsonObject2)


                val gson = GsonBuilder().serializeNulls().create()
                val jsonObject = JSONObject().put("writeDiaryDto", gson.toJson(jsonRequest))
                Log.d("diaryActivity", jsonObject.toString())
                val jsonObject11 = JSONObject(jsonObject.toString())
                val data = createPartFromString(jsonObject.toString())


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
                    Log.d("234234234234", element)
                }

                viewModel.diaryWrite(exdata, imageList)
                Log.d("diaryActivity111111", jsonObject11.toString())
            }else{
                Toast.makeText(this, "오늘의 운동을 추가해주세요", Toast.LENGTH_SHORT).show()
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




    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }

        exerciseAdapter.notifyDataSetChanged()

    }

    private fun subscribeUI() {

        viewModel.diaryData.observe(this) { data ->

            if (data == null){  // 다이어리 목록 없음
                setWriteView()
            }else{
                setReadView()
                val getReview = data.review.toString()
                Log.d("getReview", getReview)
                val getexInfo = data.exerciseInfo
                Log.d("getexInfo", getexInfo.toString())
                for(x in getexInfo){
                    val exerciseName = x.exerciseName
                    val reps = x.reps
                    val exSetCount = x.exSetCount
                    val isCardio = x.cardio
                    val cardioTime = x.cardioTime
                    val bodyPart = x.bodyPart
                    Log.d("exerciseName", exerciseName.toString())
                    Log.d("reps", reps.toString())
                    Log.d("exSetCount", exSetCount.toString())
                    Log.d("isCardio", isCardio.toString())
                    Log.d("cardioTime", cardioTime.toString())
                    Log.d("bodyPart", bodyPart)
                    exerciseAdapter.addItem(DiaryExerciseRvItem(exerciseName.toString(), reps, exSetCount, isCardio, cardioTime, bodyPart, false))
                }
                val getMedia = data.mediaList
                Log.d("bodyPart", getMedia.toString())
                binding.edtMemo.setText(getReview.toString())
                    for(x in getMedia){
                        val lastSegment = x.substringAfterLast("/").toInt()
                        viewModel.media(lastSegment)
                        viewModel.mediaData.observe(this) { data2 ->
                            if (data2 != null) {
                                Log.d("bodyPartsdfasd",data2.toString())
                                mediaAdapter2.addItem(data2.bytes())
                            }
                        }
                    }


            }
        }


        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) loadingDialog.show() else loadingDialog.dismiss()
        }

        viewModel.diaryWriteSuccess.observe(this) { isSuccess ->

            if(!isSuccess) return@observe

            setReadView()
        }
    }



}