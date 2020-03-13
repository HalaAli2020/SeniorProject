package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Posts.ClickedPost
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import kotlinx.android.synthetic.main.rv_post.view.*
import kotlinx.android.synthetic.main.rv_post_comment.view.*

class ProfileCommentsAdapter(context: Context, var ProfileComments: CommentLive) :
    RecyclerView.Adapter<CustomViewHolders>() {
    val mContext:Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolders {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.rv_post_comment, parent, false)
        return CustomViewHolders(cellForRow)
    }

    override fun getItemCount(): Int {
        if (ProfileComments.value != null)
            return ProfileComments.value!!.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: CustomViewHolders, position: Int) {
        if (ProfileComments.value == null) {
            holder.itemView.comment_text.text = "LOADING"
        } else {
            val comment: Comment = ProfileComments.value!![position]
            holder.itemView.comment_text.text = comment.text
            //holder.itemView.username.text = post.author

        }

        //val mContext: Context = context
    }

    fun getCommentKey(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.UserComkey

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getUserKey(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.PosterID

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getClassKey(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.Classkey

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getClassProfileKey(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.ProfileComKey

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun pkeyUserProfile(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.Postkey

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getCrn(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.crn

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }

    fun getText(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = ProfileComments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.text

        //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }



}