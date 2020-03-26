package com.example.seniorproject.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.CommentLive

import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import javax.inject.Inject



class ProfileViewModel @Inject constructor(private val repository: PostRepository) : ViewModel(){

    var posts: PostLiveData = PostLiveData()
    var comments: CommentLive = CommentLive()
    private var PostKey : String? = null
    val CommentListener : PostListener? = null
    //val userbio : String = fetchBio(FirebaseAuth.getInstance().currentUser?.uid ?: "null")
    var otherEmail : String = "no email available"
    var otherBio : String = "no bio available"


    fun getUserProfilePosts(UserID : String): PostLiveData {

        posts = repository.getUserProfilePosts(UserID)
        return posts
    }
    fun returnProfilePost() : PostLiveData
    {
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

    fun saveNewUsername(username: String) = repository.saveNewUsername(username)

    var user = repository.currentUser()

    fun fetchEmail(UserID: String, callback : EmailCallback) : String {
        repository.fetchEmail(UserID, object : PostRepository.FirebaseCallbackItem{
            override fun onStart() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onMessage(data: DataSnapshot): String {
                val email = data.child("email").getValue(String::class.java)
                otherEmail = email ?: "no email in success"
                callback.getEmail(otherEmail)
                return otherEmail
            }
        })
        return otherEmail
    }

    fun fetchBio(UserID: String, callback: EmailCallback) : String {
        repository.fetchBio(UserID, object : PostRepository.FirebaseCallbackItem{
            override fun onStart() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onMessage(data: DataSnapshot): String {
                val bio = data.child("UserBio").getValue(String::class.java) ?: "no bio"
                otherBio = bio
                callback.getEmail(otherBio)
                return otherBio
            }

        })
        return otherBio
    }


    fun getclassnamesforusername() = repository.getclassnamesforusername()

    fun sendClassnameForUsername() = repository.sendClassnameForUsername()

    fun saveUserbio(bio : String) = repository.saveUserbio(bio)

    fun fetchCurrentBio() = repository.fetchCurrentBio()

    fun noPostsChecker(UserID: String) = repository.noPostsChecker(UserID)

    fun noCommentsChecker(UserID: String) = repository.noCommentsChecker(UserID)

}