package com.unity.mynativeapp.features.community


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseFragment
import com.unity.mynativeapp.databinding.FragmentCommunityBinding
import com.unity.mynativeapp.features.postwrite.PostWriteActivity
import com.unity.mynativeapp.model.PostItem


class CommunityFragment : BaseFragment<FragmentCommunityBinding>(
    FragmentCommunityBinding::bind, R.layout.fragment_community)  {

    private val viewModel by viewModels<PostViewModel>()
    private lateinit var postingRvAdapter: PostListRvAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()

        with(binding){

            // 게시글 어댑터 설정
            postingRvAdapter = PostListRvAdapter(requireContext())
            rvPosting.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvPosting.adapter = postingRvAdapter
            //postingRvAdapter.getListFromView(postListSample())


            // 게시물 필터 설정
            layoutFilter.setOnClickListener {
                var dialog = PostSortDialog(requireContext())
                dialog.show()
            }
        }

        // 게시물 작성
        this.requireActivity().findViewById<FloatingActionButton>(R.id.btn_write_post).setOnClickListener {
            startActivity(Intent(requireContext(), PostWriteActivity::class.java))
        }

    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            showCustomToast(message)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoadingDialog(requireContext()) else dismissLoadingDialog()
        }

        viewModel.logout.observe(viewLifecycleOwner){
            if(it) logout()
        }

        viewModel.postData.observe(viewLifecycleOwner) { data ->
            val getexInfo = data?.postListDto

            if (getexInfo != null) {
                for (x in getexInfo) {
                    val username = x.username
                    val title = x.title
                    val commentCount = x.commentCount
                    val createdAt = x.createdAt
                    val workOutCategory = x.workOutCategory
                    val views = x.views
                    val mediaListCount = x.mediaListCount
                    val postType = x.postType
                    val postId = x.postId
                    val likeCount = x.likeCount

                    postingRvAdapter.addItem(PostItem(username,postType,workOutCategory,createdAt,title,postId,mediaListCount,likeCount,views,commentCount))
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        postingRvAdapter.removeAllItem()
        viewModel.community()
    }

}