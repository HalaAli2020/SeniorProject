package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.repositories.PostRepository

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory(private val repository: PostRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(repository) as T
    }
}