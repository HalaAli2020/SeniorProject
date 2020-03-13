package com.example.seniorproject.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.CommentLive

import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject



class ProfileViewModel @Inject constructor(private val repository: PostRepository) : ViewModel(){

    var posts: PostLiveData = PostLiveData()
    var comments: CommentLive = CommentLive()
    private var PostKey : String? = null
    val CommentListener : PostListener? = null



    fun getUserProfilePosts(UserID : String): PostLiveData {

        posts = repository.getUserProfilePosts(UserID)
        return posts
    }

    fun getUserProfileComments(UserID : String) : CommentLive {
        comments = repository.getUserProfileComments(UserID)
        return comments
    }

    fun deletePost(Classkey: String, crn: String, userID: String)
    {

        repository.deleteNewPost(Classkey, crn, userID)
    }

    fun deleteCommentFromUserProfile(PKey: String, crn: String, Classkey: String, userID: String)
    {
        if(PKey.isNullOrEmpty())
        {
            PostKey = PKey
            CommentListener?.onFailure("Post key not found")

        }
        repository.deleteNewCommentFromUserProfile(PKey!!, crn!!, Classkey!!, userID!!)
    }

    fun deleteCommentFromCommPosts(PKey: String, crn: String, Classkey: String)
    {
        if(PKey.isNullOrEmpty())
        {
            PostKey = PKey
            CommentListener?.onFailure("Post key not found")

        }
        repository.deleteNewCommentFromCommPosts(PKey!!, crn!!, Classkey!!)
    }

    fun uploadUserProfileImage(selectedPhotoUri: Uri) = repository.uploadUserProfileImage(selectedPhotoUri)

    fun currentUser() = repository.currentUser()

    var user = repository.currentUser()

    fun fetchEmail(UserID: String) = repository.fetchEmail(UserID)
    //fun getEmail() = repository.getEmail()


}