package com.example.seniorproject.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel

import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject



class ProfileViewModel @Inject constructor(private val repository: PostRepository) : ViewModel(){

    var posts: PostLiveData = PostLiveData()

    fun getUserProfilePosts(): PostLiveData {

        posts = repository.getUserProfilePosts()
        return posts
    }

    fun uploadUserProfileImage(selectedPhotoUri: Uri) = repository.uploadUserProfileImage(selectedPhotoUri)

    fun currentUser() = repository.currentUser()

    var user = repository.currentUser()
}