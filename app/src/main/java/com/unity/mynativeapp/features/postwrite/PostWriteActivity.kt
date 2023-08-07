package com.unity.mynativeapp.features.postwrite

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.MyApplication.Companion.postCategoryHashMap
import com.unity.mynativeapp.MyApplication.Companion.postExerciseTypeHashMap
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityPostWriteBinding
import com.unity.mynativeapp.features.diary.DiaryActivity
import com.unity.mynativeapp.model.MediaRvItem
import com.unity.mynativeapp.model.PostWriteRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class PostWriteActivity : BaseActivity<ActivityPostWriteBinding>(ActivityPostWriteBinding::inflate) {

    private lateinit var postCategoryArr: ArrayList<String>
    private lateinit var postExerciseTypeArr: ArrayList<String>
    lateinit var mediaAdapter: PostWriteMediaRvAdapter      // 미디어 Rv 어댑터
    private val viewModel by viewModels<PostWriteViewModel>()
    private var editing = false
    private var postData: PostWriteRequest?= null
    private var postFilePathList = arrayListOf<String>()
    private var postId = -1

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
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){

        postCategoryArr = arrayListOf(getString(R.string.please_select_category),
            getString(R.string.q_and_a), getString(R.string.knowledge_sharing),
            getString(R.string.show_off), getString(R.string.assess), getString(R.string.free))

        postExerciseTypeArr = arrayListOf(getString(R.string.please_select_exercise_type),
            getString(R.string.health), getString(R.string.pilates), getString(R.string.yoga),
            getString(R.string.jogging), getString(R.string.etc))

        val postCategoryAdapter = SpinnerAdapter(this, R.layout.item_spinner, postCategoryArr)
        val postExerciseTypeAdapter = SpinnerAdapter(this, R.layout.item_spinner, postExerciseTypeArr)
        binding.spinnerPostCategory.adapter = postCategoryAdapter
        binding.spinnerPostType.adapter = postExerciseTypeAdapter

        mediaAdapter = PostWriteMediaRvAdapter(this)
        binding.rvPostMedia.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPostMedia.adapter = mediaAdapter

        // 게시글 수정인지 확인
        editing = intent.getBooleanExtra("editing", false)
        if (editing){
            postId = intent.getIntExtra("postId", -1)
            postData = intent.getSerializableExtra("postData") as PostWriteRequest
            val files = intent.getStringArrayListExtra("postFilePathList")
            if(files != null){
                postFilePathList = files
            }
            binding.spinnerPostCategory.setSelection(postCategoryArr.indexOf(postData?.postType))
            binding.spinnerPostType.setSelection(postExerciseTypeArr.indexOf(postData?.workOutCategory))
            binding.edtPostTitle.setText(postData?.title)
            binding.edtPostText.setText(postData?.content)
            binding.btnPosting.text = getString(R.string.editing)
            for(path in postFilePathList){
                val bitmap = BitmapFactory.decodeFile(path);
                //mediaAdapter.addItem(MediaRvItem(3, null, bitmap, null))
            }
            deleteMediaFile()

        }
    }

    private fun setUiEvent() {
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

        binding.ivClose.setOnClickListener { finish() }

        binding.btnPosting.setOnClickListener {//게시글 등록 또는 수정
            var category = binding.spinnerPostCategory.selectedItem.toString()
            val type = binding.spinnerPostType.selectedItem.toString()
            val title = binding.edtPostTitle.text.toString()
            val text = binding.edtPostText.text.toString()


            val jsonRequest = PostWriteRequest(
                title = title.takeIf { it != "" },
                content = text.takeIf { it != "" },
                postType = postCategoryHashMap[category].takeIf { it != null },
                workOutCategory = postExerciseTypeHashMap[type].takeIf { it != null }
            )

            val gson = GsonBuilder().serializeNulls().create()
            val requestBodyString = gson.toJson(jsonRequest).toString()
            val postData = requestBodyString.toRequestBody("application/json".toMediaType())

            // 미디어
            val mediaList = mediaAdapter.getMediaList()
            val imageList: ArrayList<MultipartBody.Part> = ArrayList()

            if(editing){ // 게시글 수정
                for(i in mediaList.indices){
                    val uri = mediaList[i].uri
                    val url = mediaList[i].url
                    var file: File = if(url != null){
                        val realPath = saveMedia(url)
                        File(realPath)
                    }else if(uri != null){
                        File(getRealPathFromUri(uri))
                    }else{return@setOnClickListener}
                    val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    val uploadFile: MultipartBody.Part = MultipartBody.Part.createFormData("files",  file.name, requestFile)
                    imageList.add(uploadFile)
                }

                viewModel.postEdit(postId, postData, imageList)

            }else{ // 게시글 작성
                for(element in mediaList) {
                    if(element.uri != null){
                        val file = File(getRealPathFromUri(element.uri))
                        val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                        val uploadFile: MultipartBody.Part = MultipartBody.Part.createFormData("files",  file.name, requestFile)
                        imageList.add(uploadFile)
                    }
                }
                viewModel.postWrite(postData, imageList)
            }
        }

    }

    private fun subscribeUI(){
        viewModel.toastMessage.observe(this){ message ->
            showCustomToast(message)
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }

        viewModel.logout.observe(this) { logout ->
            if(logout) logout()
        }

        viewModel.postWriteSuccess.observe(this){ isSuccess ->
            if(!isSuccess) return@observe

            finish()
        }
    }
    // 이미지 절대 경로 얻기 위해, 서버로 부터 받은 이미지 캐쉬에 임시 저장
    private fun saveMedia(url: String): String {
        val fileName = url.substringAfter("/")
        val tempFile = File(cacheDir, "$fileName")

        try {
            // convert url to bitmap
            val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)

            // save bitmap image into cache
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

            out.close()
        } catch (e: FileNotFoundException) {
            Log.e(DiaryActivity.TAG, "FileNotFoundException : " + e.message)
        } catch (e: IOException) {
            Log.e(DiaryActivity.TAG, "IOException : " + e.message)
        }
        postFilePathList.add(tempFile.path)
        return tempFile.absolutePath
    }

    // 임시로 저장 해놓은 이미지 삭제
    private fun deleteMediaFile(){ // 임시 미디어 삭제
        for(path in postFilePathList){
            val file = File(path)
            if(file.exists()){
                Log.d(DiaryActivity.TAG, "file name: ${path}")
                file.delete()
            }
        }

    }

    override fun finish() {
        super.finish()
        deleteMediaFile()
    }

    companion object{
        val TAG = "PostWriteActivity"
    }

}