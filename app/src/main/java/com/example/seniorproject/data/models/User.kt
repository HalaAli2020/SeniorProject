package com.example.seniorproject.data.models

import androidx.lifecycle.LiveData

data class User(val username: String?, val email: String?, val uid: String?)
{
    constructor(): this("","","")
     var Posts  = mutableListOf<Post>()
    //lateinit var Subscriptions : List<String>
    var Admin : Boolean = true
    var Subscriptions : HashMap<String,String> = hashMapOf()
    init {


    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "Username" to username,
            "email" to email,
            "uid" to uid,
            "Admin" to Admin,
            "Subscriptions" to Subscriptions,
            "Posts" to Posts

        )

    }

}
