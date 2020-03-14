package com.example.seniorproject.MainForum


import android.content.Context
import android.content.Intent
import android.database.Observable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.models.PostViewHolder
import com.example.seniorproject.viewModels.HomeFragmentViewModel
//import kotlinx.android.synthetic.main.post_rv.view.*

import javax.inject.Inject

class PostAdapter(context: Context, ViewModel: HomeFragmentViewModel) :
    RecyclerView.Adapter<PostViewHolder>() {
    val mContext:Context = context
    val mViewModel = ViewModel
    var savedPosts : LiveData<MutableList<Post>>? = null
    lateinit var looking : Observer<MutableList<Post>>
    lateinit var obse : Observer<in MutableList<Post>>
    init {
        savedPosts = mViewModel.getPost()



    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.rv_post, parent, false)
        return PostViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        if (savedPosts?.value != null)
            return savedPosts!!.value!!.size
        else
            Log.d("NULL", "Isa null")
            return 0
    }



    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
       holder.bind(savedPosts!!.value!![position], mContext)
        Log.d("In list" , savedPosts!!.value!![position].title)

    }




}
/*else {
            val post: Post = savedPosts.value!![position]
            holder.itemView.post_title.text = post.title
            holder.itemView.username.text = post.crn
            holder.itemView.setOnClickListener {
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
             if(value.size == savedPosts.value!!.size)
              {
                  savedPosts.value = value
              }
            else if(value.size > savedPosts.value!!.size)
              {
                  val s = value.size + 1
                  savedPosts.value!!.add(value[s])
              }
            else if(value.size < savedPosts.value!!.size)
              {
                  savedPosts.value = value
              }


            */
