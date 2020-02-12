package com.example.seniorproject.data

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

data class Post (val title: String, val text: String, val courseID: Int, val uid: String)
{
    constructor(): this("","",0,"")

    private val database = FirebaseDatabase.getInstance()
    private var ptime: Long
    init {
        // backend will initalize values here or set to null if
        ptime = 1

    }



    // should include database functions later on to correctly get information to fill class with proper query
}