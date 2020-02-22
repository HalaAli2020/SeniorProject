package com.example.seniorproject.MainForum

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.post_rv.view.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count





class CustomAdapter(context: Context, var savedPosts: Flow<List<Post>>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    val mContext:Context = context
    var savedMutablePosts : LiveData<List<Post>> = MutableLiveData()
    val mSavedPost: Flow<List<Post>> = savedPosts

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        savedMutablePosts = mSavedPost.asLiveData()
        val layoutInflater = LayoutInflater.from(parent.context)
        // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.post_rv, parent, false)
        return ViewHolder(cellForRow)
    }


    override fun getItemCount(): Int {
        if (savedMutablePosts.value != null)
            return savedMutablePosts.value!!.size
        else
            return 0
       // return savedPosts.asLiveData()
    }


    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        var i: Int

        for (i in 0..itemCount)
        {
                if (savedMutablePosts.value == null) {

                } else {

                    val post: Post = savedMutablePosts.value!![position]
                    holder.itemView.post_title.text = post.title
                    holder.itemView.post_text.text = post.text

                    holder.itemView.setOnClickListener {
                        val intent = Intent(mContext, ClickedPost::class.java)
                        intent.putExtra("Title", post.title)
                        intent.putExtra("Text", post.text)
                        mContext.startActivity(intent)
                    }
                    //holder.post.bind(savedPosts[position])


                    /* holder.itemView.post_title.text = post.title
                 holder.itemView.post_text.text = post.text

                 holder.itemView.setOnClickListener {
                     val intent = Intent(mContext, ClickedPost::class.java)
                     intent.putExtra("Title", post.title)
                     intent.putExtra("Text", post.text)
                     mContext.startActivity(intent)*/
                }


        }
    }




    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        /*fun bindItems(post: Post){
            val textPostTitle= v.findViewById(R.id.post_title) as TextView
            val textPostText= v.findViewById(R.id.post_text) as TextView
            textPostText.text=post.text
            textPostTitle.text=post.title
        }*/


        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

    }

}




/*class CustomAdapter(context: Context, var savedPosts: Flow<List<Post>>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    val mContext:Context = context

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
       // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.post_rv, parent, false)
        return ViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {

        for (item in savedPosts) {
            if (item == null) {

            } else {

                val post: Post = savedPosts!![position]
                holder.itemView.post_title.text = post.title
                holder.itemView.post_text.text = post.text

                holder.itemView.setOnClickListener {
                    val intent = Intent(mContext, ClickedPost::class.java)
                    intent.putExtra("Title", post.title)
                    intent.putExtra("Text", post.text)
                    mContext.startActivity(intent)
                }
                //holder.post.bind(savedPosts[position])


               /* holder.itemView.post_title.text = post.title
                holder.itemView.post_text.text = post.text

                holder.itemView.setOnClickListener {
                    val intent = Intent(mContext, ClickedPost::class.java)
                    intent.putExtra("Title", post.title)
                    intent.putExtra("Text", post.text)
                    mContext.startActivity(intent)*/
                }

            }
        }


     class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

         /*fun bindItems(post: Post){
             val textPostTitle= v.findViewById(R.id.post_title) as TextView
             val textPostText= v.findViewById(R.id.post_text) as TextView
             textPostText.text=post.text
             textPostTitle.text=post.title
         }*/


        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

    }}*/







