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
import com.unity.mynativeapp.MyApplication.Companion.postTypeHashMap
import com.unity.mynativeapp.MyApplication.Companion.workOutCategoryHashMap
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseFragment
import com.unity.mynativeapp.databinding.FragmentCommunityBinding
import com.unity.mynativeapp.features.postwrite.PostWriteActivity
import com.unity.mynativeapp.model.PostItem


class CommunityFragment : BaseFragment<FragmentCommunityBinding>(
    FragmentCommunityBinding::bind, R.layout.fragment_community)  {

    private val viewModel by viewModels<PostViewModel>()
    private lateinit var postingRvAdapter: PostListRvAdapter
    private var postType: String? = null
    private var workOutCategory: String? = null
    private var getPostIsFirst = true
    private var getPostHasNext = false
    private var currentPage = 0
    private val pageSize = 20

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()

        with(binding){

            // 게시글 어댑터 설정
            postingRvAdapter = PostListRvAdapter(requireContext())
            rvPosting.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvPosting.adapter = postingRvAdapter

            binding.tvFilter.text = getString(R.string.total)

            // 게시물 필터 설정
            layoutFilter.setOnClickListener {
                var dialog = PostSortDialog(requireContext())
                dialog.show()

                dialog.setOnDismissListener {
                    if(dialog.resultCode == 1){
                        var btnStr = ""

                        if(dialog.checkedSortText != getString(R.string.total) && dialog.checkedSortText != null){
                            btnStr = btnStr + dialog.checkedSortText + " > "
                            postType = postTypeHashMap[dialog.checkedSortText]
                        }else{
                            postType = null
                        }

                        if(dialog.checkedCateText != getString(R.string.total) && dialog.checkedCateText != null){
                            btnStr += dialog.checkedCateText
                            workOutCategory = workOutCategoryHashMap[dialog.checkedCateText]
                        }else{
                            btnStr = btnStr.split(" > ")[0]
                            workOutCategory = null
                        }

                        if(dialog.checkedSortText == getString(R.string.total) && dialog.checkedCateText == getString(R.string.total)){
                            btnStr = getString(R.string.total)
                        }

                        binding.tvFilter.text = btnStr
                        viewModel.community(postType, workOutCategory, currentPage, pageSize)
                    }
                }
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

            if(data != null){
                postingRvAdapter.removeAllItem()

                val getExInfo = data.postListDto

                if(getExInfo.isEmpty()){
                    binding.emptyView.visibility = View.VISIBLE
                    binding.rvPosting.visibility = View.GONE
                }else{
                    binding.emptyView.visibility = View.INVISIBLE
                    binding.rvPosting.visibility = View.VISIBLE
                }

                for (x in getExInfo) {
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

                getPostHasNext = data.hasNext
                getPostIsFirst = data.isFirst
            }
        }
    }

    override fun onResume() {
        super.onResume()
        postingRvAdapter.removeAllItem()
        viewModel.community(postType, workOutCategory, currentPage, pageSize)
    }

}