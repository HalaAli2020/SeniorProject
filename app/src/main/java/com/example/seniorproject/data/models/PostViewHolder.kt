package com.example.seniorproject.data.models

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.ClickedPost
import com.example.seniorproject.R

import kotlinx.android.synthetic.main.rv_post.view.*

open class PostViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
    //var mPost = Post()





    fun bind(post: Post, mContext : Context) {

        itemView.post_title.text = post.title
        itemView.username.text = post.crn
        itemView.setOnClickListener {
            val intent = Intent(mContext, ClickedPost::class.java)

            intent.putExtra("Title", post.title)
            intent.putExtra("Text", post.text)
            intent.putExtra("Pkey", post.key)
            intent.putExtra("Classkey", post.Classkey)
            intent.putExtra("UserID", post.UserID)
            intent.putExtra("Author", post.author)
            intent.putExtra("crn", post.crn)
            //val bundle = bundleOf()
            mContext.startActivity(intent)

        }
    }
}