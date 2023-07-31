package com.unity.mynativeapp.features.mypage.myposts

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityMyPostsBinding
import com.unity.mynativeapp.model.PostItem

class MyPostsActivity : BaseActivity<ActivityMyPostsBinding>(ActivityMyPostsBinding::inflate) {

    private var firstStart = true
    private val viewModel by viewModels<MyPostViewModel>()
    private lateinit var myPostAdpater: MyPostRvAdapter
    private var getPostHasNext = false
    private var getPostIsFirst = true
    private var currentPage = 0
    private val pageSize = 20
    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){
        // 게시글 어댑터 설정
        myPostAdpater = MyPostRvAdapter(this)
        binding.rvMyPost.adapter = myPostAdpater

        // 유저 이름
        username = MyApplication.prefUtil.getString("username", "").toString()
        if(username.isNotEmpty()){
            // 내 게시물 목록 요청
            viewModel.myPost(null, null, username, currentPage, pageSize)
        }
    }

    private fun setUiEvent(){
        binding.ivBack.setOnClickListener {
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

        viewModel.postData.observe(this) { data ->

            if(data != null){
                myPostAdpater.removeAllItem()

                val getPostInfo = data.postListDto

                if(getPostInfo.isEmpty()){
                    binding.emptyView.visibility = View.VISIBLE
                    binding.rvMyPost.visibility = View.GONE
                }else{
                    binding.emptyView.visibility = View.INVISIBLE
                    binding.rvMyPost.visibility = View.VISIBLE
                }

                for (post in getPostInfo) {
                    myPostAdpater.addItem(
                        PostItem(
                            username = post.username,
                            postType = post.postType,
                            workOutCategory = post.workOutCategory,
                            createdAt = post.createdAt,
                            title = post.title,
                            postId = post.postId,
                            mediaListCount = post.mediaListCount,
                            likeCount = post.likeCount,
                            views = post.views,
                            commentCount = post.commentCount,
                        )
                    )
                }

                getPostHasNext = data.hasNext
                getPostIsFirst = data.isFirst
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.myPost(null, null, username, currentPage, pageSize)
    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }
    }
}