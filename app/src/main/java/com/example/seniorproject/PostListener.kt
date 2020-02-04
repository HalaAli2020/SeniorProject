package com.example.seniorproject

interface PostListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}