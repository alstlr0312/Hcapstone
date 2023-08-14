package com.unity.mynativeapp.features.comment

import android.app.Service
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityCommentBinding
import com.unity.mynativeapp.model.CommentData
import com.unity.mynativeapp.model.CommentWriteRequest
import com.unity.mynativeapp.model.OnCommentClick
import com.unity.mynativeapp.util.KeyboardVisibilityUtil


class CommentActivity : BaseActivity<ActivityCommentBinding>(ActivityCommentBinding::inflate),
    OnCommentClick {

    private lateinit var commentRvAdapter: ParentCommentRvAdapter // 댓글 어댑터
    private var firstStart = true
    private val viewModel by viewModels<CommentViewModel>()
    private var postId = -1
    private var focusParentId: Int? = null
    lateinit var keyboardVisibilityUtil: KeyboardVisibilityUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postId = intent.getIntExtra("postId", -1)
        setView()
        setUiEvent()
        subscribeUI()

    }

    private fun setView(){

        // 게시물 댓글 리사이클러뷰 (댓글 3개)
        commentRvAdapter = ParentCommentRvAdapter(this, this)
        binding.rvPostComment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvPostComment.adapter = commentRvAdapter
        commentRvAdapter.setPostId(postId)

        // 댓글 조회 요청
        viewModel.commentGet(postId, null, null, null, null)

        binding.edtAddComment.requestFocus()
        showKeyBoard(binding.edtAddComment)

    }


    private fun setUiEvent(){

        binding.ivBack.setOnClickListener {
            finish()
        }

        // 댓글 추가
        binding.btnSendComment.setOnClickListener {
            if(binding.edtAddComment.text.toString() != ""){

                viewModel.commentWrite(CommentWriteRequest(
                    postId,
                    binding.edtAddComment.text.toString(),
                    focusParentId
                ))

            }
        }

        keyboardVisibilityUtil = KeyboardVisibilityUtil(window,
            onShowKeyboard = {
                binding.root.run {
                    //키보드 올라왔을때 원하는 동작
                }
            },
            onHideKeyboard = {
                binding.root.run {
                    //키보드 내려갔을때 원하는 동작
                    if(focusParentId != null){
                        focusParentId = null
                        commentRvAdapter.setUnFocusComment()
                    }

                }
            }
        )
    }

    private fun subscribeUI(){

        viewModel.loading.observe(this){isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }
        viewModel.logout.observe(this) {
            if(it) logout()
        }

        viewModel.toastMessage.observe(this){
            showCustomToast(it)
        }

        // 댓글 작성 성공
        viewModel.commentWriteSuccess.observe(this){
            if(!it) return@observe

            viewModel.commentGet(postId, focusParentId, null, null, null) // 댓글 다시 전체 조회

            hideKeyBoard()
            binding.edtAddComment.setText("")

        }

        // 댓글 조회
        viewModel.commentGetData.observe(this) {data ->
            if(data != null && data.commentListDto.isNotEmpty()) {
                binding.tvNoComment.visibility = View.GONE

                //
                if(data.parentId == null){
                    commentRvAdapter.removeAllItem()
                    for(comment in data.commentListDto){ // 모든 댓글에 대해
                        if(comment.childCount != -1){ // 부모 댓글이라면
                            commentRvAdapter.addItem(comment)
                        }
                    }
                }else{ // 자식 댓글만 조회한 경우
                    commentRvAdapter.setChildComments(data.parentId!!, data.commentListDto)
                }

            }else{
                binding.tvNoComment.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }
    }



    override fun childCommentGetListener(parentId: Int) { // 자식 댓글 조회 요청
        viewModel.commentGet(postId, parentId, null, null, null)
    }

    override fun writeChildComment(parentId: Int) {
        focusParentId = parentId
        binding.edtAddComment.requestFocus()

        showKeyBoard(binding.edtAddComment)
    }


}