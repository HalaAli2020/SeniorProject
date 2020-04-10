package com.example.seniorproject.data.models

import com.example.seniorproject.R
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.rv_post_comment.view.*
import java.text.SimpleDateFormat
import java.util.*

//data class for user comments
class Comment(var text: String, var Ptime : String?, var PosterID : String?, var crn : String?, var Postkey : String?)
{
    constructor() : this("", null,"", "", "")

    var Classkey : String? = null
    var UserComkey : String? = null
    var author : String? = null
    var ProfileComKey : String? = null
    private var userPostKey : String? = null

    //initialization of timestamp
    init {
    val calendar: Calendar = Calendar.getInstance()
        val simple: SimpleDateFormat = SimpleDateFormat("M-d-yy h:mm a")
    Ptime = simple.format(calendar.time)
    }

    fun getLayout(): Int {
        return R.layout.rv_post_comment
    }

    fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.comment_text.text = text

    }

    //mapping of comment data to database
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "text" to text,
            "PosterID" to PosterID,
            "author" to author,
            "Ptime" to Ptime,
            "crn" to crn,
            "Postkey" to Postkey,
            "UserPostkey" to userPostKey,
            "Classkey" to Classkey,
            "UserComkey" to UserComkey,
            "ProfileComKey" to ProfileComKey

        )

    }
}