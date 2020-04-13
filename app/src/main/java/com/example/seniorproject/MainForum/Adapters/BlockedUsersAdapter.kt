package com.example.seniorproject.MainForum.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.UnblockUserActivity
import com.example.seniorproject.MainForum.UserProfileActivity
import com.example.seniorproject.R
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.viewModels.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.rv_unblock_users.*
import kotlinx.android.synthetic.main.rv_unblock_users.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.lifecycle.ViewModelProvider as ViewModelProvider1

class BlockedUsersAdapter(var context: Context, var blockedUsersList: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.rv_unblock_users, parent, false)
        return CustomListViewHolders(cellForRow)
    }

    override fun getItemCount(): Int {
        if (!blockedUsersList.isNullOrEmpty()) {
            return blockedUsersList.size
        } else
            return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.blockedUserName.text = blockedUsersList[position]

        holder.itemView.Unblock.setOnClickListener {
            val userID = FirebaseAuth.getInstance().uid
            val ref = FirebaseDatabase.getInstance().getReference("users/$userID")
            ref.child("BlockedUsers").orderByValue().addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (!p0.exists()) {
                    }
                    if (p0.exists()) {
                        for (block in p0.children) {
                            if (block.value == holder.itemView.blockedUserName.text) {
                                block.ref.removeValue()
                                val toast = Toast.makeText(it.context, "This user is removed from your blocked list.", Toast.LENGTH_SHORT)
                                val intent = Intent(context, UnblockUserActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                context.startActivity(intent)
                                toast.show()
                            }
                        }
                    }
                    ref.removeEventListener(this)
                }

                override fun onCancelled(p0: DatabaseError) {
                    ref.removeEventListener(this)
                }
            })
        }
    }

}