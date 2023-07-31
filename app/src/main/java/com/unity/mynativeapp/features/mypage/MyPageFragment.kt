package com.unity.mynativeapp.features.mypage

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseFragment
import com.unity.mynativeapp.databinding.FragmentMypageBinding
import com.unity.mynativeapp.model.MyPageRvItem
import com.unity.mynativeapp.network.util.PreferenceUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MyPageFragment : BaseFragment<FragmentMypageBinding>(
    FragmentMypageBinding::bind, R.layout.fragment_mypage)  {

    private val viewModel by viewModels<MyPageViewModel>()
    private lateinit var myPageAdapter: MyPageAdapter
    lateinit var userName: String

    override fun onResume() {
        super.onResume()
        // 회원 정보 요청
        val username = PreferenceUtil(requireContext()).getString("username", null)
        if(username != null){
            viewModel.myPageInfo(username)
        }else{
            showCustomToast("username is null")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myPageAdapter = MyPageAdapter(requireContext(), getRvItemList())
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

        viewModel.logout.observe(viewLifecycleOwner){
            if(it) logout()
        }

        viewModel.myPageData.observe(viewLifecycleOwner) { data ->
            if(data==null) return@observe

            if(data.profileImage != null){
                MainScope().async {
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

            var date = data.createdAt
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val dateTime: LocalDateTime = LocalDateTime.parse(date, formatter)
            val formatDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일에 가입함"))
            binding.tvStartDate.text = formatDate
            binding.layoutStartDate.visibility = View.VISIBLE

            data.commentCount?.let { myPageAdapter.setCounts(data.postCount, it) }

        }
    }
}