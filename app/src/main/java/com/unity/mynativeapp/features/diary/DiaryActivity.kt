package com.unity.mynativeapp.features.diary


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityDiaryBinding
import com.unity.mynativeapp.model.*
import com.unity.mynativeapp.network.util.SimpleDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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

        // 다이어리 상세 조회 요청
        diaryId = intent.getIntExtra("diaryId", -1)
        if(diaryId != -1){
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

        exerciseAdapter.registerAdapterDataObserver(object: AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                edited = true
            }
        })

        // 뒤로 가기
        binding.btnBack.setOnClickListener {
            diaryRequest()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        diaryRequest()
    }

    private fun diaryRequest(){
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
                    val uri = mediaList[i].uri
                    val url = mediaList[i].url
                    var file: File? = null
                    if(url != null){
                        saveMedia(url, "image${i}") { filePath ->
                            if(filePath != null){
                                file = File(filePath)
                            }else {
                                file = null
                            }
                        }
                    }else if(uri != null){
                        file = File(getRealPathFromUri(uri))
                    }else{return}
                    if(file != null){
                        val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                            file!!
                        )
                        val uploadFile: MultipartBody.Part = MultipartBody.Part.createFormData("files",  file!!.name, requestFile)
                        imageList.add(uploadFile)
                    }
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
            var dialog = SimpleDialog(
                this,
                getString(R.string.cancel_write),
                getString(R.string.no_save_without_exercise)
            )
            dialog.show()

            var btnYes = dialog.findViewById<TextView>(R.id.btn_yes)
            var btnNo = dialog.findViewById<TextView>(R.id.btn_no)

            btnYes.setOnClickListener {
                dialog.dismiss()
                finish()
            }
            btnNo.setOnClickListener {
                dialog.dismiss()
            }
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
        }else{
            overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right)
        }

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
                diaryId = -1
            }else{ // 다이어리 상세조회 성공

                //setReadView()   // 읽기 모드
                oldData = data

                diaryId = data.diaryId
                binding.edtMemo.setText(data.review)

                for(item in data.exerciseInfo){
                    exerciseAdapter.addItem(item)
                }
                for(media in data.mediaList){
                    mediaAdapter.addItem(MediaRvItem(3, null, media))
                }

                edited = false

//                for(x in data.mediaList){
//                    val lastSegment = x.substringAfterLast("/").toInt()
//                    viewModel.media(lastSegment)
//                }
            }
        }

        // 다이어리 작성
        viewModel.diaryWriteSuccess.observe(this) { isSuccess ->

            if(!isSuccess) return@observe

            deleteMediaFile()

        }

        // 다이어리 수정
        viewModel.diaryEditSuccess.observe(this) { isSuccess ->



            if(!isSuccess){ return@observe }

            deleteMediaFile()

        }
    }

    companion object{
        const val TAG = "DiaryActivity"
    }



    // 이미지 절대 경로 얻기 위해, 서버로 부터 받은 이미지 캐쉬에 임시 저장
    private fun saveMedia(imageUrl: String, fileName: String, callback: (String?) -> Unit) {

        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?){
                    val file = File(cacheDir,
                        "${getString(R.string.app_name)}_${SimpleDateFormat("yyyyMMddhhmmss").format(Date(System.currentTimeMillis()))}.jpg")
                    try {
                        file.createNewFile()
                        FileOutputStream(file).use { outputStream ->
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        }
                        Log.d("fileName", file.path)
                        tempMediaPathArr.add(file.path)
                        callback(file.path)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback(null)
                    }

                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // Do nothing
                }
            })
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
        finish()
    }


}