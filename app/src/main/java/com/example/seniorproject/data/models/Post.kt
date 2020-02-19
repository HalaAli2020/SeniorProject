package com.example.seniorproject.data.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.R
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

import kotlinx.android.synthetic.main.post_rv.view.*

data class Post(val title: String?, val text: String?, val subject: String?)
{
    //val title: String, val text: String, val courseID: Int, val uid: String
    constructor(): this("","", "CSC1500")

    private val database = FirebaseDatabase.getInstance()
    private var ptime: Long
    init {
        // backend will initalize values here or set to null if
        ptime = 1

    }

     fun getLayout(): Int {
        return R.layout.post_rv
    }

     fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.post_text.text = text
        viewHolder.itemView.post_title.text = title
    }


    // should include database functions later on to correctly get information to fill class with proper query
}