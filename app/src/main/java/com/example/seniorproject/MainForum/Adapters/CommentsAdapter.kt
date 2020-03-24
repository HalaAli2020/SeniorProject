package com.example.seniorproject.MainForum.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Posts.ClickedPost
import com.example.seniorproject.MainForum.UserProfileActivity
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import kotlinx.android.synthetic.main.rv_post.view.*
import kotlinx.android.synthetic.main.rv_post_comment.view.*
import kotlinx.android.synthetic.main.rv_post_header.view.*

class CommentsAdapter(
    context: Context,
    var Comments: CommentLive?,
    title: String,
    text: String,
    author: String,
    crn: String,
    UserID : String, ptime: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val mContext: Context = context
    private val TYPE_HEADER: Int = 0
    private val TYPE_LIST: Int = 1
    private val title: String = title
    private val text: String = text
    private val author: String = author
    private val crn: String = crn
    private val UserID: String = UserID
    private val ptime: String = ptime


    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER
        }
        return TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0) {
            val layoutInflater = LayoutInflater.from(parent.context)
            // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
            val cellForRow = layoutInflater.inflate(R.layout.rv_post_header, parent, false)
            return CustomViewHoldersHeader(
                cellForRow
            )
        }
        val layoutInflater = LayoutInflater.from(parent.context)
        // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.rv_post_comment, parent, false)
        return CustomViewHolders(
            cellForRow
        )
    }

    override fun getItemCount(): Int {
        if (Comments?.value != null)
            return Comments?.value!!.size
        else
            return 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            Log.d("CommentsAdapter:", "" + position)
            if (holder is CustomViewHoldersHeader) {
                holder.itemView.click_post_title.text = title
                holder.itemView.click_post_text.text = text
                holder.itemView.community_name_TV.text = crn
                holder.itemView.author_name_TV.text = author
                holder.itemView.posts_timestamp.text=ptime
                holder.itemView.author_name_TV.setOnClickListener {
                    val intent = Intent(mContext, UserProfileActivity::class.java)
                    intent.putExtra("UserID", UserID)
                    intent.putExtra("Author", author)
                    mContext.startActivity(intent)
                }

            } else {
                if (Comments?.value == null || getItemCount() == 0) {
                    holder.itemView.comment_text.text = "No Comments yet"
                    //need to get the No comments yet to show up

                } else {
                    val comment: Comment = Comments?.value!![position]
                    //holder.itemView.post_title.text = comment.title
                    holder.itemView.comment_text.text = comment.text
                    holder.itemView.authcom.text = comment.author
                    holder.itemView.comment_timestamp.text=comment.Ptime

                    holder.itemView.authcom.setOnClickListener {
                        val intent = Intent(mContext, UserProfileActivity::class.java)
                        intent.putExtra("UserID", comment.PosterID)
                        intent.putExtra("Author", comment.author)
                        mContext.startActivity(intent)
                    }

                    /* holder.itemView.setOnClickListener {
                 val intent = Intent(mContext, ClickedPost::class.java)
                 intent.putExtra("Title", post.title)
                 intent.putExtra("Text", post.text)
                 intent.putExtra("Key", post.Classkey)
                 mContext.startActivity(intent)
             }*/

                }

        }
        }


    fun removeItem(holder: RecyclerView.ViewHolder): String {
        val comment: Comment = Comments?.value!![holder.adapterPosition]
        val commentkey: String?= comment.Classkey

        return commentkey!!
    }

    fun getCrn(holder: RecyclerView.ViewHolder, position: Int): String {
        val comment: Comment = Comments?.value!![holder.adapterPosition]
        val commentkey: String?= comment.crn

        return commentkey!!
    }

    fun getUserKey(holder: RecyclerView.ViewHolder): String {
        val comment: Comment = Comments?.value!![holder.adapterPosition]
        val commentkey: String?= comment.PosterID

        return commentkey!!
    }

    fun getText(holder: RecyclerView.ViewHolder): String {
        val comment: Comment = Comments?.value!![holder.adapterPosition]
        val commentkey: String?= comment.text

        return commentkey!!
    }

    fun getPostKey(holder: RecyclerView.ViewHolder): String {
        val comment: Comment = Comments?.value!![holder.adapterPosition]
        val commentkey: String?= comment.Postkey

        return commentkey!!
    }

        class CustomViewHolders(v: View) : RecyclerView.ViewHolder(v) {

        }


        class CustomViewHoldersHeader(v: View) : RecyclerView.ViewHolder(v) {

        }

    }




