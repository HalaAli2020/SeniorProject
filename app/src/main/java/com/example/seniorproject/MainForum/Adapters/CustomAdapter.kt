package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.MainForum.Posts.ClickedPost
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import kotlinx.android.synthetic.main.rv_post.view.*

class CustomAdapter(context: Context, var savedPosts: PostLiveData, var type:Int ) :
    RecyclerView.Adapter<CustomViewHolders>() {
    val mContext:Context = context

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): CustomViewHolders {
        val layoutInflater = LayoutInflater.from(parent.context)
       // val binding : ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        val cellForRow = layoutInflater.inflate(R.layout.rv_post, parent, false)
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
            holder.itemView.post_title.text="LOADING"
        } else {
            val post: Post = savedPosts.value!![position]
            holder.itemView.post_title.text = post.title
            holder.itemView.username.text = post.author

            if(type==0){
                holder.itemView.username.text = post.crn
            }

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

    fun removeItem(customViewHolders: CustomViewHolders, position: Int): String {
        //position=customViewHolders.adapterPosition

       // val post: Post = savedPosts.value!![customViewHolders.adapterPosition]
        val post: Post = savedPosts.value!![customViewHolders.adapterPosition]
        val postkey: String?= post.Classkey
        //notifyItemRemoved(customViewHolders.adapterPosition)
        //notifyItemRangeChanged(customViewHolders.adapterPosition, itemCount)

        return postkey!!
    }

    fun getUserKey(customViewHolders: CustomViewHolders, position: Int): String {
        //position=customViewHolders.adapterPosition

        // val post: Post = savedPosts.value!![customViewHolders.adapterPosition]
        val post: Post = savedPosts.value!![customViewHolders.adapterPosition]
        val postkey: String?= post.UserID
        //notifyItemRemoved(customViewHolders.adapterPosition)
        //notifyItemRangeChanged(customViewHolders.adapterPosition, itemCount)

        return postkey!!
    }

    fun getNewCount() : Int{
        if (savedPosts.value != null)
            return savedPosts.value!!.size-1
        else
            return 0
    }


        //notifyD
        //notifyItemRemoved(customViewHolders.adapterPosition)

        //adapter.notifyItemRangeRemoved(customViewHolders.adapterPosition, 1)
        //adapter.notifyItemRangeChanged(viewHolders.adapterPosition, adapter.itemCount - viewHolders.adapterPosition+1)
    }

    /*fun alertItem(customViewHolders: CustomViewHolders, position: Int): AlertDialog.Builder {

        var builder = AlertDialog.Builder(customViewHolders.itemView.context)

        builder.setTitle("Are you sure?")
        builder.setMessage("You cannot restore posts that have been deleted")
        builder.setPositiveButton("DELETE",
            { dialogInterface: DialogInterface?, i: Int ->
                builder.show()
            })
        builder.setNegativeButton("CANCEL",
            { dialogInterface: DialogInterface?, i: Int ->
                builder.show()
            })

        return builder

    }*/



class CustomViewHolders(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

    override fun onClick(v: View) {
        Log.d("RecyclerView", "CLICK!")
    }

}