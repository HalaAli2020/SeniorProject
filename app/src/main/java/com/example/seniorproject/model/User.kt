package com.example.seniorproject.model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.HashMap


@IgnoreExtraProperties
data class User(var username: String? = "",
                var email: String? = "",
                var Fname: String? ="",
                var Lname: String? = "")
{
    @Exclude
    fun toMap() : Map<String, Any?>{
        return mapOf(
            "username" to username,
            "email" to email,
            "Fname" to Fname,
            "Lname" to Lname

        )
    }


}
