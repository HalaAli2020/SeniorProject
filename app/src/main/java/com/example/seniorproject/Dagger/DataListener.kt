package com.example.seniorproject.Dagger

interface DataListener {
    //fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}