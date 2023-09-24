package com.You.haveto.features.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.You.haveto.R
import com.You.haveto.config.BaseActivity
import com.You.haveto.databinding.ActivityMemberPageBinding
import com.You.haveto.features.media.MediaFullActivity
import com.You.haveto.model.MyPageRvItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MemberPageActivity : BaseActivity<ActivityMemberPageBinding>(ActivityMemberPageBinding::inflate) {

    private val viewModel by viewModels<MyPageViewModel>()
    private lateinit var myPageAdapter: MyPageAdapter
    private var firstStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 회원 정보 요청
        val username: String = intent.getStringExtra("username")!!
        viewModel.myPageInfo(username)

        myPageAdapter = MyPageAdapter(this, getRvItemList(), username)
        binding.rvMemberPage.adapter = myPageAdapter

        setUiEvent()
        subscribeUI()

    }

    private fun getRvItemList(): MutableList<MyPageRvItem>{
        var list = mutableListOf<MyPageRvItem>()
        list.add(MyPageRvItem(R.drawable.ic_post, getString(R.string.posting), 0))
        return list
    }

    private fun setUiEvent(){

        binding.ivClose.setOnClickListener {
            finish()
        }



    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            showCustomToast(message)
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }

        viewModel.logout.observe(this){
            if(it) logout()
        }

        viewModel.myPageData.observe(this) { data ->
            if(data==null) return@observe

            if(data.profileImage != null){
                CoroutineScope(Dispatchers.Main).launch {
                    Glide.with(binding.ivProfileImg)
                        .load(data.profileImage)
                        .placeholder(R.color.main_black)
                        .error(R.drawable.ic_profile_photo_base)
                        .apply(RequestOptions.centerCropTransform())
                        .into(binding.ivProfileImg)
                }
                // 미디어 크게 보기
                binding.ivProfileImg.setOnClickListener {
                    val intent = Intent(this, MediaFullActivity::class.java)
                    intent.putExtra("url", data.profileImage)
                    intent.putExtra("viewType", 3) // url 사진
                    startActivity(intent)
                }
            }else{
                binding.ivProfileImg.setImageResource(R.drawable.ic_profile_photo_base)
            }

            binding.tvUsername.text = data.username

            if(data.field != null){
                binding.layoutField.visibility = View.VISIBLE
                binding.tvField.text = data.field
            }else{
                binding.layoutField.visibility = View.GONE
            }

            val date = data.createdAt
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val dateTime: LocalDateTime = LocalDateTime.parse(date, formatter)
            val formatDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일에 가입함"))
            binding.tvStartDate.text = formatDate
            binding.layoutStartDate.visibility = View.VISIBLE

            // 게시글 개수
            myPageAdapter.setPostCounts(data.postCount)

        }
    }

    override fun onResume() {
        super.onResume()
        if(!firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right)
        }
        firstStart = false
    }
}