package com.unity.mynativeapp.features.comment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.AdapterView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.databinding.ItemRvParentCommentBinding
import com.unity.mynativeapp.model.CommentData
import com.unity.mynativeapp.R
import com.unity.mynativeapp.model.OnCommentClick

class ParentCommentRvAdapter(
    val context: Context, listener: OnCommentClick
)
    : RecyclerView.Adapter<ParentCommentRvAdapter.ViewHolder>(), View.OnCreateContextMenuListener {

    private var postId = -1
    private var parentList = mutableListOf<CommentData>()
    private val commentListener = listener
    private val childCommentAdapterList = mutableListOf<ChildCommentRvAdapter>()
    private var longClickPos = -1
    private var bindingList = mutableListOf<ItemRvParentCommentBinding>()
    inner class ViewHolder(val binding: ItemRvParentCommentBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(commentItem: CommentData){

            with(binding){

                if(commentItem.profileImage!=null){
                    Glide.with(ivProfileImg)
                        .load(commentItem.profileImage)
                        .placeholder(R.drawable.ic_profile_photo_base)
                        .error(R.drawable.ic_profile_photo_base)
                        .fallback(R.drawable.ic_profile_photo_base)
                        .apply(RequestOptions.centerCropTransform())
                        .into(ivProfileImg)
                }

                tvUsername.text = commentItem.username
                tvPostDate.text = commentItem.createdAt
                tvCommentContext.text = commentItem.commentContext

                if(commentItem.commentId == null){ // 게시글 상세조회 화면의 댓글
                    layoutInvisibleChildComment.visibility = View.GONE
                    layoutVisibleChildComment.visibility = View.GONE

                }else{ // 댓글 화면의 댓글
                    if(commentItem.childCount > 0){ // 자식 댓글이 있으면
                        layoutInvisibleChildComment.visibility = View.VISIBLE
                        tvChildCommentNum.text = commentItem.childCount.toString()
                        layoutVisibleChildComment.visibility = View.GONE

                    }else{
                        layoutInvisibleChildComment.visibility = View.GONE
                        layoutVisibleChildComment.visibility = View.GONE

                    }
                }


            }

            // 답글 보기 클릭
            binding.layoutInvisibleChildComment.setOnClickListener {

               val childCommentAdapter = ChildCommentRvAdapter(context)
                binding.rvChildComment.adapter = childCommentAdapter
                childCommentAdapter.setParentId(commentItem.commentId!!)
                childCommentAdapterList.add(childCommentAdapter)

                var childNum = commentItem.childCount
                if(childNum > 0) {
                    commentListener.childCommentGetListener(commentItem.commentId)
                }

                it.visibility = View.GONE
                binding.layoutVisibleChildComment.visibility = View.VISIBLE
            }

            // 답글 닫기 클릭
            binding.tvCloseChildComment.setOnClickListener {
                binding.layoutVisibleChildComment.visibility = View.GONE
                binding.layoutInvisibleChildComment.visibility = View.VISIBLE
            }

            binding.root.setOnCreateContextMenuListener(this@ParentCommentRvAdapter)

            binding.root.setOnLongClickListener {
                longClickPos = adapterPosition
                false
            }


//            binding.rvChildComment.setOnLongClickListener {
//                val popupMenu = PopupMenu(context, binding.root)
//                menuInflater.inflate(R.menu.menu_comment, popupMenu.menu)
//
//                popupMenu.setOnMenuItemClickListener { item ->
//                    when (item.itemId) {
//                        R.id.menuChildComment -> {
//                            focusParentComment = commentItem
//                        }
//                        R.id.menuAccuseComment -> {
//
//                        }
//                        R.id.menuMemberInfo -> {
//
//                        }
//                        else -> {}
//                    }
//                    false
//                }
//            }
        }

        init{
            bindingList.add(binding)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentCommentRvAdapter.ViewHolder {
        return ViewHolder(ItemRvParentCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ParentCommentRvAdapter.ViewHolder, position: Int) {
        holder.bind(parentList[position])
    }

    override fun getItemCount(): Int {
        return parentList.size
    }

    fun getListFromView(nList: MutableList<CommentData>){
        parentList = nList
        notifyDataSetChanged()
    }

    fun removeAllItem(){
        parentList = mutableListOf()
        notifyDataSetChanged()
    }

    fun addItem(item: CommentData){
        parentList.add(item)
        notifyItemChanged(itemCount-1)
    }

    fun setUnFocusComment(){
        bindingList[longClickPos].root.background = ColorDrawable(Color.TRANSPARENT)
        notifyItemChanged(longClickPos)
    }

    fun setChildComments(parentId: Int, commentData: List<CommentData>){
        for(adapter in childCommentAdapterList){
            if(adapter.getParentId() == parentId){
                adapter.setItemList(commentData as MutableList<CommentData>)
            }
        }
    }

    fun setPostId(id: Int){
        postId = id
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        view: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        val writeChildCommentItem = menu?.add(Menu.NONE, 1001, 1, "답글 달기")
        val commentAccuseItem = menu?.add(Menu.NONE, 1002, 2, "댓글 신고")
        val memberInfoItem = menu?.add(Menu.NONE, 1003, 3, "회원 정보")

        if(longClickPos != -1){
            writeChildCommentItem?.setOnMenuItemClickListener {
                if(parentList[longClickPos].commentId == null){
                    val intent = Intent(context, CommentActivity::class.java)
                    intent.putExtra("postId", postId)
                    context.startActivity(intent)
                }else{
                    bindingList[longClickPos].root.background = ColorDrawable(context.getColor(R.color.main_red_dark))
                    commentListener.writeChildComment(parentList[longClickPos].commentId!!)
                }

                true
            }
        }

    }

}