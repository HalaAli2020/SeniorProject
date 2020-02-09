package com.example.seniorproject.Utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


interface PostListener : ValueEventListener{
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}