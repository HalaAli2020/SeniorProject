package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import com.example.seniorproject.Profile.ProfileRepository

import com.example.seniorproject.data.models.PostLiveData
import javax.inject.Inject

private const val TAG = "MyLogTag"

class ProfileViewModel @Inject constructor(private val repository: ProfileRepository) : ViewModel(){

    var posts: PostLiveData = PostLiveData()

    fun getUserProfilePosts(): PostLiveData {

        posts = repository.getUserProfilePosts()
        return posts
    }
    //may need user
}