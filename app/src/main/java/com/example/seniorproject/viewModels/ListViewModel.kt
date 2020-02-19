package com.example.seniorproject.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class ListViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private var listClasses: MutableLiveData<List<String>> = MutableLiveData()

    private fun getClasses(){
        listClasses = repository.getClasses()
    }

    fun returnClasses(): MutableLiveData<List<String>>{
        getClasses()
        return listClasses
    }
}