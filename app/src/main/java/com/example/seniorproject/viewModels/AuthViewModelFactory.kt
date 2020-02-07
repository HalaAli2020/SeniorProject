package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.repositories.UserAuthRepo
import javax.inject.Inject
import javax.inject.Provider


class AuthViewModelFactory @Inject constructor( private val myviewmodelprovider:Provider<AuthenticationViewModel>):ViewModelProvider.Factory {

    @Suppress("UNCHECK_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return myviewmodelprovider.get() as T
    }
}

