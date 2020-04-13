package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seniorproject.MainForum.Posts.ClickedPost
import com.example.seniorproject.MainForum.Posts.CommunityPosts
import com.example.seniorproject.MainForum.UserProfileActivity
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.rv_post.view.post_timestamp
import kotlinx.android.synthetic.main.rv_post.view.post_title
import kotlinx.android.synthetic.main.rv_post.view.username
import kotlinx.android.synthetic.main.rv_post_image.view.*

class PostAdapter(context: Context, private var savedPostList: List<Post>, var type: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val mContext: Context = context

    private val typeText: Int = 0
    private val typeImage: Int = 1

    override fun getItemViewType(position: Int): Int {
        if (savedPostList[position].uri == null || savedPostList[position].uri == "null") {
            return typeText
        }
        return typeImage
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        if (viewType == 1) {
            val cellForRow = layoutInflater.inflate(R.layout.rv_post_image, parent, false)
            return PostImageViewHolders(cellForRow)
        }

        val cellForRow = layoutInflater.inflate(R.layout.rv_post, parent, false)
        return CustomViewHolders(cellForRow)
    }

    override fun getItemCount(): Int {
        if (!savedPostList.isNullOrEmpty()) {
            return savedPostList.size
        } else
            return 0
    }

    fun getUserKey(holder: RecyclerView.ViewHolder): String {
        val post: Post = savedPostList[holder.adapterPosition]
        val postkey: String?= post.UserID

        return postkey!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
        val post: Post = savedPostList[position]
        if (holder is PostImageViewHolders) {
            val post: Post = savedPostList[position]
            holder.itemView.username.text = post.author
            holder.itemView.post_timestamp.text = post.Ptime


            if (post.uri != null) {
                Glide.with(mContext).load(post.uri).placeholder(R.color.white)
                    .into(holder.itemView.post_image)
                holder.itemView.post_title.text = post.title
                val userID = FirebaseAuth.getInstance().uid
                val ref = FirebaseDatabase.getInstance().getReference("users/$userID")
                ref.child("BlockedUsers").orderByValue().addListenerForSingleValueEvent( object :
                    ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            for (block in p0.children) {
                                if (block.value == post.author) {
                                    params.height = 0
                                    params.width = 0
                                    holder.itemView.layoutParams = params
                                }
                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
            } else {
                Glide.with(mContext).clear(holder.itemView.post_image)
                holder.itemView.post_image.setImageDrawable(null)
            }

            if (type == 0) {
                holder.itemView.username.text = post.subject
                holder.itemView.username.setOnClickListener {
                    val intent = Intent(mContext, CommunityPosts::class.java)
                    intent.putExtra("ClassName", post.subject)
                    mContext.startActivity(intent)

                }
            } else if (type == 1) {
                holder.itemView.username.setOnClickListener {
                    val intent = Intent(mContext, UserProfileActivity::class.java)
                    intent.putExtra("UserID", post.UserID)
                    intent.putExtra("Author", post.author)
                    mContext.startActivity(intent)
                }

            }
        } else {
            val userID = FirebaseAuth.getInstance().uid
            holder.itemView.post_title.text = post.title
            val ref = FirebaseDatabase.getInstance().getReference("users/$userID")
            ref.child("BlockedUsers").orderByValue().addListenerForSingleValueEvent( object :
                ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (block in p0.children) {
                            if (block.value == post.author) {
                                params.height = 0
                                params.width = 0
                                holder.itemView.layoutParams = params
                            }
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
            holder.itemView.username.text = post.author
            holder.itemView.post_timestamp.text = post.Ptime

            if (type == 0) {
                holder.itemView.username.text = post.subject
                holder.itemView.username.setOnClickListener {
                    val intent = Intent(mContext, CommunityPosts::class.java)
                    intent.putExtra("ClassName", post.subject)
                    mContext.startActivity(intent)

                }
            } else if (type == 1) {
                holder.itemView.username.setOnClickListener {
                    val intent = Intent(mContext, UserProfileActivity::class.java)
                    intent.putExtra("UserID", post.UserID)
                    intent.putExtra("Author", post.author)
                    mContext.startActivity(intent)
                }

            }
        }
        holder.itemView.setOnClickListener {
            //make it so you cant view that users profile?
            //user mcontext
            if (post.title == "no Posts" || post.title == "No Posts") {
                Log.d("Tag", "no post")
                //toast needed
            }
            else if(holder.itemView.post_title.text == "[blocked]"){
                Log.d("Tag","blocked post will not open to clicked post screen")
            }
            else {
                val intent = Intent(mContext, ClickedPost::class.java)
                intent.putExtra("Title", post.title)
                intent.putExtra("Text", post.text)
                intent.putExtra("Pkey", post.key)
                intent.putExtra("Classkey", post.Classkey)
                intent.putExtra("UserID", post.UserID)
                intent.putExtra("Author", post.author)
                intent.putExtra("crn", post.crn)
                intent.putExtra("uri", post.uri)
                intent.putExtra("subject", post.subject)
                intent.putExtra("Ptime", post.Ptime)
                mContext.startActivity(intent)
            }
        }
    }

    fun removeItem(holder: RecyclerView.ViewHolder): String {

        val post: Post = savedPostList[holder.adapterPosition]
        val postkey: String? = post.Classkey

        return postkey!!
    }



    fun getCrn(holder: RecyclerView.ViewHolder): String {

        val post: Post = savedPostList[holder.adapterPosition]
        val postcrn: String?= post.subject
        return postcrn!!
    }

    fun getTitle(holder: RecyclerView.ViewHolder): String {
        val post: Post = savedPostList[holder.adapterPosition]
        val posttitle: String?= post.title

        return posttitle!!
    }

    fun getAuthor(holder: RecyclerView.ViewHolder): String {
        val post: Post = savedPostList[holder.adapterPosition]
        val postauth: String?= post.author

        return postauth!!
    }

    fun getText(holder: RecyclerView.ViewHolder): String {
        val post: Post = savedPostList[holder.adapterPosition]
        val posttext: String? = post.text
        return posttext!!
    }

}

class CustomViewHolders(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

    override fun onClick(v: View) {
        Log.d("RecyclerView", "CLICK!")
    }

}

class PostImageViewHolders(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

    override fun onClick(v: View) {
        Log.d("RecyclerView", "CLICK!")
    }

}