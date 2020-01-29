package com.example.seniorproject.model

import java.sql.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.HashMap

@IgnoreExtraProperties
data class Posts(
    var text : String? = "",
    var uid : String? = "",
    var Ptime : Long? = 0,
    var commentID : String? = "")
{
    @Exclude
    fun toMap() : Map<String, Any?>
    {
        return mapOf(
            "text" to text,
            "uid" to uid,
            "Ptime" to Ptime,
            "commentID" to commentID
        )

    }
}
