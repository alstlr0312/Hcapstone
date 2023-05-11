package com.unity.mynativeapp.features.postdetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.unity.mynativeapp.MyApplication.Companion.postTypeHashMap
import com.unity.mynativeapp.MyApplication.Companion.postTypeToKoHashMap
import com.unity.mynativeapp.MyApplication.Companion.workOutCategoryHashMap
import com.unity.mynativeapp.MyApplication.Companion.workOutCategoryToKoHashMap
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityPostDetailBinding
import com.unity.mynativeapp.features.comment.CommentActivity
import com.unity.mynativeapp.features.comment.ParentCommentRvAdapter
import com.unity.mynativeapp.model.CommentDto

class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(ActivityPostDetailBinding::inflate) {

    private lateinit var mediaVpAdapter: MediaViewPagerAdapter // 게시물 미디어 어댑터
    private lateinit var commentRvAdapter: ParentCommentRvAdapter // 댓글 어댑터
    private var firstStart = true
    private val viewModel by viewModels<PostDetailViewModel>()
    private var postId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postId = intent.getIntExtra("num", -1)
        viewModel.postDetail(postId)

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
        commentRvAdapter = ParentCommentRvAdapter(this)
        binding.rvPostComment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvPostComment.adapter = commentRvAdapter
        //commentRvAdapter.getListFromView(setCommentSample())



    }

    private fun setMediaSample(): MutableList<Int>{
        var list = mutableListOf<Int>()
        list.add(R.drawable.bugi)
        list.add(R.drawable.photo01)
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
        binding.tvSeeMoreComment.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("postId", 0)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

    }

    private fun subscribeUI(){

        viewModel.logout.observe(this) {
            if(it) logout()
        }

        viewModel.postDetailData.observe(this) { data ->
            if (data != null) {

                binding.tvPostType.text = postTypeToKoHashMap[data.postType]
                binding.tvPostCatory.text = workOutCategoryToKoHashMap[data.workOutCategory]

                binding.tvUsername.text = data.username
                binding.tvPostTitle.text = data.title
                binding.tvPostContent.text = data.content

                binding.tvHeartNum.text = data.likeCount.toString()
                binding.tvCommentNum.text = "0"
                binding.tvViewsNum.text = data.views.toString()

                binding.tvPostDate.text = data.createdAt

                binding.cbHeart.isChecked = data.likePressed

                if(binding.tvCommentNum.text == "0"){
                    binding.tvComment2.visibility = View.GONE
                    binding.tvSeeMoreComment.text = getString(R.string.write_comment)
                }else{
                    binding.tvComment2.visibility = View.VISIBLE
                    binding.tvSeeMoreComment.text = getString(R.string.see_more_comment)
                }

                if(mediaVpAdapter.itemCount == 1){

                }

                val getMedia = data.mediaList
                Log.d("bodyPart", getMedia.toString())
                for (x in getMedia) {
                    val lastSegment = x.substringAfterLast("/").toInt()
                    viewModel.media(lastSegment)
                    viewModel.mediaData.observe(this) { data2 ->
                        if (data2 != null) {
                            Log.d("bodyPartsdfasd", data2.toString())
                            mediaVpAdapter.addItem(data2.bytes())
                        }
                    }
                }
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
}