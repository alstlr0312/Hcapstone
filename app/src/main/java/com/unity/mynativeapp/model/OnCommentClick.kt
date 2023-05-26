package com.unity.mynativeapp.model

interface OnCommentClick{
    fun childCommentGetListener(parentId: Int)
    fun writeChildComment(parentId: Int)
}