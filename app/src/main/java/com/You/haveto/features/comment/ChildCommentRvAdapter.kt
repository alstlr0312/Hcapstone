package com.You.haveto.features.comment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.You.haveto.MyApplication
import com.You.haveto.R
import com.You.haveto.databinding.ItemRvChildCommentBinding
import com.You.haveto.features.mypage.MemberPageActivity
import com.You.haveto.model.CommentData
import com.You.haveto.network.util.SimpleDialog

class ChildCommentRvAdapter(val context: Context, listener: OnCommentClick, private val parentId: Int = -1)
    : RecyclerView.Adapter<ChildCommentRvAdapter.ViewHolder>(), View.OnCreateContextMenuListener  {

    private var itemList = mutableListOf<CommentData>()
    private var longClickPos = -1
    private var bgColorChangePos = -1
    private val commentListener = listener
    private val username = MyApplication.prefUtil.getString("username", "")
    inner class ViewHolder(val binding: ItemRvChildCommentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(commentItem: CommentData){

            with(binding){

                Glide.with(ivProfileImg)
                    .load(commentItem.profileImage)
                    .placeholder(R.drawable.ic_profile_photo_base)
                    .error(R.drawable.ic_profile_photo_base)
                    .fallback(R.drawable.ic_profile_photo_base)
                    .apply(RequestOptions.centerCropTransform())
                    .into(ivProfileImg)

                tvUsername.text = commentItem.username
                tvPostDate.text = commentItem.createdAt
                tvCommentContext.text = commentItem.commentContext

                if(adapterPosition == bgColorChangePos){
                    binding.root.background = ColorDrawable(context.getColor(R.color.main_red_dark))
                }else{
                    binding.root.background = ColorDrawable(Color.TRANSPARENT)
                }


                binding.root.setOnCreateContextMenuListener(this@ChildCommentRvAdapter)
                binding.root.setOnLongClickListener {// 롱클릭
                    Log.d("longClick", "child.commendId: ${commentItem.commentId}, adapterPosition: $adapterPosition")
                    longClickPos = adapterPosition
                    false
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildCommentRvAdapter.ViewHolder {
        return ViewHolder(ItemRvChildCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChildCommentRvAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setItemList(nList: MutableList<CommentData>){
        itemList = nList
        notifyDataSetChanged()
    }

    fun removeAllItem(){
        itemList = mutableListOf()
        notifyDataSetChanged()
    }

    fun addItem(comment: CommentData){
        itemList.add(comment)
        notifyItemChanged(itemCount-1)
    }

    fun getParentId(): Int{
        return parentId
    }

    // 자식 댓글 포커스 취소
    fun setChildCommentUnFocus(){
        bgColorChangePos = -1
        notifyItemChanged(longClickPos)
        longClickPos = -1
    }

    // 자식 댓글 삭제
    fun setChildCommentDelete(commentId: Int){
        for(cmt in itemList){
            if(cmt.commentId == commentId){
                val index = itemList.indexOf(cmt)
                itemList.removeAt(index)
                setChildCommentUnFocus()
                notifyItemRemoved(index)
                break
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        view: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        // 자식 댓글 롱클릭 메뉴 설정
        if(longClickPos != -1 && itemList[longClickPos].commentId != null){ // 댓글 화면에서 롱클릭한 댓글이 있다면

            if(itemList[longClickPos].username == username){ // 내가 작성한 댓글(댓글 삭제)
                val commentRemoveItem = menu?.add(Menu.NONE, 2001, 11, "댓글 삭제")
                commentRemoveItem?.setOnMenuItemClickListener {
                    bgColorChangePos = longClickPos
                    notifyItemChanged(bgColorChangePos)

                    val dialog = SimpleDialog(context, context.getString(R.string.you_want_delete_comment))
                    dialog.show()
                    dialog.setOnDismissListener {
                        if(dialog.resultCode == 1){ // 댓글 삭제 요청

                            itemList[longClickPos].commentId?.let {
                                commentListener.deleteChileCommentListener(it, parentId) }
                        }else{
                            setChildCommentUnFocus()
                        }
                    }
                    true
                }

            }else { // 타인이 작성한 댓글(댓글 신고, 회원 정보)
                val commentAccuseItem = menu?.add(Menu.NONE, 2002, 22, "댓글 신고")
                val memberInfoItem = menu?.add(Menu.NONE, 2003, 33, "회원 정보")

                commentAccuseItem?.setOnMenuItemClickListener {// 댓글 신고
                    val dialog = SimpleDialog(context, "해당 기능은 추후 업데이트 될 예정입니다!")
                    dialog.show()

                    true
                }
                memberInfoItem?.setOnMenuItemClickListener {// 회원 정보 페이지로 이동
                    val intent = Intent(context, MemberPageActivity::class.java)
                    intent.putExtra("username", itemList[longClickPos].username)
                    context.startActivity(intent)
                    true
                }
            }
        }
    }
}