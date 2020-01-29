package com.example.seniorproject.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostDAO()

    private val database = FirebaseDatabase.getInstance().reference.child("Users")
    


    private fun createUserData(user : User)
    {
       // use auth data to get uid for this database search
        //database.child(user.()).setValue(user)
    }
    private fun getUserData(user: User) {
        /*val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userback = dataSnapshot.getValue(User::class.java)

            }

         need to be re-written
        */
    }

