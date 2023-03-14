package com.unity.mynativeapp.Main.community

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.unity.mynativeapp.R
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
            //sortCbList.add(cbSortTotal)
            ///sortCbList.add(cbQAndA)
            //sortCbList.add(cbKnowledgeSharing)
            //sortCbList.add(cbShowOff)
            //sortCbList.add(cbAssess)

            categoryCbList.add(cbCategoryTotal)
            categoryCbList.add(cbHealth)
            categoryCbList.add(cbPilates)
            categoryCbList.add(cbYoga)
            categoryCbList.add(cbJogging)
            categoryCbList.add(cbEtc)
        }
    }


}
