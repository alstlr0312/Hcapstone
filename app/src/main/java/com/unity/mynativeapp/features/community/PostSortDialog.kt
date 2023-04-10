package com.unity.mynativeapp.features.community

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import com.unity.mynativeapp.databinding.DialogPostSortBinding

class PostSortDialog(context: Context): Dialog(context) {

    val binding by lazy { DialogPostSortBinding.inflate(layoutInflater) }
    var sortCbList = arrayListOf<CheckBox>()    // 정렬 체크박스 리스트
    var categoryCbList = arrayListOf<CheckBox>()    // 카테고리 체크박스 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCbList()




    }

    override fun show() {
        if (!this.isShowing) super.show()
    }

    override fun dismiss() {
        if (this.isShowing) super.dismiss()
    }

    private fun setCbList() {
        with(binding) {
            categoryCbList.add(cbCategoryTotal)
            categoryCbList.add(cbHealth)
            categoryCbList.add(cbPilates)
            categoryCbList.add(cbYoga)
            categoryCbList.add(cbJogging)
            categoryCbList.add(cbEtc)
        }
    }


}

