package com.example.seniorproject.MainForum.Adapters


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seniorproject.MainForum.UserProfileActivity
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.rv_post_comment.view.*
import kotlinx.android.synthetic.main.rv_post_header.view.*

class CommentsListAdapter(
    var mContext: Context,
    var Comments: List<Comment>,
    var title: String,
    var text: String,
    var author: String,
    var crn: String,
    var asUserID: String,
    var ptime: String,
    var uri: String

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeHeader: Int = 0
    private val typeList: Int = 1
    val userID = FirebaseAuth.getInstance().uid
    /*
    this adapter is used to display a post and its comments when clicked
     */
    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return typeHeader
            //returns the post UI
        }
        return typeList
        //returns the comments list UI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        //inflate layout for both the post header and comment cardviews
        if (viewType == 0) {
            val cellForRow = layoutInflater.inflate(R.layout.rv_post_header, parent, false)
            return CustomViewHoldersHeader(cellForRow)
        }

        val cellForRow = layoutInflater.inflate(R.layout.rv_post_comment, parent, false)
        return CustomViewHolders(cellForRow)
    }

    //returns the item count
    override fun getItemCount(): Int {
        var size = 0
        if (!Comments.isNullOrEmpty()) {
            return Comments.size
        }

        return size
    }

    //
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Log.d("CommentsAdapter:", "" + position)
        if (holder is CustomViewHoldersHeader) {
            holder.itemView.click_post_title.text = title
            holder.itemView.click_post_text.text = text
            holder.itemView.community_name_TV.text = crn
            holder.itemView.author_name_TV.text = author

            if (uri != "null") {
                Glide.with(mContext)
                    .load(uri)
                    .placeholder(R.color.white)
                    .into(holder.itemView.post_image_onclick)
            }

            holder.itemView.posts_timestamp.text = ptime
            holder.itemView.author_name_TV.setOnClickListener {
                val intent = Intent(mContext, UserProfileActivity::class.java)
                intent.putExtra("UserID", asUserID)
                intent.putExtra("Author", author)
                mContext.startActivity(intent)

            }

        }
        //   }
        else {
            if (Comments[position] == null || getItemCount() == 0) {
                holder.itemView.comment_text.text = "No Comments yet"
            } else {
                val comment: Comment = Comments[position]
                val ref = FirebaseDatabase.getInstance().getReference("users/$userID")
                ref.child("BlockedUsers").orderByValue().addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            for (block in p0.children) {
                                if (block.getValue() == comment.PosterID) {
                                    holder.itemView.comment_text.text = "[blocked]"
                                }
                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
                //holder.itemView.post_title.text = comment.title
                holder.itemView.comment_text.text = comment.text
                holder.itemView.authcom.text = comment.author
                holder.itemView.comment_timestamp.text = comment.Ptime

                holder.itemView.authcom.setOnClickListener {
                    val intent = Intent(mContext, UserProfileActivity::class.java)
                    intent.putExtra("UserID", comment.PosterID)
                    intent.putExtra("Author", comment.author)
                    mContext.startActivity(intent)
                }

            }
        }
    }


    //the functions below are used to get information about comments in the recyclerview
    fun removeItem(holder: RecyclerView.ViewHolder): String {
        val comment: Comment = Comments[holder.adapterPosition]
        val commentkey: String? = comment.Classkey
        //returns comment classkey at selected position
        return commentkey!!
    }

    fun getCrn(holder: RecyclerView.ViewHolder): String {
        val comment: Comment = Comments[holder.adapterPosition]
        val commentkey: String? = comment.crn
        //returns comment crn at selected position
        return commentkey!!
    }

    fun getUserKey(holder: RecyclerView.ViewHolder): String {
        val comment: Comment = Comments[holder.adapterPosition]
        val commentkey: String? = comment.PosterID
        //returns the comment poster ID at the selected position
        return commentkey!!
    }

    fun getText(holder: RecyclerView.ViewHolder): String {
        val comment: Comment = Comments[holder.adapterPosition]
        val commentkey: String? = comment.text
        //returns the comment text at the select position
        return commentkey!!
    }

    fun getPostKey(holder: RecyclerView.ViewHolder): String {
        val comment: Comment = Comments[holder.adapterPosition]
        val commentkey: String? = comment.Postkey
        //retirns the post key at the selected position
        return commentkey!!
    }

    class CustomViewHolders(v: View) : RecyclerView.ViewHolder(v) {

    }


    class CustomViewHoldersHeader(v: View) : RecyclerView.ViewHolder(v) {

    }


}