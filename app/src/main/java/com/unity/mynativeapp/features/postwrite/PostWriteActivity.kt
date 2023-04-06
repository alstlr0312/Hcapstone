package com.unity.mynativeapp.features.postwrite

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityPostWriteBinding
import com.unity.mynativeapp.features.diary.DiaryMediaRvAdapter

class PostWriteActivity : BaseActivity<ActivityPostWriteBinding>(ActivityPostWriteBinding::inflate) {

    lateinit var postStyleArr: ArrayList<String>
    lateinit var postCategoryArr: ArrayList<String>
    lateinit var mediaAdapter: PostWriteMediaRvAdapter      // 미디어 Rv 어댑터

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

        postStyleArr = arrayListOf(getString(R.string.please_select_post_style),
            getString(R.string.q_and_a), getString(R.string.knowledge_sharing),
            getString(R.string.show_off), getString(R.string.assess), getString(R.string.freedom))

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


        setUiEvent()
    }

    private fun setUiEvent(){
        // 미디어 추가
        binding.btnAddMedia.setOnClickListener {
            if(mediaAdapter.itemCount == 4){
                showCustomToast(getString(R.string.you_can_register_four_medias))
            }else{
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                imageResult.launch(intent)
            }
        }
    }

    private fun setupSpinnerHandler() {
        binding.spinnerPostStyle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item = binding.spinnerPostStyle.getItemAtPosition(position) as SpinnerModel

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerPostCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item = binding.spinnerPostCategory.getItemAtPosition(position) as SpinnerModel

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }
}