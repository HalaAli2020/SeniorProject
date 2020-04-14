package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.seniorproject.Messages.ChatLog
import com.example.seniorproject.R
import com.example.seniorproject.data.models.User
import kotlinx.android.synthetic.main.m_rv_new_message.view.*

//Display Users to Message on NewMessage.kt
class NewMessageAdapter(context: Context, private var UserList: List<User>) : RecyclerView.Adapter<UserListHolder>() {

    //Initialized Variables
    val mContext: Context = context
    private var filterlist : MutableList<User> = UserList as MutableList<User>

    companion object {
        const val USER_KEY = "USER_KEY"
        const val USERNAME = "USERNAME"
        const val USER_PROF = "USER_PROF"
    }

    //Display using new message format for RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.m_rv_new_message, parent, false)
        return UserListHolder(cellForRow)
    }

    //Find item count
    override fun getItemCount(): Int {
        if (!filterlist.isNullOrEmpty())
            return filterlist.size
        return 0
    }

    //Bind data of new messages to the layout and set an OnClickListener for each one
    override fun onBindViewHolder(holder: UserListHolder, position: Int) {
        val user: User = filterlist[position]

        holder.itemView.usernameTextview.text = user.username ?: ""
        Log.d("NewMessageAdapter", user.username)

        if (user.profileImageUrl.toString().isBlank() || user.profileImageUrl.toString() == "null") {
            Glide.with(mContext) //1
                .load(R.drawable.ic_account_circle_blue_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop().fitCenter()
                .into(holder.itemView.image_new_message)
        } else {
            Glide.with(mContext) //1
                .load(user.profileImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop().fitCenter()
                .into(holder.itemView.image_new_message)
        }

        holder.itemView.newMessageUser.setOnClickListener {
            val intent = Intent(mContext, ChatLog::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(USER_KEY, user.uid)
            intent.putExtra(USERNAME, user.username)
            intent.putExtra(USER_PROF, user.profileImageUrl)
            mContext.startActivity(intent)
        }
    }

    //Display the search results
    fun onfilter(query: String?) {
        val nlist = mutableListOf<User>()
        if (query.isNullOrEmpty()) {
            filterlist = UserList as MutableList<User>
            notifyDataSetChanged()
        } else {


            for ((index, x) in UserList.withIndex()) {
                // query.contains()
                if (x.username!!.contains(query, true)) {
                    nlist.add(x)
                }
            }
            filterlist = nlist
            notifyDataSetChanged()
        }

    }
}

class UserListHolder(v: View) : RecyclerView.ViewHolder(v)












