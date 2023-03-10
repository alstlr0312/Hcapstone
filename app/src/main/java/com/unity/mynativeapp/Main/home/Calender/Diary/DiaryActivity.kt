package com.unity.mynativeapp.Main.home.Calender.Diary


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.Main.home.Calender.Diary.DairyMediaRv.DiaryMediaRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise.AddExerciseActivity
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteRequest
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryWrite.DiaryWriteResponse
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityDiaryBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

lateinit var diaryActivity: DiaryActivity

class DiaryActivity : AppCompatActivity(), DiaryActivityInterface {
    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
    lateinit var diaryDate: String   // 다이어리 날짜
    lateinit var exerciseAdapter: DiaryExerciseRvAdapter // 오늘의 운동 Rv 어댑터
    lateinit var mediaAdapter: DiaryMediaRvAdapter      // 사진 Rv 어댑터

    var firstStart = true
    var status = 0 // 0(read), 1(write)

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

        // 다이어리 날짜 설정
        val formatDate = intent.getStringExtra("formatDate").toString()
        binding.tvDate.text = formatDate
        diaryDate = intent.getStringExtra("diaryDate").toString()

        // 화면 모드
        status = intent.getIntExtra("mode", -1)

        exerciseAdapter = DiaryExerciseRvAdapter(this)
        binding.recyclerViewTodaysExercise.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTodaysExercise.adapter = exerciseAdapter

        mediaAdapter = DiaryMediaRvAdapter(this)
        binding.recyclerViewMedia.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewMedia.adapter = mediaAdapter


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

    private fun setClickListener() {

        // 운동 추가
        binding.btnAddExercise.setOnClickListener {
            var intent = Intent(this, AddExerciseActivity::class.java)
            intent.putExtra("diaryDate", diaryDate)
            startActivity(intent)
        }

        // 미디어 추가
        binding.btnAddMedia.setOnClickListener {
            if (mediaAdapter.itemCount == 4) {
                Toast.makeText(
                    this,
                    getString(R.string.you_can_register_four_medias),
                    Toast.LENGTH_SHORT
                ).show()
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
                    diaryDate
                )
                val gson = GsonBuilder().serializeNulls().create()
                val datjson = gson.toJson(jsonRequest)
                val body = datjson.toString().toRequestBody("application/json".toMediaType())


                val requestBody = MultipartBody.Builder().addPart(body)
                val rBody = MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("writeDiaryDto", body.toString())

                Log.d("sdfsdfsdfsdffffsd", datjson.toString())
                Log.d("tlrtlrtlr", rBody.toString())
                // 미디어
                for (element in mediaAdapter.getMediaList()) {
                    val file = File(element)
                    val requestFile = RequestBody.create("image/*".toMediaType(), file)
                    val uploadFile = MultipartBody.Part.createFormData("files", file.name,  requestFile)
                    Log.d("diaryActivity", element)
                }
                //
                var requestName: RequestBody =
                    datjson.toRequestBody("text/plain".toMediaTypeOrNull())
                val partMap : HashMap<String, RequestBody> = hashMapOf()
                partMap.put("writeDiaryDto", requestName)
                System.out.println(partMap.get("writeDiaryDto"))
                StoryService(this).writeStory2(partMap)
                //
                //StoryService(this).writeStory(rBody)
               //   DiaryActivityService(this).tryPostDiaryWrite(rBody.build())


            } else {
                Toast.makeText(this, "오늘의 운동을 추가해주세요", Toast.LENGTH_SHORT).show()
            }


            // 뒤로 가기
            binding.btnBack.setOnClickListener {
                finish()
            }

        }
    }
//
    //

   override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }

        exerciseAdapter.notifyDataSetChanged()

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

    override fun onPostLoginSuccess(response: DiaryWriteResponse) {
        runOnUiThread {
            Toast.makeText(this, response.status.toString() + " " + response.error.toString(), Toast.LENGTH_SHORT).show()
        }


        if(response.status == 200){
            //응답 성공
            setReadView()
        }
    }

    override fun onPostLoginFailure(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        }
        Log.d("diaryActivity", message)
    }

}

