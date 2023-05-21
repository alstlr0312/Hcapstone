package com.unity.mynativeapp.features.comment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityCommentBinding
import com.unity.mynativeapp.model.CommentData
import com.unity.mynativeapp.model.CommentWriteRequest

class CommentActivity : BaseActivity<ActivityCommentBinding>(ActivityCommentBinding::inflate) {

    private lateinit var commentRvAdapter: ParentCommentRvAdapter // 댓글 어댑터
    private var firstStart = true
    private val viewModel by viewModels<CommentViewModel>()
    private var postId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postId = intent.getIntExtra("postId", -1)
        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){


        // 게시물 댓글 리사이클러뷰 (댓글 3개)
        commentRvAdapter = ParentCommentRvAdapter(this)
        binding.rvPostComment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvPostComment.adapter = commentRvAdapter
        //commentRvAdapter.getListFromView(setCommentSample())

    }

//    private fun setCommentSample(): MutableList<CommentDto>{
//        var list = mutableListOf<CommentDto>()
//        list.add(CommentDto(0, "댓글1...", "방금", R.drawable.ic_profile_photo_base, "user1"))
//        list.add(CommentDto(1, "댓글2...", "방금", R.drawable.ic_profile_photo_base, "user2"))
//        list.add(CommentDto(2, "댓글3...", "방금", R.drawable.ic_profile_photo_base, "user3"))
//        list.add(CommentDto(0, "댓글4...", "방금", R.drawable.ic_profile_photo_base, "user4"))
//        list.add(CommentDto(3, "댓글5...", "방금", R.drawable.ic_profile_photo_base, "user5"))
//        list.add(CommentDto(0, "댓글6...", "방금", R.drawable.ic_profile_photo_base, "user6"))
//
//        return list
//    }

    private fun setUiEvent(){

        binding.ivBack.setOnClickListener {
            finish()
        }

        // 부모 댓글 추가
        binding.btnSendComment.setOnClickListener {
            if(binding.edtAddComment.text.toString() != ""){
                viewModel.commentWrite(CommentWriteRequest(
                    postId,
                    binding.edtAddComment.text.toString(),
                    parentId = null
                ))
            }
        }


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

        viewModel.commentWriteSuccess.observe(this){
            if(!it) return@observe

            //viewModel.commentGet(postId, null, null, null) // 댓글 다시 전체 조회

            hideKeyboad()
            binding.edtAddComment.setText("")

        }

        // 댓글 조회
        viewModel.commentGetData.observe(this) {data->
            if(data != null && data.commentListDto.isNotEmpty()) {
                binding.tvNoComment.visibility = View.GONE

                commentRvAdapter.removeAllItem()

                for(comment in data.commentListDto){
                    commentRvAdapter.addItem(comment)
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

    override fun onRestart() {
        super.onRestart()
    }
}