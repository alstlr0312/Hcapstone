package com.unity.mynativeapp.features.mypage.editprofile

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.DialogPostSortBinding
import com.unity.mynativeapp.databinding.ItemRvCheckboxBinding

data class CbItem(
    val title: String,
    var isChecked: Boolean
)
class PostSortDialog(context: Context): Dialog(context) {

    val binding by lazy { DialogPostSortBinding.inflate(layoutInflater) }
    var resultCode = 0
    var changedPw = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnCancel.setOnClickListener {
            resultCode = 0
            dismiss()
        }
        binding.btnApply.setOnClickListener {
            resultCode = 1
            dismiss()
        }

    }

    override fun show() {
        if (!this.isShowing) super.show()
    }

    override fun dismiss() {
        if (this.isShowing) super.dismiss()
    }


}

