package com.example.seniorproject.data.repositories

import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.Post
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PostRepository @Inject constructor(private val Firebase: FirebaseData) {

    fun saveNewPost(Title: String, Text: String, Subject: String) = Firebase.saveNewPost(Title, Text, Subject)

    fun getSavedPosts() = Firebase.getSavedPost()

    fun currentUser() = Firebase.CurrentUser()

    fun fetchCurrentUserName() = Firebase.fetchCurrentUserName()

    fun getClasses() = Firebase.getClasses()

    fun getClassPosts(className: String) = Firebase.getClassPosts(className)

    companion object {
        @Volatile
        private var instance: PostRepository? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(firebasedata).also { instance = it }
            }
    }
}