package com.unity.mynativeapp.features.postwrite

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityPostWriteBinding
import com.unity.mynativeapp.features.diary.DiaryMediaRvAdapter
import com.unity.mynativeapp.model.DiaryWriteRequest
import com.unity.mynativeapp.model.PostWriteRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class PostWriteActivity : BaseActivity<ActivityPostWriteBinding>(ActivityPostWriteBinding::inflate) {

    lateinit var postStyleArr: ArrayList<String>
    lateinit var postCategoryArr: ArrayList<String>
    lateinit var mediaAdapter: PostWriteMediaRvAdapter      // 미디어 Rv 어댑터

    var imageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
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

    private fun setView() {

        postStyleArr = arrayListOf(
            getString(R.string.please_select_post_style),
            getString(R.string.q_and_a), getString(R.string.knowledge_sharing),
            getString(R.string.show_off), getString(R.string.assess), getString(R.string.freedom)
        )

        postCategoryArr = arrayListOf(
            getString(R.string.please_select_post_category),
            getString(R.string.health), getString(R.string.pilates), getString(R.string.yoga),
            getString(R.string.jogging), getString(R.string.etc)
        )


        val postStyleArrayAdapter = SpinnerAdapter(this, R.layout.item_spinner, postStyleArr)
        val postCategoryArrayAdapter = SpinnerAdapter(this, R.layout.item_spinner, postCategoryArr)
        binding.spinnerPostStyle.adapter = postStyleArrayAdapter
        binding.spinnerPostCategory.adapter = postCategoryArrayAdapter

        mediaAdapter = PostWriteMediaRvAdapter(this)
        binding.rvPostMedia.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
            val sstyle = binding.spinnerPostStyle.selectedItem.toString()
            val scat = binding.spinnerPostCategory.selectedItem.toString()
            val postitle = binding.edtPostTitle.toString()
            val posttext = binding.edtPostText.toString()

            val jsonRequest = PostWriteRequest(
                postitle,
                posttext,
                sstyle,
                scat
            )
            //    val jsonBody = RequestBody.create(parse("application/json"),jsonObject2)

            val gson = GsonBuilder().serializeNulls().create()
            val jsonObject = JSONObject().put("writeDiaryDto", gson.toJson(jsonRequest))
            Log.d("diaryActivity", jsonObject.toString())
            val jsonObject11 = JSONObject(jsonObject.toString())

            val requestBodyString = gson.toJson(jsonRequest).toString()
            val requestBodyWithoutBackslashes = requestBodyString.replace("\\", "")
            val exdata = createPartFromString(requestBodyWithoutBackslashes)


            // 미디어
            val imageList: ArrayList<MultipartBody.Part> = ArrayList()
            for (element in mediaAdapter.getMediaList()) {
                val file = File(element)
                val requestFile: RequestBody =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                val uploadFile: MultipartBody.Part =
                    MultipartBody.Part.createFormData("files", file.name, requestFile)
                imageList.add(uploadFile)
                Log.d("234234234234", element)
            }

            //viewModel.diaryWrite(exdata, imageList)
            Log.d("diaryActivity111111", jsonObject11.toString())
        }

    }
    fun createPartFromString(stringData: String): RequestBody {
        return stringData.toRequestBody("application/json".toMediaTypeOrNull())
    }


    private fun subscribeUI(){

    }

}