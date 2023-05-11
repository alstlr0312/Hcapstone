package com.unity.mynativeapp.features.postwrite

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.MyApplication.Companion.postTypeHashMap
import com.unity.mynativeapp.MyApplication.Companion.workOutCategoryHashMap
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityPostWriteBinding
import com.unity.mynativeapp.model.PostWriteRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PostWriteActivity : BaseActivity<ActivityPostWriteBinding>(ActivityPostWriteBinding::inflate) {

    lateinit var postStyleArr: ArrayList<String>
    lateinit var postCategoryArr: ArrayList<String>
    lateinit var mediaAdapter: PostWriteMediaRvAdapter      // 미디어 Rv 어댑터
    private val viewModel by viewModels<PostWriteViewModel>()

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

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){

        postStyleArr = arrayListOf(getString(R.string.please_select_post_style),
            getString(R.string.q_and_a), getString(R.string.knowledge_sharing),
            getString(R.string.show_off), getString(R.string.assess), getString(R.string.free))

        postCategoryArr = arrayListOf(getString(R.string.please_select_post_category),
            getString(R.string.health), getString(R.string.pilates), getString(R.string.yoga),
            getString(R.string.jogging), getString(R.string.etc))


        val postStyleArrayAdapter = SpinnerAdapter(this, R.layout.item_spinner, postStyleArr)
        val postCategoryArrayAdapter = SpinnerAdapter(this, R.layout.item_spinner, postCategoryArr)
        binding.spinnerPostStyle.adapter = postStyleArrayAdapter
        binding.spinnerPostCategory.adapter = postCategoryArrayAdapter

        mediaAdapter = PostWriteMediaRvAdapter(this)
        binding.rvPostMedia.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPostMedia.adapter = mediaAdapter
    }

    private fun setUiEvent() {
        // 미디어 추가
        binding.btnAddMedia.setOnClickListener {
            if (mediaAdapter.itemCount == 4) {
                showCustomToast(getString(R.string.you_can_register_four_medias))
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                imageResult.launch(intent)
            }
        }

        binding.btnPosting.setOnClickListener {//게시글 등록
            var type = binding.spinnerPostStyle.selectedItem.toString()
            val category = binding.spinnerPostCategory.selectedItem.toString()
            val title = binding.edtPostTitle.text.toString()
            val text = binding.edtPostText.text.toString()


            val jsonRequest = PostWriteRequest(
                title,
                text,
                postTypeHashMap[type].toString(),
                workOutCategoryHashMap[category].toString()
            )

            val gson = GsonBuilder().serializeNulls().create()
            val requestBodyString = gson.toJson(jsonRequest).toString()
            val requestBodyWithoutBackslashes = requestBodyString.replace("\\", "")
            val postData = createPartFromString(requestBodyWithoutBackslashes)


            // 미디어
            val imageList: ArrayList<MultipartBody.Part> = ArrayList()
            for (element in mediaAdapter.getMediaList()) {
                val file = File(element)
                val requestFile: RequestBody =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                val uploadFile: MultipartBody.Part =
                    MultipartBody.Part.createFormData("files", file.name, requestFile)
                imageList.add(uploadFile)
            }

            viewModel.postWrite(postData, imageList)
        }

    }
    fun createPartFromString(stringData: String): RequestBody {
        return stringData.toRequestBody("application/json".toMediaTypeOrNull())
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

}