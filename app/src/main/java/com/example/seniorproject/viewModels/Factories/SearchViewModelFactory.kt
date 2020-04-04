package com.example.seniorproject.viewModels.Factories


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.repositories.SearchRepo
import com.example.seniorproject.viewModels.SearchViewModel
//initialization of Search view model factory
@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val repository: SearchRepo):
    ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>) : T{
        return SearchViewModel(repository) as T
    }
}