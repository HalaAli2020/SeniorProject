package com.example.seniorproject.data.models

import java.text.SimpleDateFormat
import java.util.*

//data class for user posts
data class Post(
    var title: String?,
    var text: String?,
    var subject: String?,
    var Ptime: String?,
    var author: String?,
    var crn: String?,
    var Classkey: String?,
    var UserID: String?,
    var key: String?,
    var uri: String?,
    var imagePost: Boolean?
) {

    constructor() : this("", "", "", null, null, null, null, null, null, null, null)

    private var comments: List<Comment> = emptyList()

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