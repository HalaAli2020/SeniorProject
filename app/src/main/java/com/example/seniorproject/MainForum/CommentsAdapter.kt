package com.example.seniorproject.MainForum

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import kotlinx.android.synthetic.main.rv_post_comment.view.*
import kotlinx.android.synthetic.main.rv_post_header.view.*

class CommentsAdapter(
    context: Context,
    var Comments: CommentLive?,
    title: String,
    text: String,
    author: String,
    crn: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val mContext: Context = context
    private val TYPE_HEADER: Int = 0
    private val TYPE_LIST: Int = 1
    private val title: String = title
    private val text: String = text
    private val author: String = author
    private val crn: String = crn


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
            return CustomViewHoldersHeader(cellForRow)
        }
        val layoutInflater = LayoutInflater.from(parent.context)
        // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.rv_post_comment, parent, false)
        return CustomViewHolders(cellForRow)
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
        } else {
            if (Comments?.value == null) {
                holder.itemView.comment_text.text = "No Comments yet"

            } else {
                val comment: Comment = Comments?.value!![position]
                //holder.itemView.post_title.text = .title
                holder.itemView.comment_text.text = comment.text
                holder.itemView.authcom.text = comment.author

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

    fun removeItem(customViewHolders: CustomViewHolders, position: Int): String {
        val comment: Comment = Comments?.value!![customViewHolders.adapterPosition]
        val commentkey: String?= comment.UserComkey

       //notifyItemRemoved(customViewHolders.adapterPosition)

        return commentkey!!
    }



    class CustomViewHolders(v: View) : RecyclerView.ViewHolder(v) {

    }


  class CustomViewHoldersHeader(v: View) : RecyclerView.ViewHolder(v) {

    }


}




