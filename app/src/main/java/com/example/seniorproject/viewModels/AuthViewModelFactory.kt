package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.repositories.UserAuthRepo

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val repository: UserAuthRepo) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthenticationViewModel(repository) as T
    }
}