package com.example.seniorproject.viewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.repositories.MessagesRepo
import com.example.seniorproject.viewModels.MessagesFragmentViewModel

@Suppress("UNCHECKED_CAST")
class MessagesFragmentViewModelFactory(private val repository: MessagesRepo) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessagesFragmentViewModel(repository) as T
    }
}