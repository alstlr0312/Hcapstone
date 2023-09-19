package com.unity.mynativeapp.features.comment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.nfc.Tag
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.databinding.ItemRvParentCommentBinding
import com.unity.mynativeapp.model.CommentData
import com.unity.mynativeapp.R
import com.unity.mynativeapp.features.mypage.MemberPageActivity
import com.unity.mynativeapp.features.postdetail.PostDetailActivity
import com.unity.mynativeapp.network.util.SimpleDialog

class ParentCommentRvAdapter(
    val context: Context, listener: OnCommentClick
): RecyclerView.Adapter<ParentCommentRvAdapter.ViewHolder>(), View.OnCreateContextMenuListener {

    private var postId = -1
    private var focusParentId = -1
    private var parentList = mutableListOf<CommentData>()
    private val commentListener = listener
    private var childCommentAdapterList = mutableListOf<ChildCommentRvAdapter>()
    private var longClickPos = -1
    private var bgColorChangePos = -1
    private var bindingList = mutableListOf<ItemRvParentCommentBinding>()
    private val username = MyApplication.prefUtil.getString("username", "")
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

                if(commentItem.commentId != null){
                    layoutInvisibleChildComment.visibility = View.GONE
                    layoutVisibleChildComment.visibility = View.GONE

                    // 부모 댓글
                    if(commentItem.childCount != -1) {

                        if(!findChildAdapter(commentItem.commentId)){
                            // 답글 어댑터 설정
                            val childCommentAdapter =
                                ChildCommentRvAdapter(context, commentListener, commentItem.commentId)
                            binding.rvChildComment.adapter = childCommentAdapter
                            childCommentAdapterList.add(childCommentAdapter)
                        }

                        // 자식 댓글이 있다면
                        if (commentItem.childCount > 0) {
                            layoutInvisibleChildComment.visibility = View.VISIBLE
                            tvChildCommentNum.text = "${commentItem.childCount}개"
                            layoutVisibleChildComment.visibility = View.GONE
                        }
                    }

                }

            }

            if(adapterPosition == bgColorChangePos){
                binding.root.background = ColorDrawable(context.getColor(R.color.main_red_dark))
            }else if(commentItem.commentId == focusParentId){
                longClickPos = adapterPosition
                binding.root.background = ColorDrawable(context.getColor(R.color.main_red_dark))
            }else{
                binding.root.background = ColorDrawable(Color.TRANSPARENT)
            }

            // 답글 보기 클릭
            binding.layoutInvisibleChildComment.setOnClickListener {

                val childNum = commentItem.childCount
                if(childNum > 0) { // 부모 댓글에 자식 댓글이 있다면 자식 댓글 조회 요청
                    commentItem.commentId?.let { it -> commentListener.childCommentGetListener(it) }
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

            binding.root.setOnLongClickListener {// 롱클릭
                Log.d("longClick", "parent.commendId: ${commentItem.commentId}, adapterPosition: $adapterPosition")
                longClickPos = adapterPosition
                false
            }
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
        childCommentAdapterList = mutableListOf()
        notifyDataSetChanged()
    }

    fun addItem(item: CommentData){
        parentList.add(item)
        notifyItemChanged(itemCount-1)
    }

    // 부모 댓글 포커스 취소
    fun setCommentUnFocus(){
        bgColorChangePos = -1; focusParentId = -1
        notifyItemChanged(longClickPos)
        longClickPos = -1
    }

    // 부모 댓글 삭제
    fun setCommentDelete(commentId: Int){
        for(cmt in parentList){
            if(cmt.commentId == commentId){
                val index = parentList.indexOf(cmt)
                parentList.removeAt(index)
                setCommentUnFocus()
                notifyItemRemoved(index)
                return
            }
        }
    }

    // 자식 댓글 리스트 설정
    fun setChildComments(parentId: Int, commentData: List<CommentData>){
        for(adapter in childCommentAdapterList){
            if(adapter.getParentId() == parentId){
                adapter.setItemList(commentData as MutableList<CommentData>)
                break
            }
        }
//        for(parentIdx in parentList.indices){
//            if(parentList[parentIdx].commentId == parentId){
//                parentList[parentIdx].childCount = commentData.size
//            }
//        }

    }

    // 자식 댓글 포커스 취소
    fun setChildCommentUnFocus(parentId: Int){
        for(childAdapter in childCommentAdapterList){
            if(childAdapter.getParentId() == parentId){
                childAdapter.setChildCommentUnFocus()
                return
            }
        }
    }

    // 자식 댓글 삭제
    fun setChildCommentDelete(commentId: Int, parentId: Int){
        for(childAdapterIdx in childCommentAdapterList.indices){
            val childAdapter = childCommentAdapterList[childAdapterIdx]
            if(childAdapter.getParentId() == parentId){
                childAdapter.setChildCommentDelete(commentId)
                if(childAdapter.itemCount == 0){
                    commentListener.parentCommentGetListener()
                }
                break
            }
        }
    }


    fun setPostId(id: Int){
        postId = id
    }

    fun setFocusId(id: Int){
        focusParentId = id
    }

    fun findChildAdapter(parentId: Int): Boolean{
        for(adapter in childCommentAdapterList){
            if(parentId == adapter.getParentId()){
                return true
            }
        }
        return false
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        view: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {

        // 부모 댓글 롱클릭 메뉴 설정
        if(longClickPos != -1 && parentList[longClickPos].commentId != null){ // 댓글 화면에서 롱클릭한 댓글이 있다면
            val writeChildCommentItem = menu?.add(Menu.NONE, 1001, 1, "답글 달기")
            // 답글 달기 클릭
            writeChildCommentItem?.setOnMenuItemClickListener {
//                if (parentList[longClickPos].commentId == null) { // 게시글 상세 화면의 댓글 메뉴에서 "답글 달기" 클릭한 경우
//                    val intent = Intent(context, CommentActivity::class.java)
//                    intent.putExtra("postId", postId)
//                    intent.putExtra(
//                        "parentId",
//                        parentList[longClickPos].commentId
//                    ) // 대댓글 달기 위해 부모 댓글의 id 전달
//                    context.startActivity(intent)
//                } else { // 댓글 화면의 댓글 메뉴에서 "답글 달기"를 클릭한 경우
//                    bgColorChangePos = longClickPos
//                    notifyItemChanged(bgColorChangePos)
//                    commentListener.writeChildComment(parentList[longClickPos].commentId!!)
//                }
                if(context is PostDetailActivity){
                    Log.d("focusView", "postDetail")
                    val intent = Intent(context, CommentActivity::class.java)
                    intent.putExtra("postId", postId)
                    intent.putExtra(
                        "parentId",
                        parentList[longClickPos].commentId
                    ) // 대댓글 달기 위해 부모 댓글의 id 전달
                    context.startActivity(intent)

                }else if(context is CommentActivity){
                    bgColorChangePos = longClickPos
                    notifyItemChanged(bgColorChangePos)
                    commentListener.writeChildComment(parentList[longClickPos].commentId!!)
                    Log.d("longClick", "답글 달기 클릭: pos: $longClickPos, id: ${parentList[longClickPos].commentId}")
                }


                true
            }

            if(parentList[longClickPos].username == username){ // 내가 작성한 댓글(댓글 삭제)
                val commentRemoveItem = menu?.add(Menu.NONE, 1002, 2, "댓글 삭제")
                commentRemoveItem?.setOnMenuItemClickListener {
                    bgColorChangePos = longClickPos
                    notifyItemChanged(bgColorChangePos)

                    val dialog = SimpleDialog(context, context.getString(R.string.you_want_delete_comment))
                    dialog.show()
                    dialog.setOnDismissListener {
                        if(dialog.resultCode == 1){ // 댓글 삭제 요청
                            parentList[longClickPos].commentId?.let { commentListener.deleteParentCommentListener(it) }
                        }else{
                            setCommentUnFocus()
                        }
                    }
                    true
                }

            }else { // 타인이 작성한 댓글(댓글 신고, 회원 정보)
                val commentAccuseItem = menu?.add(Menu.NONE, 1003, 3, "댓글 신고")
                val memberInfoItem = menu?.add(Menu.NONE, 1003, 4, "회원 정보")

                commentAccuseItem?.setOnMenuItemClickListener {// 댓글 신고
                    val dialog = AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.notification))
                        .setMessage("해당 기능은 추후 업데이트 될 예정입니다!")
                        .setPositiveButton(context.getString(R.string.confirm)) { p0, _ -> p0.dismiss()}
                        .create()
                    dialog.show()

                    true
                }
                memberInfoItem?.setOnMenuItemClickListener {// 회원 정보 페이지로 이동
                    val intent = Intent(context, MemberPageActivity::class.java)
                    intent.putExtra("username", parentList[longClickPos].username)
                    context.startActivity(intent)
                    true
                }
            }
        }
    }

}