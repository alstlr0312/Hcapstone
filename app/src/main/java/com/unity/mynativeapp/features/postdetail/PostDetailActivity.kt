package com.unity.mynativeapp.features.postdetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.unity.mynativeapp.MyApplication.Companion.postTypeToKoHashMap
import com.unity.mynativeapp.MyApplication.Companion.workOutCategoryToKoHashMap
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityPostDetailBinding
import com.unity.mynativeapp.features.comment.CommentActivity
import com.unity.mynativeapp.features.comment.ParentCommentRvAdapter
import com.unity.mynativeapp.model.CommentData
import com.unity.mynativeapp.model.OnCommentClick


class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(ActivityPostDetailBinding::inflate), OnCommentClick {

    private lateinit var mediaVpAdapter: MediaViewPagerAdapter // 게시물 미디어 어댑터
    private lateinit var commentRvAdapter: ParentCommentRvAdapter // 댓글 어댑터
    private var firstStart = true
    private val viewModel by viewModels<PostDetailViewModel>()
    private var postId = -1
    private var postLikeCnt = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postId = intent.getIntExtra("num", -1)
        viewModel.getPostDetail(postId)

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
        //mediaVpAdapter.getListFromView(setMediaSample())

        // 뷰페이저 인디케이터
        TabLayoutMediator(binding.tabLayout, binding.vpPostMedia){ tab, pos ->

        }.attach()


        // 게시물 댓글 리사이클러뷰
        commentRvAdapter = ParentCommentRvAdapter(this,this)
        binding.rvPostComment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvPostComment.adapter = commentRvAdapter
        commentRvAdapter.setPostId(postId)


    }

    private fun setUiEvent() {

        // 댓글 조회 & 작성 액티비티로 이동
        binding.tvSeeMoreComment.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("postId", postId)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.cbLike.setOnClickListener {
            if (it is CheckBox) {
                viewModel.like(postId, !(it.isChecked))
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

        // 게시글 조회 성공
        viewModel.postDetailData.observe(this) { data ->
            if (data != null) {

                binding.tvPostType.text = postTypeToKoHashMap[data.postType]
                binding.tvPostCatory.text = workOutCategoryToKoHashMap[data.workOutCategory]

                binding.tvUsername.text = data.username
                binding.tvPostTitle.text = data.title
                binding.tvPostContent.text = data.content

                postLikeCnt = data.likeCount
                binding.tvLikeCnt.text = postLikeCnt.toString()
                binding.tvCommentCnt.text = data.comments.size.toString()
                binding.tvViewsCnt.text = data.views.toString()

                binding.tvPostDate.text = data.createdAt

                binding.cbLike.isChecked = data.likePressed

                if(data.comments.isEmpty()){
                    binding.tvComment2.visibility = View.GONE
                    binding.tvSeeMoreComment.text = getString(R.string.write_comment)
                }else if(data.comments.size <= 3){
                    binding.tvComment2.visibility = View.VISIBLE
                    binding.tvSeeMoreComment.text = getString(R.string.write_comment)
                }else { // > 3
                    binding.tvComment2.visibility = View.VISIBLE
                    binding.tvSeeMoreComment.text = getString(R.string.see_more_comment)
                }

                val getMedia = data.mediaList
                Log.d("bodyPart", getMedia.toString())
                for (x in getMedia) {
                    val lastSegment = x.substringAfterLast("/").toInt()
                    viewModel.media(lastSegment)

                }

                // 댓글
                if(data.comments.isNotEmpty()){
                    commentRvAdapter.getListFromView(data.comments as MutableList<CommentData>)
                }

            }
        }

        viewModel.mediaData.observe(this) { data2 ->
            if (data2 != null) {
                Log.d("bodyPartsdfasd", data2.toString())
                mediaVpAdapter.addItem(data2.bytes())
            }
        }

        // 좋아요
        viewModel.likePressed.observe(this){
            if(it){
                if(binding.cbLike.isChecked){
                    postLikeCnt++
                }else{
                    postLikeCnt--
                }
                binding.tvLikeCnt.text = postLikeCnt.toString()
            }
        }

        // 댓글 조회
        viewModel.commentGetData.observe(this) {data->
            if(data != null && data.commentListDto.isNotEmpty()) {

                commentRvAdapter.setChildComments(data.parentId!!, data.commentListDto)
            }
        }
    }



    override fun onResume() {
        super.onResume()
        if(!firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right)
        }
        firstStart = false
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.getPostDetail(postId)
    }

    override fun childCommentGetListener(parentId: Int) {
        viewModel.commentGet(postId, parentId, null, null, null)
    }

    override fun writeChildComment(parentId: Int) {

    }
}