package com.example.seniorproject.data.models


class Reports(val accuserID: String, var accusedID: String, var complaintext: String, var crn: String, var classkey: String)
{
    //constructor() : this("","","", "", "")


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