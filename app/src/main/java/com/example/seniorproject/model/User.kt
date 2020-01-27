package com.example.seniorproject.model

import com.google.firebase.auth.FirebaseUser

data class User(private val username: String?, private val email: String?, private val uid: String?)
{


    fun getUID(): String? {
        return uid
    }
    fun getUserName(): String?{
        return username
    }
    fun getemail(): String?{
        return email
    }

    private fun backendID() : Int
    {
        // function will use backend to get user id
        return 1
    }


}
