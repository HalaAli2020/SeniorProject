package com.example.seniorproject.model

import java.sql.Timestamp

class Parentpost()
{
    private var txt: String
    private var courseID : Int
    private var commentID : Int?
    private var PTime: Timestamp
    private var userID : Int
    init {
        // backend will initalize values here or set to null if
        txt = "A"
        courseID = 1
        commentID = null
        PTime = Timestamp(System.currentTimeMillis())
        userID = 1
    }

    fun newPost(t: String, CourseID : Int, ID : Int)
    {
        txt = t
        PTime = Timestamp(System.currentTimeMillis())
        userID = ID
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
    fun getPtime(): Timestamp
    {
        return PTime
    }
    fun getuserID(): Int
    {
        return userID
    }
    // should include database functions later on to correctly get information to fill class with proper query
}