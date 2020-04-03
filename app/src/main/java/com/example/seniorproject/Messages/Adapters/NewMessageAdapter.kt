package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Messages.ChatLog
import com.example.seniorproject.R
import com.example.seniorproject.data.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.m_rv_new_message.view.*


class NewMessageAdapter(
    context: Context,
    private var UserList: List<User>
    ) :
    RecyclerView.Adapter<UserListHolder>() {

    val mContext: Context = context
    private var filterlist : MutableList<User> = UserList as MutableList<User>

    companion object{
        const val USER_KEY = "USER_KEY"
        const val USERNAME = "USERNAME"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.m_rv_new_message, parent, false)
        return UserListHolder(cellForRow)
    }

    override fun getItemCount(): Int {

        if (!filterlist.isNullOrEmpty()) {
            return filterlist.size
        }

        return 0
    }

    override fun onBindViewHolder(holder: UserListHolder, position: Int) {

        val user: User = filterlist[position]

        holder.itemView.usernameTextview.text = user.username ?: "no username"
        //Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.image_new_message)
        Log.d("NewMessageAdapter", user.username)
        //Picasso.get().load(user.profileImageUrl).into(holder.itemView.image_new_message)

        if(user.profileImageUrl.toString().isBlank() || user.profileImageUrl.toString()=="null"){
            holder.itemView.image_new_message.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_account_circle_blue_24dp))
        }
        else
        {
            Picasso.get().load(user.profileImageUrl).into(holder.itemView.image_new_message)
        }

        holder.itemView.newMessageUser.setOnClickListener {
            val intent = Intent(mContext, ChatLog::class.java)
            intent.putExtra(USER_KEY, user.uid)
            intent.putExtra(USERNAME, user.username)
            mContext.startActivity(intent)
        }

    }
    fun onfilter(query : String?)
    {
        val nlist = mutableListOf<User>()
        if(query.isNullOrEmpty())
        {
             filterlist = UserList as MutableList<User>
            notifyDataSetChanged()
        }
        else
        {



            for ((index, x) in UserList.withIndex())
            {
                // query.contains()
                if(x.username!!.contains(query, true))
                {
                    nlist.add(x)
                }
            }
            filterlist = nlist
            notifyDataSetChanged()
        }

    }
}

class UserListHolder(v: View) : RecyclerView.ViewHolder(v)












