package com.example.seniorproject.viewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.repositories.MessagesRepo
import com.example.seniorproject.viewModels.ChatLogViewModel

//initialization of chatlog viewmodel factory
@Suppress("UNCHECKED_CAST")
class ChatLogViewModelFactory(private val repository: MessagesRepo) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatLogViewModel(repository) as T
    }
}