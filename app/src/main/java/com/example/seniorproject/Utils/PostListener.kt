package com.example.seniorproject.Utils

import com.google.firebase.database.ValueEventListener

//interface used to with post relatd database queries
interface PostListener : ValueEventListener{
    //fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}