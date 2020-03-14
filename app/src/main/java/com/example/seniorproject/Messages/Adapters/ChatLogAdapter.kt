package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Messages.ChatLog
import com.example.seniorproject.R
import com.example.seniorproject.data.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.m_rv_new_message.view.*


class ChatLogAdapter(
    context: Context,
    private val UserList: List<User>
) :
    RecyclerView.Adapter<ChatLogHolder>() {

    val mContext: Context = context

    companion object{
        val USER_KEY = "USER_KEY"
        val USERNAME = "USERNAME"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatLogHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.m_rv_new_message, parent, false)
        return ChatLogHolder(cellForRow)
    }

    override fun getItemCount(): Int {

        if (!UserList.isNullOrEmpty()) {
            return UserList.size
        }

        return 0
    }

    override fun onBindViewHolder(holder: ChatLogHolder, position: Int) {

        val user: User = UserList[position]

        holder.itemView.usernameTextview.text = user.username
        //Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.image_new_message)
        Log.d("NewMessageAdapter", user.username)
        Picasso.get().load(user.profileImageUrl).into(holder.itemView.image_new_message)
        holder.itemView.newMessageUser.setOnClickListener {
            val intent = Intent(mContext, ChatLog::class.java)
            intent.putExtra(USER_KEY, user.uid)
            intent.putExtra(USERNAME, user.username)
            mContext.startActivity(intent)
        }

    }
}

class ChatLogHolder(v: View) : RecyclerView.ViewHolder(v)












