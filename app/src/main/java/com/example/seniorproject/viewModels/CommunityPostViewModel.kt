package com.example.seniorproject.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class CommunityPostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private var listClasses: PostLiveData = PostLiveData()
    private lateinit var className: String

    private fun getClasses(className: String){
        listClasses = repository.getClassPosts(className)
    }

    fun returnClassPosts(className: String): PostLiveData {
        getClasses(className)
        return listClasses
    }



}