package com.unity.mynativeapp.features.community

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import com.unity.mynativeapp.databinding.DialogPostSortBinding

class PostSortDialog(context: Context): Dialog(context) {

    val binding by lazy { DialogPostSortBinding.inflate(layoutInflater) }
    var sortCbList = arrayListOf<String>()   // 정렬 체크박스 리스트
    var categoryCbList = arrayListOf<String>()      // 카테고리 체크박스 리스트
    interface PostFragmentInterfacer {
        fun onButtonClick(inputcb: String, inputscb: String)
    }
    private var fragmentInterfacer: PostFragmentInterfacer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCbList()
        setsortCbList()
        binding.btnApply.setOnClickListener {
            val inputcb = setCbList().toString()
            val inputscb = setsortCbList().toString()
            fragmentInterfacer?.onButtonClick(inputcb,inputscb)
            dismiss()
        }
    }
    fun setFragmentInterfacer(fragmentInterfacer: PostFragmentInterfacer) {
        this.fragmentInterfacer = fragmentInterfacer
    }
    override fun show() {
        if (!this.isShowing) super.show()
    }

    override fun dismiss() {
        if (this.isShowing) super.dismiss()
    }


    private fun setsortCbList() {
        with(binding) {
            sortCbList.add(cbQAndA.text.toString())
            sortCbList.add(cbShowOff.text.toString())
            sortCbList.add(cbKnowledgeSharing.text.toString())
            sortCbList.add(cbSortTotal.text.toString())
            sortCbList.add(cbAssess.text.toString())
            sortCbList.add(cbFree.text.toString())

        }
    }
    private fun setCbList() {
        with(binding) {
            categoryCbList.add(cbCategoryTotal.text.toString())
            categoryCbList.add(cbHealth.text.toString())
            categoryCbList.add(cbPilates.text.toString())
            categoryCbList.add(cbYoga.text.toString())
            categoryCbList.add(cbJogging.text.toString())
            categoryCbList.add(cbEtc.text.toString())

        }
    }


}

