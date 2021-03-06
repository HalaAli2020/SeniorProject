package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import com.example.seniorproject.data.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.m_rv_chat_from_row.view.*
import kotlinx.android.synthetic.main.m_rv_chat_to_row.view.*

//Display Chat Log on ChatLog.kt
class ChatLogAdapter(context: Context, private val messageList: List<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //Initialize variables
    val mContext: Context = context
    private val toChat: Int = 0
    private val fromChat: Int = 1
    private val uid = FirebaseAuth.getInstance().uid

    //Determine if message was sent or received
    override fun getItemViewType(position: Int): Int {
        if (messageList[position].fromID == uid)
            return toChat
        return fromChat
    }

    //Display received message layout or send message layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == toChat) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val cellForRow = layoutInflater.inflate(R.layout.m_rv_chat_to_row, parent, false)
            return ToChatLogHolder(cellForRow)
        }

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.m_rv_chat_from_row, parent, false)
        return FromChatLogHolder(cellForRow)
    }

    //Grab number of messages
    override fun getItemCount(): Int {
        if (!messageList.isNullOrEmpty())
            return messageList.size
        return 0
    }

    //Bind text of messages to the layout
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message: ChatMessage = messageList[position]

        if (holder is ToChatLogHolder)
            holder.itemView.textview_to_Row.text = message.text
        else
            holder.itemView.textview_from_Row.text = message.text


        //val message: ChatMessage = messageList[position]
        //holder.itemView.textview_from_Row.text = message.text
        //Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.image_new_message)
        //Log.d("NewMessageAdapter", message.username)

    }
}

class ToChatLogHolder(v: View) : RecyclerView.ViewHolder(v)
class FromChatLogHolder(v: View) : RecyclerView.ViewHolder(v)













