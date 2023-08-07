package com.unity.mynativeapp.features.community

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
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
    lateinit var sortAdapter: CheckBoxAdapter
    lateinit var categoryAdapter: CheckBoxAdapter
    var resultCode = 0
    var checkedSortText: String? = null
    var checkedCateText: String? = null

    var sortTextList = listOf(R.string.total, R.string.q_and_a, R.string.knowledge_sharing,
        R.string.show_off, R.string.assess, R.string.free)
    var categoryTextList = listOf(R.string.total, R.string.health, R.string.pilates,
        R.string.yoga, R.string.jogging, R.string.etc)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sortAdapter = CheckBoxAdapter(setCbList(sortTextList))
        binding.rvSort.layoutManager = GridLayoutManager(context, 3)
        binding.rvSort.adapter = sortAdapter

        categoryAdapter = CheckBoxAdapter(setCbList(categoryTextList))
        binding.rvCategory.layoutManager = GridLayoutManager(context, 3)
        binding.rvCategory.adapter = categoryAdapter

        binding.btnCancel.setOnClickListener {
            resultCode = 0
            dismiss()
        }
        binding.btnApply.setOnClickListener {
            resultCode = 1
            checkedSortText = sortAdapter.getCheckedText()
            checkedCateText = categoryAdapter.getCheckedText()
            dismiss()
        }

    }

    override fun show() {
        if (!this.isShowing) super.show()
    }

    override fun dismiss() {
        if (this.isShowing) super.dismiss()
    }

    private fun setCbList(titleList: List<Int>): MutableList<CbItem> {
        var list = mutableListOf<CbItem>()

        for(title in titleList){
            if(title == R.string.total){
                list.add(CbItem(context.getString(title), true))
            }else{
                list.add(CbItem(context.getString(title), false))
            }
        }
        return list
    }

    inner class CheckBoxAdapter(var cbList: MutableList<CbItem>): RecyclerView.Adapter<CheckBoxAdapter.ViewHolder>(){

        var bindingList = mutableListOf<ItemRvCheckboxBinding>()
        inner class ViewHolder(val binding: ItemRvCheckboxBinding): RecyclerView.ViewHolder(binding.root){
            fun bind(item: CbItem){
                binding.checkBox.text = item.title
                binding.checkBox.isChecked = item.isChecked
            }

            init{
                bindingList.add(binding)
                binding.checkBox.setOnClickListener {
                    for(item in bindingList){
                        item.checkBox.isChecked = item == binding
                    }
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemRvCheckboxBinding.inflate(layoutInflater, parent, false))
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(cbList[position])
        }
        override fun getItemCount(): Int {
            return cbList.size
        }

        fun getCheckedText(): String? {
            for(item in bindingList){
                if(item.checkBox.isChecked){
                    return item.checkBox.text.toString()
                }
            }
            return null
        }


    }
}

