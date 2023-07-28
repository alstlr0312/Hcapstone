package com.unity.mynativeapp.features.postdetail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.unity.mynativeapp.MyApplication.Companion.postCategoryKorHashMap
import com.unity.mynativeapp.MyApplication.Companion.postExerciseTypeKorHashMap
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityPostDetailBinding
import com.unity.mynativeapp.features.comment.CommentActivity
import com.unity.mynativeapp.features.comment.ParentCommentRvAdapter
import com.unity.mynativeapp.features.diary.DiaryActivity
import com.unity.mynativeapp.features.postwrite.PostWriteActivity
import com.unity.mynativeapp.model.CommentData
import com.unity.mynativeapp.model.OnCommentClick
import com.unity.mynativeapp.model.PostWriteRequest
import com.unity.mynativeapp.network.util.SimpleDialog
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(ActivityPostDetailBinding::inflate),
    OnCommentClick {

    private lateinit var mediaVpAdapter: MediaViewPagerAdapter // 게시물 미디어 어댑터
    private lateinit var commentRvAdapter: ParentCommentRvAdapter // 댓글 어댑터
    private var firstStart = true
    private val viewModel by viewModels<PostDetailViewModel>()
    private var postId = -1
    private var postLikeCnt = -1
    private var mine = false
    private lateinit var postData: PostWriteRequest
    private var tempMediaPathArr = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postId = intent.getIntExtra("postId", -1)
        if(postId != -1) viewModel.getPostDetail(postId)

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

        // 뷰페이저 인디케이터
        TabLayoutMediator(binding.tabLayout, binding.vpPostMedia){ tab, pos ->

        }.attach()


        // 게시물 댓글 리사이클러뷰
        commentRvAdapter = ParentCommentRvAdapter(this,this)
        binding.rvPostComment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvPostComment.adapter = commentRvAdapter
        commentRvAdapter.setPostId(postId)

        binding.layoutPostView.visibility = View.INVISIBLE
    }

    private fun setUiEvent() {

        // 댓글 조회 & 작성 화면으로 이동
        binding.tvSeeMoreComment.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("postId", postId) // 게시글 id
            startActivity(intent)
        }

        // 뒤로가기 버튼
        binding.ivBack.setOnClickListener {
            finish()
        }

        // 좋아요 체크박스
        binding.cbLike.setOnClickListener {
            if (it is CheckBox) {
                viewModel.like(postId, !(it.isChecked)) // 좋아요 요청
            }

        }
        // 메뉴 설정
        binding.ivMenu.setOnClickListener {
            registerForContextMenu(it)
            openContextMenu(it)
            unregisterForContextMenu(it)
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

                mine = data.mine

                // 게시글 카테고리 이름 설정
                binding.tvPostType.text = postCategoryKorHashMap[data.postType]
                binding.tvPostCatory.text = postExerciseTypeKorHashMap[data.workOutCategory]

                // 게시글 유저이름, 타이틀, 내용 설정
                binding.tvUsername.text = data.username
                binding.tvPostTitle.text = data.title
                binding.tvPostContent.text = data.content

                // 좋아요 수, 댓글 수, 조회수 설정
                postLikeCnt = data.likeCount
                binding.tvLikeCnt.text = postLikeCnt.toString()
                binding.tvCommentCnt.text = data.comments.size.toString()
                binding.tvViewsCnt.text = data.views.toString()

                // 게시 날짜 설정
                binding.tvPostDate.text = data.createdAt

                // 좋아요 체크박스 상태 설정
                binding.cbLike.isChecked = data.likePressed

                // 하단의 댓글 버튼 설정
                if(data.comments.isEmpty()){ // 댓글 없으면
                    binding.tvComment2.visibility = View.GONE
                    binding.tvSeeMoreComment.text = getString(R.string.write_comment)
                }else if(data.comments.size <= 3){ // 댓글이 3개 이하이면
                    binding.tvComment2.visibility = View.VISIBLE
                    binding.tvSeeMoreComment.text = getString(R.string.write_comment)
                }else { // 댓글이 3개 이상이면
                    binding.tvComment2.visibility = View.VISIBLE
                    binding.tvSeeMoreComment.text = getString(R.string.see_more_comment)
                }

                val getMedia = data.mediaList
                mediaVpAdapter.removeAllItem()
                for (x in getMedia) {
                    val lastSegment = x.substringAfterLast("/").toInt()
                    viewModel.media(lastSegment) // 미디어 요청
                }
                // 댓글 있으면 화면에 보이기
                if(data.comments.isNotEmpty()){
                    commentRvAdapter.getListFromView(data.comments as MutableList<CommentData>)
                }
                // 게시글 뷰 보이기
                binding.layoutPostView.visibility = View.VISIBLE
                binding.tvNoExistPost.visibility = View.INVISIBLE
            }else{
                // 존재하지 않는 게시글
                binding.layoutPostView.visibility = View.INVISIBLE
                binding.tvNoExistPost.visibility = View.VISIBLE
            }

            // 게시글 수정할 때 필요한 데이터 저장
            postData = PostWriteRequest(
                data?.title,
                data?.content,
                postCategoryKorHashMap[data?.postType],
                postExerciseTypeKorHashMap[data?.workOutCategory]
            )


        }

        // 미디어 조회
        viewModel.mediaData.observe(this) { data ->
            if (data != null) {
                val byteArray = data.bytes()
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                mediaVpAdapter.addItem(bitmap)
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

        viewModel.postDeleteSuccess.observe(this){
            if(it){ // 삭제 성공
                finish()
            }else{ // 삭제 실패
                finish()
            }
        }

        /*
        // 자식 댓글 조회
        viewModel.commentGetData.observe(this) {data->
            if(data != null && data.commentListDto.isNotEmpty()) {
                commentRvAdapter.setChildComments(data.parentId!!, data.commentListDto)
            }
        }
        */

    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }else{
            overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right)
        }

    }

    override fun onRestart() {
        super.onRestart()
        viewModel.getPostDetail(postId)
    }

    override fun childCommentGetListener(parentId: Int) {
        //viewModel.commentGet(postId, parentId, null, null, null)
    }

    override fun writeChildComment(parentId: Int) {

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //val inflater: MenuInflater = menuInflater
        if(postId != -1){
            if(mine){ // 내 게시글인 경우의 메뉴
                menuInflater.inflate(R.menu.menu_post_mine, menu)
            }else{ // 타인의 게시글인 경우의 메뉴
                menuInflater.inflate(R.menu.menu_post_other, menu)
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menuEditPost ->{ // 내 게시글 수정
                val intent = Intent(this, PostWriteActivity::class.java)
                intent.putExtra("editing", true)
                intent.putExtra("postId", postId)
                intent.putExtra("postData", postData)
                if(mediaVpAdapter.itemCount > 0) {
                    val bitmapList = mediaVpAdapter.getMediaList()
                    tempMediaPathArr = arrayListOf()
                    for(i in bitmapList.indices){
                        val realPath = saveMedia(bitmapList[i], "temp$i")
                    }
                    intent.putExtra("postFilePathList", tempMediaPathArr)
                }
                startActivity(intent)
            }
            R.id.menuDeletePost -> { // 내 게시글 삭제
                // alert dialog
                var dialog = SimpleDialog(this, getString(R.string.you_want_delete_post))
                dialog.show()

                var btnYes = dialog.findViewById<TextView>(R.id.btn_yes)
                var btnNo = dialog.findViewById<TextView>(R.id.btn_no)

                btnYes.setOnClickListener {
                    // 게시글 삭제 요청
                    viewModel.postDelete(postId)
                }
                btnNo.setOnClickListener {
                    dialog.dismiss()
                }
            }
            R.id.menuAccusePost -> { // 타인의 게시글 신고
                // 신고 이유 menu 선택
            }
            R.id.menuSendMsg -> { // 게시글 주인에게 메시지 보내기

            }
            R.id.menuRefresh -> { // 게시글 새로고침
                viewModel.getPostDetail(postId)
            }
        }
        return super.onContextItemSelected(item)
    }

    // 이미지 절대 경로 얻기 위해, 서버로 부터 받은 이미지 캐쉬에 임시 저장
    private fun saveMedia(bitmap: Bitmap, name: String): String {
        val tempFile = File(cacheDir, "$name.jpg")

        try {
            tempFile.createNewFile()

            val out = FileOutputStream(tempFile)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

            out.close()
        } catch (e: FileNotFoundException) {
            Log.e(DiaryActivity.TAG, "FileNotFoundException : " + e.message)
        } catch (e: IOException) {
            Log.e(DiaryActivity.TAG, "IOException : " + e.message)
        }
        tempMediaPathArr.add(tempFile.path)
        return tempFile.absolutePath
    }

}