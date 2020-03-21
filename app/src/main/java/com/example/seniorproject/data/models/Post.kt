package com.example.seniorproject.data.models

import com.example.seniorproject.R
import java.text.SimpleDateFormat
import java.util.*

data class Post(var title: String?, var text: String?, val subject: String?)
{
    //val title: String, val text: String, val courseID: Int, val uid: String
    constructor(): this("","", "")
    private  var comments : List<Comment> = emptyList()
    //private val database = FirebaseDatabase.getInstance()
    var author : String? = null
    //var subject : String? = null
    var crn : String? = null
    var Ptime: String? = null
    var Classkey : String? = null
    var UserID : String? = null
    var key : String? = null

        init {
            var calendar: Calendar = Calendar.getInstance()
            var simple: SimpleDateFormat = SimpleDateFormat("MM-dd-yyyy hh:mm:ss a")
            Ptime = simple.format(calendar.time)
        }



    fun getLayout(): Int {
        return R.layout.rv_post
    }

   /* fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.post_text.text = text
        viewHolder.itemView.post_title.text = title
    }*/
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "text" to text,
            "Time" to Ptime,
            "author" to author,
            "subject" to subject,
            "crn" to crn,
            "comments" to comments,
            "UserID" to UserID,
            "Classkey" to Classkey,
            "key" to key

        )

    }
    // should include database functions later on to correctly get information to fill class with proper query
}