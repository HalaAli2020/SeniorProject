package com.example.seniorproject.model

import java.sql.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Posts()
{
    private val database = FirebaseDatabase.getInstance()
    private var txt: String
    private var courseID : Int
    private var commentID : Int?
    private var ptime: Long
    private var uid : String?
    init {
        // backend will initalize values here or set to null if
        txt = "A"
        courseID = 1
        commentID = null
        ptime = 1
        ptime = 1
        uid = "1"
    }

    fun newPost(t: String, CourseID : Int, ID : String?)
    {
        txt = t
        ptime = 1
        uid = ID
        courseID = CourseID


    }

    fun gettxt() : String
    {
        return txt
    }
    fun getcousreID() : Int
    {
        return courseID
    }
    fun getcommentID() : Int?
    {
        return commentID
    }
    fun getPtime(): Long
    {
        return ptime
    }
    fun getuserID(): String?
    {
        return uid
    }
    // should include database functions later on to correctly get information to fill class with proper query
}