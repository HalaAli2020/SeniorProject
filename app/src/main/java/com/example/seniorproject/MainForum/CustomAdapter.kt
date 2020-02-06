package com.example.seniorproject.MainForum

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import kotlinx.android.synthetic.main.post_rv.view.*

class CustomAdapter(var savedPosts: LiveData<List<Post>>) :
    RecyclerView.Adapter<CustomViewHolders>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolders {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.post_rv, parent, false)
        return CustomViewHolders(cellForRow)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: CustomViewHolders, position: Int) {

            //val post: Post = savedPosts?.value!![position]
            //holder.itemView.post_title.text = post.title
            //holder.itemView.post_text.text = post.text

    }

}

class CustomViewHolders(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

    override fun onClick(v: View) {
        Log.d("RecyclerView", "CLICK!")
    }

}