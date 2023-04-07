package com.unity.mynativeapp.features.postdetail

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityPostDetailBinding
import com.unity.mynativeapp.features.comment.CommentActivity
import com.unity.mynativeapp.model.CommentDto

class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(ActivityPostDetailBinding::inflate) {

    private lateinit var mediaVpAdapter: MediaViewPagerAdapter // 게시물 미디어 어댑터
    //private lateinit var commentRvAdapter: CommentRvAdapter // 댓글 어댑터
    var firstStart = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){
        // 게시물 미디어 뷰페이저
        mediaVpAdapter = MediaViewPagerAdapter(this)
        binding.vpPostMedia.adapter = mediaVpAdapter
        binding.vpPostMedia.offscreenPageLimit = 1
        binding.vpPostMedia.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpPostMedia.setCurrentItem(0, false)
        mediaVpAdapter.getListFromView(setMediaSample())

        // 뷰페이저 인디케이터
        TabLayoutMediator(binding.tabLayout, binding.vpPostMedia){ tab, pos ->

        }.attach()


        /*
        // 게시물 댓글 리사이클러뷰
        commentRvAdapter = CommentRvAdapter(this)
        binding.rvPostComment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvPostComment.adapter = commentRvAdapter
        commentRvAdapter.getListFromView(setCommentSample())
        */
    }

    private fun setMediaSample(): MutableList<Int>{
        var list = mutableListOf<Int>()
        list.add(R.drawable.bugi)
        list.add(R.drawable.bugi)
        list.add(R.drawable.bugi)
        list.add(R.drawable.bugi)
        return list
    }

    private fun setCommentSample(): MutableList<CommentDto>{
        var list = mutableListOf<CommentDto>()
        list.add(CommentDto(0, "댓글1...", "방금", R.drawable.ic_profile_photo_base, "user1"))
        list.add(CommentDto(1, "댓글2...", "방금", R.drawable.ic_profile_photo_base, "user2"))
        list.add(CommentDto(2, "댓글3...", "방금", R.drawable.ic_profile_photo_base, "user3"))

        return list
    }

    private fun setUiEvent(){

        // 댓글 조회 & 작성 액티비티로 이동
        binding.tvWriteComment.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("postId", 0)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

    }

    private fun subscribeUI(){

    }

    override fun onResume() {
        super.onResume()
        if(!firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right)
        }
        firstStart = false
    }
}