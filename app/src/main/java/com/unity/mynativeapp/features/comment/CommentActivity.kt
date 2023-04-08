package com.unity.mynativeapp.features.comment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityCommentBinding
import com.unity.mynativeapp.model.CommentDto

class CommentActivity : BaseActivity<ActivityCommentBinding>(ActivityCommentBinding::inflate) {

    private lateinit var commentRvAdapter: ParentCommentRvAdapter // 댓글 어댑터
    private var firstStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){


        // 게시물 댓글 리사이클러뷰 (댓글 3개)
        commentRvAdapter = ParentCommentRvAdapter(this)
        binding.rvPostComment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvPostComment.adapter = commentRvAdapter
        commentRvAdapter.getListFromView(setCommentSample())

    }

    private fun setCommentSample(): MutableList<CommentDto>{
        var list = mutableListOf<CommentDto>()
        list.add(CommentDto(0, "댓글1...", "방금", R.drawable.ic_profile_photo_base, "user1"))
        list.add(CommentDto(1, "댓글2...", "방금", R.drawable.ic_profile_photo_base, "user2"))
        list.add(CommentDto(2, "댓글3...", "방금", R.drawable.ic_profile_photo_base, "user3"))
        list.add(CommentDto(0, "댓글4...", "방금", R.drawable.ic_profile_photo_base, "user4"))
        list.add(CommentDto(3, "댓글5...", "방금", R.drawable.ic_profile_photo_base, "user5"))
        list.add(CommentDto(0, "댓글6...", "방금", R.drawable.ic_profile_photo_base, "user6"))

        return list
    }

    private fun setUiEvent(){

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun subscribeUI(){

        // 댓글 없으면
        //binding.tvNoComment.visibility = View.VISIBLE

        // 댓글 있으면
        //binding.tvNoComment.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }

    }
}