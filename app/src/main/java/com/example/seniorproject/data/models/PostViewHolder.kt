package com.example.seniorproject.data.models

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Posts.ClickedPost

import kotlinx.android.synthetic.main.rv_post.view.*

open class PostViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

    fun bind(post: Post, mContext : Context) {

        itemView.post_title.text = post.title
        itemView.username.text = post.author
        itemView.post_timestamp.text = post.Ptime
        itemView.setOnClickListener {
            val intent = Intent(mContext, ClickedPost::class.java)

            intent.putExtra("Title", post.title)
            intent.putExtra("Text", post.text)
            intent.putExtra("Pkey", post.key)
            intent.putExtra("Classkey", post.Classkey)
            intent.putExtra("UserID", post.UserID)
            intent.putExtra("Author", post.author)
            intent.putExtra("crn", post.crn)
            intent.putExtra("subject", post.subject)
            intent.putExtra("Ptime", post.Ptime)
            //val bundle = bundleOf()
            mContext.startActivity(intent)

        }
    }
}