package com.example.seniorproject.viewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.repositories.PostRepository
import com.example.seniorproject.viewModels.ClickedPostViewModel
import com.example.seniorproject.viewModels.CommunityPostViewModel

//initialization of CommunityPost view model factory
@Suppress("UNCHECKED_CAST")
class ClickedPostViewModelFactory(private val repository: PostRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ClickedPostViewModel(repository) as T
    }
}