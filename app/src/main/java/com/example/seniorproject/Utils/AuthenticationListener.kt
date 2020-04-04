package com.example.seniorproject.Utils

//interface to listen for the authentication state of a user
interface AuthenticationListener{
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}