package com.example.seniorproject.MainForum.Adapters



import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
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

class HomeAdapter(context: Context, var savedPosts: List<Post>, var type: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val mContext: Context = context

    private val typeText: Int = 0
    private val typeImage: Int = 1

    override fun getItemViewType(position: Int): Int {
        if (savedPosts[position].uri == null || savedPosts[position].uri == "null") {
            return typeText
        }
        return typeImage
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        if (viewType == 1) {
            val cellForRow = layoutInflater.inflate(R.layout.rv_post_image, parent, false)
            return PostImageViewHolders(cellForRow)
        }else {
            val cellForRow = layoutInflater.inflate(R.layout.rv_post, parent, false)
            return CustomViewHolders(cellForRow)
        }

    }

    override fun getItemCount(): Int {
        if (!savedPosts.isNullOrEmpty()) {
            return savedPosts.size
        } else
            return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post: Post = savedPosts[position]
        if (holder is PostImageViewHolders) {
            val post: Post = savedPosts[position]
            val userID = FirebaseAuth.getInstance().uid

            holder.itemView.post_title.text = post.title
            holder.itemView.username.text = post.author
            holder.itemView.post_timestamp.text = post.Ptime

            if (post.uri != null) {
                Glide.with(mContext).load(post.uri).placeholder(R.color.white)
                    .into(holder.itemView.post_image)
                holder.itemView.post_title.text = post.title
                val ref = FirebaseDatabase.getInstance().getReference("users/$userID")
                ref.child("BlockedUsers").orderByValue().addListenerForSingleValueEvent( object :
                    ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            for (block in p0.children) {
                                if (block.value == post.userID) {
                                    holder.itemView.post_title.text = "[blocked]"
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
                if (post.title == "No Posts")
                {
                    holder.itemView.username.text = " "
                    holder.itemView.post_timestamp.text = " "
                    holder.itemView.imageView4.isInvisible
                }
                else
                {
                    holder.itemView.username.text = post.subject
                    holder.itemView.username.setOnClickListener {
                        val intent = Intent(mContext, CommunityPosts::class.java)
                        intent.putExtra("ClassName", post.subject)
                        mContext.startActivity(intent)
                    }

                }
            } else if (type == 1) {
                holder.itemView.username.setOnClickListener {
                    val intent = Intent(mContext, UserProfileActivity::class.java)
                    intent.putExtra("UserID", post.userID)
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
                            if (block.value == post.userID) {
                                holder.itemView.post_title.text = "[blocked]"
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
                    intent.putExtra("UserID", post.userID)
                    intent.putExtra("Author", post.author)
                    mContext.startActivity(intent)
                }

            }
        }
        holder.itemView.setOnClickListener {
            if (post.title == "no Posts" || post.title == "No Posts") {
                Log.d("Tag", "no post")
                //toast needed
            }
            else if(holder.itemView.post_title.text == "[blocked]"){
                //var pauth = post.author
                Log.d("Tag","blocked post will not open to clicked post screen")
            }
            else {
                val intent = Intent(mContext, ClickedPost::class.java)
                intent.putExtra("Title", post.title)
                intent.putExtra("Text", post.text)
                intent.putExtra("Pkey", post.key)
                intent.putExtra("Classkey", post.classkey)
                intent.putExtra("UserID", post.userID)
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
        val post: Post = savedPosts[holder.adapterPosition]
        val postkey: String? = post.classkey

        return postkey!!
    }

    fun getUserKey(holder: RecyclerView.ViewHolder): String {
        val post: Post = savedPosts[holder.adapterPosition]
        val postkey: String?= post.userID

        return postkey!!
    }


    fun getCrn(holder: RecyclerView.ViewHolder): String {
        val post: Post = savedPosts[holder.adapterPosition]
        val postcrn: String?= post.subject

        return postcrn!!
    }

    fun getTitle(holder: RecyclerView.ViewHolder): String {
        val post: Post = savedPosts[holder.adapterPosition]
        val posttitle: String?= post.title

        return posttitle!!
    }

    fun getAuthor(holder: RecyclerView.ViewHolder): String {

        val post: Post = savedPosts[holder.adapterPosition]
        val postauth: String?= post.author


        return postauth!!
    }

    fun getText(holder: RecyclerView.ViewHolder): String {

        val post: Post = savedPosts[holder.adapterPosition]
        val posttext: String? = post.text


        return posttext!!
    }

}




