package com.example.seniorproject.Search

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.data.models.CRN
import kotlinx.android.synthetic.main.rv_post.view.*

class SearchViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {
    fun bind(crn : CRN, mContext : Context) {

    }
}
/* itemView.post_title.text = post.title
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

 }*/