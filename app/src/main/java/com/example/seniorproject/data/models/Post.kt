package com.example.seniorproject.data.models


import java.text.SimpleDateFormat
import java.util.*

//data class for user posts
data class Post(var title: String?, var text: String?, var subject: String?, var Ptime : String?)
{

    constructor(): this("","", "", null)
    private  var comments : List<Comment> = emptyList()
    var author : String? = null
    var crn : String? = null
    var Classkey : String? = null
    var UserID : String? = null
    var key : String? = null
    var uri : String? = null
    var imagePost : Boolean? = null

    init {
        val calendar: Calendar = Calendar.getInstance()
            val simple: SimpleDateFormat = SimpleDateFormat("M-d-yy 'at' h:mm a")
        Ptime = simple.format(calendar.time)
    }

    //mapping of post data to the Firebase database
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "text" to text,
            "Ptime" to Ptime,
            "author" to author,
            "subject" to subject,
            "crn" to crn,
            "comments" to comments,
            "UserID" to UserID,
            "Classkey" to Classkey,
            "key" to key,
            "uri" to uri,
            "imagePost" to imagePost

        )

    }

}