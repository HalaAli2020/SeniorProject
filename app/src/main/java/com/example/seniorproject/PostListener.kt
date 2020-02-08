package com.example.seniorproject

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


interface PostListener : ValueEventListener{
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}