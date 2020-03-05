package com.example.seniorproject.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.data.models.CommentLive

import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject



class ProfileViewModel @Inject constructor(private val repository: PostRepository) : ViewModel(){

    var posts: PostLiveData = PostLiveData()
    var comments: CommentLive = CommentLive()

    fun getUserProfilePosts(): PostLiveData {

        posts = repository.getUserProfilePosts()
        return posts
    }

    fun getUserProfileComments() : CommentLive {
        comments = repository.getUserProfileComments()
        return comments
    }

    fun uploadUserProfileImage(selectedPhotoUri: Uri) = repository.uploadUserProfileImage(selectedPhotoUri)

    fun currentUser() = repository.currentUser()

    var user = repository.currentUser()


}