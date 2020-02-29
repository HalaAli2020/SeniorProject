package com.example.seniorproject.MainForum

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import kotlinx.android.synthetic.main.post_rv.view.*
import javax.inject.Inject

class CustomAdapter(context: Context, var savedPosts: PostLiveData) :
    RecyclerView.Adapter<CustomViewHolders>() {
    val mContext:Context = context

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): CustomViewHolders {
        val layoutInflater = LayoutInflater.from(parent.context)
       // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.post_rv, parent, false)
        return CustomViewHolders(cellForRow)
    }

    override fun getItemCount(): Int {
        if (savedPosts.value != null)
            return savedPosts.value!!.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: CustomViewHolders, position: Int) {
        if (savedPosts.value == null) {

        } else {
            val post: Post = savedPosts.value!![position]
            holder.itemView.post_title.text = post.title
            holder.itemView.username.text = post.subject
            holder.itemView.setOnClickListener {
                val intent = Intent(mContext, ClickedPost::class.java)

                intent.putExtra("Title", post.title)
                intent.putExtra("Text", post.text)
                intent.putExtra("Pkey", post.key)
                intent.putExtra("Classkey", post.Classkey)
                intent.putExtra("UserID", post.UserID)
                intent.putExtra("Author", post.author)
                intent.putExtra("crn", post.crn)

                mContext.startActivity(intent)
            }
        }
    }

}

class CustomViewHolders(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

    override fun onClick(v: View) {
        Log.d("RecyclerView", "CLICK!")
    }

}