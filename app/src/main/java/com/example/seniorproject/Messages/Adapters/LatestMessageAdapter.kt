package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.seniorproject.Messages.ChatLog
import com.example.seniorproject.R
import com.example.seniorproject.data.models.LatestMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.m_rv_latest_message.view.*

//Display Latest Messages on FragmentLatestMessages
class LatestMessageAdapter(context: Context, private val messageList: List<LatestMessage>) : RecyclerView.Adapter<RecentMessageHolder>() {

    //Initialized varibles
    val mContext: Context = context

    companion object {
        const val USER_KEY = "USER_KEY"
        const val USERNAME = "USERNAME"
        const val USER_PROF = "USER_PROF"
    }

    //Display using latest messages format for RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentMessageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.m_rv_latest_message, parent, false)
        return RecentMessageHolder(cellForRow)
    }

    //Get number of items in latest messages list
    override fun getItemCount(): Int {
        if (!messageList.isNullOrEmpty())
            return messageList.size
        return 0
    }

    //Bind data of latest messages to the layout and define an OnClickListener for each item
    override fun onBindViewHolder(holder: RecentMessageHolder, position: Int) {
        val message: LatestMessage = messageList[position]


        holder.itemView.textView_latest_user.text = message.username
        holder.itemView.textView_latest_message.text = message.text


        Glide.with(holder.itemView.context)
            .load(Uri.parse(message.profileImage))
            .placeholder(R.drawable.ic_account_circle_blue_24dp)
            .error(R.drawable.ic_account_circle_blue_24dp)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop().fitCenter()
            .into(holder.itemView.imageView_incoming_user)



        holder.itemView.listOfUsers.setOnClickListener {
            val intent = Intent(mContext, ChatLog::class.java)

            if (message.fromID == FirebaseAuth.getInstance().uid)
                intent.putExtra(USER_KEY, message.toId)
            else
                intent.putExtra(USER_KEY, message.fromID)

            intent.putExtra(USERNAME, message.username)
            intent.putExtra(USER_PROF, message.profileImage)
            holder.itemView.context.startActivity(intent)
        }

    }
}

class RecentMessageHolder(v: View) : RecyclerView.ViewHolder(v)












