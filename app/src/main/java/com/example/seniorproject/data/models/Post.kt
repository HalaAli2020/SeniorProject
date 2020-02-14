package com.example.seniorproject.data.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.R
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

import kotlinx.android.synthetic.main.post_rv.view.*

data class Post(var title: String?, var text: String?)
{
    //val title: String, val text: String, val courseID: Int, val uid: String
    constructor(): this("","")
    private lateinit var comments : MutableList<Comment>
    private val database = FirebaseDatabase.getInstance()
     var author : String? = null
     var subject : String? = null
     var crn : String? = null
     var ptime: Long? = null
    var Classkey : String? = null
    var Userkey : String? = null
    init {
        // backend will initalize values here or set to null if
       // ptime = System.currentTimeMillis()


    }

     fun getLayout(): Int {
        return R.layout.post_rv
    }

     fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.post_text.text = text
        viewHolder.itemView.post_title.text = title
    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "text" to text,
            "Time" to ptime,
            "Author" to author,
            "subject" to subject,
            "crn" to crn,
            "comments" to comments,
            "UserKey" to Userkey,
            "Classkey" to Classkey

        )

    }
    // should include database functions later on to correctly get information to fill class with proper query
}