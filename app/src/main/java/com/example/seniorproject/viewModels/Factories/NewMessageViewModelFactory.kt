package com.example.seniorproject.viewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.repositories.MessagesRepo
import com.example.seniorproject.viewModels.NewMessageViewModel

//initialization of New Messages view model factory
@Suppress("UNCHECKED_CAST")
class NewMessageViewModelFactory(private val repository: MessagesRepo) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewMessageViewModel(repository) as T
    }
}