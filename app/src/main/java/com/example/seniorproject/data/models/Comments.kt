package com.example.seniorproject.data.models

import com.example.seniorproject.R
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.rv_post_comment.view.*
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Comment(var text: String, var Ptime : String?, var PosterID : String?, var crn : String?, var Postkey : String?)
{
    constructor() : this("", null,"", "", "")

   // var PosterID : String? = null
    //var Ptime : Long? = null
    var Classkey : String? = null
    var UserComkey : String? = null
    var author : String? = null
    var ProfileComKey : String? = null
    var UserPostkey : String? = null

    init {
    var calendar: Calendar = Calendar.getInstance()
    var simple: SimpleDateFormat = SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a")
    Ptime = simple.format(calendar.time)
    }
    fun getLayout(): Int {
        return R.layout.rv_post_comment
    }

    fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.comment_text.text = text

    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "text" to text,
            "PosterID" to PosterID,
            "author" to author,
            "Ptime" to Ptime,
            "crn" to crn,
            "Postkey" to Postkey,
            "UserPostkey" to UserPostkey,
            "Classkey" to Classkey,
            "UserComkey" to UserComkey,
            "ProfileComKey" to ProfileComKey

        )

    }
}