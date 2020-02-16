package com.example.seniorproject

interface DataListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}