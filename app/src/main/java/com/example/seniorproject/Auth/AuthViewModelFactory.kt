package com.example.seniorproject.Auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.Firebase.FirebaseData


@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory (
    private val repository: FirebaseData
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return Auth_view_model(repository) as T
    }
}