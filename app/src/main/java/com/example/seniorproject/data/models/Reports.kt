package com.example.seniorproject.data.models

//model class for reporting a user
class Reports(private val accuserID: String, var accusedID: String, var complaintext: String, var crn: String, var classkey: String)
{
    //constructor() : this("","","", "", "")


    //mapping of report data to the database
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "complaintext" to complaintext,
            "accuserID" to accuserID,
            "accusedID" to accusedID,
            "crn" to crn,
            "classkey" to classkey
        )

    }
}