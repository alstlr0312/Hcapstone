package com.unity.mynativeapp.features.mypage.mycomments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityMyCommentsBinding
import com.unity.mynativeapp.features.comment.ChildCommentRvAdapter
import com.unity.mynativeapp.features.comment.CommentViewModel
import com.unity.mynativeapp.features.comment.OnCommentClick
import com.unity.mynativeapp.features.comment.ParentCommentRvAdapter
import com.unity.mynativeapp.model.PostItem

class MyCommentsActivity : BaseActivity<ActivityMyCommentsBinding>(ActivityMyCommentsBinding::inflate), OnCommentClick {

    private var firstStart = true
    private val viewModel by viewModels<CommentViewModel>()
    private lateinit var myParentCommentAdapter: ParentCommentRvAdapter
    private lateinit var myChildCommentAdapter: ChildCommentRvAdapter
    private var getPostHasNext = false
    private var getPostIsFirst = true
    private var currentPage = 0
    private val pageSize = 30
    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){

        // 댓글 어댑터 설정
        myParentCommentAdapter = ParentCommentRvAdapter(this, this)
        binding.rvMyParentComment.adapter = myParentCommentAdapter
        myChildCommentAdapter = ChildCommentRvAdapter(this, this)
        binding.rvMyChildComment.adapter = myChildCommentAdapter

        // 유저 이름
        username = MyApplication.prefUtil.getString("username", "").toString()

        // 내 댓글 목록 요청
        viewModel.commentGet(null, null, username, null, null)

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

        viewModel.commentGetData.observe(this) {data ->
            if(data != null && data.commentListDto.isNotEmpty()) {
                binding.emptyView.visibility = View.GONE
                if(data.parentId == null){ // 전체 댓글 조회

                    myParentCommentAdapter.removeAllItem()
                    myChildCommentAdapter.removeAllItem()
                    binding.layoutParentComment.visibility = View.GONE
                    binding.layoutChildComment.visibility = View.GONE
                    for(comment in data.commentListDto){ // 모든 댓글에 대해
                        if(comment.childCount != -1){
                            binding.layoutParentComment.visibility = View.VISIBLE
                            myParentCommentAdapter.addItem(comment)
                        }else{
                            binding.layoutChildComment.visibility = View.VISIBLE
                            myChildCommentAdapter.addItem(comment)
                        }
                    }
                }else{ // 자식 댓글만 조회한 경우 (답글 보기 클릭)
                    myParentCommentAdapter.setChildComments(data.parentId!!, data.commentListDto)
                }
                getPostHasNext = data.hasNext
                getPostIsFirst = data.isFirst
            }else{
                binding.emptyView.visibility = View.VISIBLE
            }
        }

        viewModel.parentCommentDeleteData.observe(this) { data ->
            if(data==null)return@observe
            viewModel.commentGet(null, null, username, null,null)
        }

    }

    override fun onRestart() {
        super.onRestart()
        viewModel.commentGet(null, null, username, null, null)
    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }
    }

    override fun parentCommentGetListener() {
        TODO("Not yet implemented")
    }

    override fun childCommentGetListener(parentId: Int) {
        viewModel.commentGet(null, parentId, username, null, null)
    }

    override fun writeChildComment(parentId: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteParentCommentListener(commentId: Int) {
        viewModel.commentDelete(commentId)
    }

    override fun deleteChileCommentListener(commentId: Int, parentId: Int) {
        viewModel.commentDelete(commentId)


    }
}