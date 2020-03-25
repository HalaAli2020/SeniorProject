package com.example.seniorproject.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.se.omapi.Session
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.CommentLive

import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject



class ProfileViewModel @Inject constructor(private val repository: PostRepository) : ViewModel(){

    var posts: PostLiveData = PostLiveData()
    var username : LiveData<String>? = null
    var email : LiveData<String>? = null
    var comments : CommentLive = CommentLive()
    var RepoUser : MutableLiveData<User>? = null
    var SessionUser = repository.SessionUser()
   var CommentListener : PostListener? = null
    private var PostKey : String? = null
    val CommentListeners : PostListener? = null
    val userbio : String = fetchBio(FirebaseAuth.getInstance().currentUser?.uid ?: "null")



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

    fun currentUser()
    {
        RepoUser = repository.SessionUser()
    }

    fun saveNewUsername(username: String) = repository.saveNewUsername(username)

    var user = repository.currentUser()

    fun fetchEmail(UserID: String) = repository.fetchEmail(UserID)
    //fun getEmail() = repository.getEmail()

    fun getclassnamesforusername() = repository.getclassnamesforusername()

    fun sendClassnameForUsername() = repository.sendClassnameForUsername()

    fun saveUserbio(bio : String) = repository.saveUserbio(bio)

    fun fetchBio(UserID: String) = repository.fetchBio(UserID)

    fun fetchCurrentBio() = repository.fetchCurrentBio()

    fun noPostsChecker(UserID: String) = repository.noPostsChecker(UserID)

    fun noCommentsChecker(UserID: String) = repository.noCommentsChecker(UserID)

}