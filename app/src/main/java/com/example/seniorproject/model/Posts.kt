package com.example.seniorproject.model

import java.sql.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Posts(private val txt: String?,private val courseID: Int?, private val uid: String?)
{
    private val database = FirebaseDatabase.getInstance()
    private var ptime: Long
    init {
        // backend will initalize values here or set to null if
        ptime = 1

    }



    fun gettxt() : String?
    {
        return txt
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