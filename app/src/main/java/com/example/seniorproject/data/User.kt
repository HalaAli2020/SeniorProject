package com.example.seniorproject.data

import androidx.lifecycle.LiveData

data class User(val username: String?, val email: String?, val uid: String?)
{
    constructor(): this("","","")

    private fun backendID() : Int
    {
        // function will use backend to get user id
        return 1
    }

   // fun getuid() = email as LiveData<String>

}
