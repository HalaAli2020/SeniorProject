package com.example.seniorproject.Utils

interface AuthenticationListener{
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}