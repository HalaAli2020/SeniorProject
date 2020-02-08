package com.example.seniorproject.Utils

interface PostListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}