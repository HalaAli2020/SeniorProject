package com.example.seniorproject.data.models

import com.example.seniorproject.R
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.rv_post_comment.view.*

class Reports(val accuserID: String, var accusedID: String, var complaintext: String, var crn: String, var classkey: String)
{
    constructor() : this("","","", "", "")


    fun toMap(): Map<String, Any?> {
        return mapOf(
            "complaintext" to complaintext,
            "accuserID" to accuserID,
            "accusedID" to accusedID,
            "crn" to crn,
            "classkey" to classkey
        )

    }
}