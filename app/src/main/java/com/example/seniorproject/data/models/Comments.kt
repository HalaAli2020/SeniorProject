package com.example.seniorproject.data.models

import com.example.seniorproject.R
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.comment_rv.view.*
import kotlinx.android.synthetic.main.post_rv.view.*

class Comment(val text: String, var Ptime : Long?, var PosterID : String?, var crn : String?, var Postkey : String?)
{
    constructor() : this("", null,"", "", "")

   // var PosterID : String? = null
    //var Ptime : Long? = null
    var ClassComkey : String? = null
    var UserComkey : String? = null



    init {
        Ptime = System.currentTimeMillis()
    }
    fun getLayout(): Int {
        return R.layout.comment_rv
    }

    fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.comment_text.text = text

    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "text" to text,
            "PosterID" to PosterID,
            "Ptime" to Ptime,
            "crn" to crn,
            "Postkey" to Postkey,
            "Classkey" to ClassComkey,
            "UserComkey" to UserComkey

        )

    }
}