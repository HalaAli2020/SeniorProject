package com.example.seniorproject.MainForum

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import kotlinx.android.synthetic.main.post_rv.view.*
import kotlinx.android.synthetic.main.profile_rv.view.*

class ProfileAdapter (context: Context, var profilePosts: PostLiveData):
    RecyclerView.Adapter<CustomViewHolders>() {
    //val mContext:Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolders {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.profile_rv, parent, false)
        return CustomViewHolders(cellForRow)
    }

    override fun getItemCount(): Int {
        if (profilePosts.value != null)
            return profilePosts.value!!.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: CustomViewHolders, position: Int) {
        if (profilePosts.value == null) {

        } else {
            val post: Post = profilePosts.value!![position]
            holder.itemView.profile_post_title.text = post.title
        }

    }
}