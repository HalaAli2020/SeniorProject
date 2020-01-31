package com.example.seniorproject

interface AuthenticationListener{
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}