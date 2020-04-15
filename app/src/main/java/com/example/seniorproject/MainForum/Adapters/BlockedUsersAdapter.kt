package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.R
import kotlinx.android.synthetic.main.rv_unblock_users.view.*


class BlockedUsersAdapter(var context: Context, var blockedUsersList: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.rv_unblock_users, parent, false)
        return CustomViewHolders(cellForRow)
    }

    override fun getItemCount(): Int {
        if (!blockedUsersList.isNullOrEmpty()) {
            return blockedUsersList.size
        } else
            return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.blockedUserName.text = blockedUsersList[position]

    }
    
    fun removeItem(holder: RecyclerView.ViewHolder): String {
        val user: String = blockedUsersList[holder.adapterPosition]
        return user
    }
}
