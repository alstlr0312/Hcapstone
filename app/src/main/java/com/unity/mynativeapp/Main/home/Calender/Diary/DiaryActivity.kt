package com.unity.mynativeapp.Main.home.Calender.Diary


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.ApplicationClass.Companion.API_URL
import com.unity.mynativeapp.ApplicationClass.Companion.okHttpClient
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise.AddExerciseActivity
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.DiaryExerciseRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DairyMediaRv.DiaryMediaRvAdapter
import com.unity.mynativeapp.Main.home.Calender.Diary.DiaryExerciseRv.AddExercise.AddexModels.ExResponse
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityDiaryBinding
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


lateinit var diaryActivity: DiaryActivity
class DiaryActivity : AppCompatActivity() {
    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
    lateinit var date: String   // 다이어리 날짜
    lateinit var exerciseAdapter: DiaryExerciseRvAdapter // 오늘의 운동 Rv 어댑터
    lateinit var mediaAdapter: DiaryMediaRvAdapter      // 사진 Rv 어댑터
    var firstStart = true
    var status = 0 // 0(read), 1(write)
    val data = JSONObject()
    val exdata = JSONArray()
    var galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        if(uri != null){
            mediaAdapter.addItem(uri)
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

        mediaAdapter = DiaryMediaRvAdapter(this)
        binding.recyclerViewMedia.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewMedia.adapter = mediaAdapter


        setView()
        init()
        setClickListener()


    }
    private fun init() {
        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                val intent = it.data
                val returnValue = intent!!.getStringExtra("exerciseInfo")
                Toast.makeText(this, returnValue.toString(), Toast.LENGTH_SHORT).show()
                exdata.put(returnValue)
            }

        }
        binding.btnAddExercise.setOnClickListener {
            val intent = Intent(this, AddExerciseActivity::class.java)
            intent.putExtra("date", date)
            activityResultLauncher.launch(intent)
            // var intent = Intent(this, AddExerciseActivity::class.java)
            //intent.putExtra("date", date)
            //startActivity(intent)
        }
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
        binding.btnAddMedia.visibility = View.INVISIBLE     //+
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
        /*binding.btnAddExercise.setOnClickListener {
            val intent = Intent(this, AddExerciseActivity::class.java)
            intent.putExtra("date", date)
            activityResultLauncher.launch(intent)
           // var intent = Intent(this, AddExerciseActivity::class.java)
            //intent.putExtra("date", date)
            //startActivity(intent)

        }*/

        // 미디어 추가
        binding.btnAddMedia.setOnClickListener {
            if (mediaAdapter.itemCount == 4) {
                Toast.makeText(
                    this,
                    getString(R.string.you_can_register_four_medias),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                galleryLauncher.launch("image/*")
            }
        }


        // 수정 아이콘 클릭
        binding.ivEdit.setOnClickListener {
            setWriteView()
        }

        // 저장 아이콘 클릭
        binding.ivSave.setOnClickListener {

           /* if (exerciseAdapter.itemCount != 0) {
                // 운동일지 작성 or 수정 요청
                data.put("writeDiaryDto",exdata)
                data.put("review", binding.edtMemo.text.toString())
                data.put("date",date)
                val URI = "http://175.114.240.162:8080/diary/write/"
                val retrofit = Retrofit.Builder()
                    .baseUrl(URI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val api = retrofit.create(AddexResponse::class.java)
                val callpostexdata = api.AddexResponse(uid,data.toString())

                callpostexdata.enqueue(object : Callback<ExData> {
                    override fun onResponse(
                        call: retrofit2.Call<ExData>,
                        response: Response<ExData>
                    ) {
                        Log.d(ContentValues.TAG, "성공 : ${response.raw()}")
                    }

                    override fun onFailure(call: retrofit2.Call<ExData>, t: Throwable) {
                        Log.d(ContentValues.TAG, "실패 : $t")
                    }
                })*/
            data.put("exerciseInfo",exdata)
            data.put("review", binding.edtMemo.text.toString())
            data.put("date",date)
            val requestBody: RequestBody =
                RequestBody.create("application/json; charset=utf-8".toMediaType(), data.toString())
            Log.d("signUpActivity",data.toString())
            val url = API_URL + "diary/write"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()


            okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {

                override fun onResponse(call: Call, response: okhttp3.Response) {
                    if (response.body != null) {
                        val body = response.body?.string()
                        val gson = GsonBuilder().create()
                        val data = gson.fromJson(body, ExResponse::class.java)

                        Log.d("signUpActivity",data.toString())

                        if(data.status == 500){
                            runOnUiThread {
                                Toast.makeText(this@DiaryActivity, data.error.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }else if(data.status == 201){

                            runOnUiThread {
                                Toast.makeText(this@DiaryActivity, "성공", Toast.LENGTH_SHORT).show()
                            }
                            finish()
                        } else if(data.status == 415){
                            runOnUiThread {
                                Toast.makeText(this@DiaryActivity, data.error.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }else if(data.status == 401){
                            runOnUiThread {
                                Toast.makeText(this@DiaryActivity, data.error.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }


                    } else {
                        Log.d("DiaryActivity","response body is null")
                    }
                }
                override fun onFailure(call: Call, e: IOException) {

                    Log.d("DiaryActivity", e.message.toString())
                }
            })
        }
    }





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

}