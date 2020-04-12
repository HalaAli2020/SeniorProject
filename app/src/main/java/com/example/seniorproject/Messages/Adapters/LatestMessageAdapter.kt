package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.m_rv_latest_message.view.*

//Display Latest Messages on FragmentLatestMessages
class LatestMessageAdapter(context: Context, private val messageList: List<LatestMessage>) : RecyclerView.Adapter<RecentMessageHolder>() {

    //Initialized varibles
    val mContext: Context = context
    companion object {
        const val USER_KEY = "USER_KEY"
        const val USERNAME = "USERNAME"
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
            .load(R.drawable.ic_account_circle_blue_100dp)
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
            holder.itemView.context.startActivity(intent)
        }

    }
}

class RecentMessageHolder(v: View) : RecyclerView.ViewHolder(v)












