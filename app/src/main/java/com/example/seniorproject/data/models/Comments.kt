package com.example.seniorproject.data.models

class Comment(val text: String, var Ptime : Long?, var PosterID : String?, var Classkey : String? = null, var Postkey : String?)
{
    constructor() : this("", null,"", "", "")
   // var PosterID : String? = null
    //var Ptime : Long? = null
    //var Classkey : String? = null
    //var Postkey : String? = null


    init {
        Ptime = System.currentTimeMillis()
    }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "text" to text,
            "PosterID" to PosterID,
            "Ptime" to Ptime,
            "Postkey" to Postkey,
            "Classkey" to Classkey

        )

    }
}