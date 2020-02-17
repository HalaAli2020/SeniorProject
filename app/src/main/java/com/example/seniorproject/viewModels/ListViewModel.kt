package com.example.seniorproject.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class ListViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    var listClasses: MutableLiveData<String> = MutableLiveData()

    fun getClasses(): MutableLiveData<String>{
        listClasses = repository.getClasses()
        return listClasses
    }

}