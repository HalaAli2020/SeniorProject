package com.example.seniorproject.Utils

//callback interface used by most of the larger database queries to get user post data
interface Callback {
    fun onCallback(value: ArrayList<String>)
}