package com.example.seniorproject.viewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.repositories.PostRepository
import com.example.seniorproject.viewModels.CommunityPostViewModel

@Suppress("UNCHECKED_CAST")
class CommunityPostViewModelFactory(private val repository: PostRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommunityPostViewModel(repository) as T
    }
}