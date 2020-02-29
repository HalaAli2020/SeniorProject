package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.Profile.ProfileRepository


@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(private val repository: ProfileRepository):
    ViewModelProvider.NewInstanceFactory() {

    override fun <T:ViewModel?> create(modelClass: Class<T>) : T{
        return ProfileViewModel(repository) as T
    }
}



