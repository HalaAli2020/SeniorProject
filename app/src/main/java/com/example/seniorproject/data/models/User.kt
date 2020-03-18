package com.example.seniorproject.data.models

import android.net.Uri
import androidx.lifecycle.LiveData
import java.net.URI

data class User(var username: String?, var email: String?, var uid: String?, val profileImageUrl: Uri?
)
{
    constructor() : this("", "", "", Uri.EMPTY)

    var Posts = mutableListOf<Post>()
    //lateinit var Subscriptions : List<String>
    var Admin: Boolean = true
    var ProfilePic : Uri? = null
    //var Subscriptions : HashMap<String,String>? = hashMapOf()
    var Subscriptions: HashMap<String, String> = hashMapOf()

    init {


    }

    fun toMap(): Map<String?, Any?> {
        return mapOf(
            "Username" to username,
            "email" to email,
            "uid" to uid,
            "profileImageUrl" to profileImageUrl,
            "Admin" to Admin,
            "ProfilePicl" to ProfilePic
            //"Subscriptions" to Subscriptions,
            //"Posts" to Posts

        )

    }

}