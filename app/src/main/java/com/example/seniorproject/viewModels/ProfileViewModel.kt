package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel

import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

private const val TAG = "MyLogTag"

class ProfileViewModel @Inject constructor(private val repository: PostRepository) : ViewModel(){

    var posts: PostLiveData = PostLiveData()

    fun getUserProfilePosts(): PostLiveData {

        posts = repository.getUserProfilePosts()
        return posts
    }


    fun fetchCurrentUserName() = repository.fetchCurrentUserName()

    fun currentUser() = repository.currentUser()
}