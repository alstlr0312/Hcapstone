package com.You.haveto.features.comment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.You.haveto.R
import com.You.haveto.config.BaseActivity
import com.You.haveto.databinding.ActivityCommentBinding
import com.You.haveto.model.CommentWriteRequest
import com.You.haveto.util.KeyboardVisibilityUtil

interface OnCommentClick{

    fun parentCommentGetListener()
    fun childCommentGetListener(parentId: Int)
    fun writeChildComment(parentId: Int)
    fun deleteParentCommentListener(commentId: Int)
    fun deleteChileCommentListener(commentId: Int, parentId: Int)

}
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
        val parentId = intent.getIntExtra("parentId", -1)
        if(parentId != -1){
            focusParentId = parentId
        }

        setView()
        setUiEvent()
        subscribeUI()

    }

    private fun setView(){

        commentRvAdapter = ParentCommentRvAdapter(this, this)
        binding.rvPostComment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvPostComment.adapter = commentRvAdapter
        commentRvAdapter.setPostId(postId)
        if(focusParentId != null){
            commentRvAdapter.setFocusId(focusParentId!!)
        }


        // 전체 댓글 조회 요청
        viewModel.commentGet(postId, null, null, null, null)

        binding.edtSendComment.requestFocus() // edt 포커스
        //showKeyBoard(binding.edtSendComment)// 키보드 올리기

    }
/*
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                val btmView = binding.layoutBottom as View
                btmView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    Log.d("focusView", "here")
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(btmView.windowToken, 0)
                }
            }
        }
        return true
    }

 */

    @SuppressLint("ClickableViewAccessibility")
    private fun setUiEvent() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        // 댓글 작성
        binding.btnSendComment.setOnClickListener{
            if(binding.edtSendComment.text.toString() != ""){
                viewModel.commentWrite(CommentWriteRequest(
                    postId,
                    binding.edtSendComment.text.toString(),
                    focusParentId
                ))
            }
        }
/*
        binding.edtSendComment.setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    if(!keyBoardIsShowing){
                        binding.edtSendComment.requestFocus()
                        showKeyBoard(binding.edtSendComment)
                    }
                    if (event.rawX >= (binding.edtSendComment.right
                                - binding.edtSendComment.compoundDrawables[2].bounds.width())
                    ) {
                        Log.d("focusView", "clicked")
                        viewModel.commentWrite(CommentWriteRequest(
                            postId,
                            binding.edtSendComment.text.toString(),
                            focusParentId
                        ))
                    }
                }
            }
            true
        }
 */

        keyboardVisibilityUtil = KeyboardVisibilityUtil(window,
            onShowKeyboard = {
                keyBoardIsShowing = true
                binding.root.run {
                    //키보드 올라왔을때 원하는 동작
                }
            },
            onHideKeyboard = {
                binding.root.run {
                    keyBoardIsShowing = false

                    //키보드 내려갔을때 원하는 동작
                    if (focusParentId != null) {
                        focusParentId = null
                        commentRvAdapter.setCommentUnFocus()
                    }
                }
            }
        )


        // 댓글 새로 고침
        binding.ivRefresh.setOnClickListener {
            viewModel.commentGet(postId, null, null, null, null) // 댓글 전체 조회
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

        // 댓글 작성 성공
        viewModel.commentWriteSuccess.observe(this){
            if(!it) return@observe

            viewModel.commentGet(postId, null, null, null, null) // 댓글 다시 전체 조회

            hideKeyBoard()
            binding.edtSendComment.setText("")

        }

        // 댓글 조회
        viewModel.commentGetData.observe(this) {data ->
            if(data != null && data.commentListDto.isNotEmpty()) {
                binding.tvNoComment.visibility = View.GONE
                if(data.parentId == null){ // 전체 댓글 조회

                    commentRvAdapter.removeAllItem()
                    for(comment in data.commentListDto){ // 모든 댓글에 대해
                        commentRvAdapter.addItem(comment)
                    }
                }else{ // 자식 댓글만 조회한 경우 (답글 보기 클릭 또는 답글 작성)
                    commentRvAdapter.setChildComments(data.parentId!!, data.commentListDto)
                }

            }else{
                binding.tvNoComment.visibility = View.VISIBLE
            }
        }

        // 부모 댓글 삭제
        viewModel.parentCommentDeleteData.observe(this) { commentId ->
            if(commentId==null){
                commentRvAdapter.setCommentUnFocus()
                return@observe
            }
            commentRvAdapter.setCommentDelete(commentId)
        }

        // 자식 댓글 삭제
        viewModel.childCommentDeleteData.observe(this) { data ->
            if(data.commentId==null){
                commentRvAdapter.setChildCommentUnFocus(data.parentId)
                return@observe
            }
            commentRvAdapter.setChildCommentDelete(data.commentId, data.parentId)
        }
    }

    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }
    }


    override fun parentCommentGetListener() {
        viewModel.commentGet(postId, null, null, null, null)
    }

    // 자식 댓글 조회 요청
    override fun childCommentGetListener(parentId: Int) {
        viewModel.commentGet(postId, parentId, null, null, null)
    }

    override fun writeChildComment(parentId: Int) {
        focusParentId = parentId
        binding.edtSendComment.requestFocus()
        showKeyBoard(binding.edtSendComment)
    }

    override fun deleteParentCommentListener(commentId: Int) {
        viewModel.commentDelete(commentId)
    }

    override fun deleteChileCommentListener(commentId: Int, parentId: Int) {
        viewModel.commentDelete(commentId, parentId)
    }

    companion object {
        const val TAG = "CommentActivityLog"
    }

}