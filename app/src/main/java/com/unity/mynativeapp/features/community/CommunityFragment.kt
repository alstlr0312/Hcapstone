package com.unity.mynativeapp.features.community


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.FragmentCommunityBinding
import com.unity.mynativeapp.features.postwrite.PostWriteActivity
import com.unity.mynativeapp.model.PostItem


class CommunityFragment : Fragment() {
    val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<PostViewModel>()
    private lateinit var postingRvAdapter: PostListRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
        with(binding){

            // 게시글 어댑터 설정
            postingRvAdapter = PostListRvAdapter(requireContext())
            rvPosting.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvPosting.adapter = postingRvAdapter
           // postingRvAdapter.getListFromView()



            layoutFilter.setOnClickListener {
                var dialog = PostSortDialog(requireContext())
                dialog.show()
                dialog.setFragmentInterfacer(object : PostSortDialog.PostFragmentInterfacer {
                    override fun onButtonClick(inputcb: String, inputscb: String) {
                      Log.d("1",inputcb)
                        Log.d("2",inputcb)
                    }
                })
            }

        }

        // 게시물 작성
        this.requireActivity().findViewById<FloatingActionButton>(R.id.btn_write_post).setOnClickListener {
            startActivity(Intent(requireContext(), PostWriteActivity::class.java))
        }

        viewModel.community("SHOW_OFF","HEALTH",0,1)
    }

    private fun subscribeUI() {

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





}