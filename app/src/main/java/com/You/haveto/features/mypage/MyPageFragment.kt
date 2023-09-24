package com.You.haveto.features.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.You.haveto.R
import com.You.haveto.config.BaseFragment
import com.You.haveto.databinding.FragmentMypageBinding
import com.You.haveto.model.MyPageRvItem
import com.You.haveto.network.util.PreferenceUtil
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MyPageFragment : BaseFragment<FragmentMypageBinding>(
    FragmentMypageBinding::bind, R.layout.fragment_mypage)  {

    private val viewModel by viewModels<MyPageViewModel>()
    private lateinit var myPageAdapter: MyPageAdapter
    private lateinit var username: String
    override fun onResume() {
        super.onResume()
        // 회원 정보 요청
        viewModel.myPageInfo(username)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = PreferenceUtil(requireContext()).getString("username", "")!!

        myPageAdapter = MyPageAdapter(requireContext(), getRvItemList(), username)
        binding.rvMypage.adapter = myPageAdapter

        setUiEvent()
        subscribeUI()
    }

    private fun getRvItemList(): MutableList<MyPageRvItem>{
        var list = mutableListOf<MyPageRvItem>()
        list.add(MyPageRvItem(R.drawable.ic_post, getString(R.string.posting), 0))
        list.add(MyPageRvItem(R.drawable.ic_comments, getString(R.string.comment), 0))
        list.add(MyPageRvItem(R.drawable.ic_edit_profile, getString(R.string.profile_edit)))
        list.add(MyPageRvItem(R.drawable.ic_setting, getString(R.string.setting)))
        return list
    }

    private fun setUiEvent(){


    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            showCustomToast(message)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoadingDialog(requireContext()) else dismissLoadingDialog()
        }

        viewModel.logout.observe(viewLifecycleOwner){ logout ->
            if(!logout)return@observe
            logout()
        }

        viewModel.myPageData.observe(viewLifecycleOwner) { data ->
            if(data==null) return@observe

            if(data.profileImage != null){
                CoroutineScope(Dispatchers.Main).launch {
                    Glide.with(binding.ivProfileImg)
                        .load(data.profileImage)
                        .placeholder(R.color.main_black)
                        .error(R.drawable.ic_profile_photo_base)
                        .apply(RequestOptions.centerCropTransform())
                        .into(binding.ivProfileImg)
                }
            }else{
                binding.ivProfileImg.setImageResource(R.drawable.ic_profile_photo_base)
            }

            binding.tvUsername.text = data.username

            if(data.email != null){
                binding.layoutEmail.visibility = View.VISIBLE
                binding.tvEmail.text = data.email
            }else{
                binding.layoutEmail.visibility = View.GONE
            }

            if(data.field != null){
                binding.layoutField.visibility = View.VISIBLE
                binding.tvField.text = data.field
            }else{
                binding.layoutField.visibility = View.GONE
            }

            val date = data.createdAt
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val dateTime: LocalDateTime = LocalDateTime.parse(date, formatter)
            val formatDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일에 가입함"))
            binding.tvStartDate.text = formatDate
            binding.layoutStartDate.visibility = View.VISIBLE

            // 게시글, 댓글 개수
            myPageAdapter.setPostCounts(data.postCount)
            data.commentCount?.let { myPageAdapter.setCmtCounts(data.commentCount)}

        }
    }
}