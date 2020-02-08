package com.example.seniorproject.data.repositories

import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.Post
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import javax.inject.Inject

class PostRepository(private val Firebase: FirebaseData) {

    fun saveNewPost(Title: String, Text: String) = Firebase.saveNewPost(Title, Text)

    fun getSavedPosts() = Firebase.getSavedPost()


    companion object {
        @Volatile
        private var instance: PostRepository? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(firebasedata).also { instance = it }
            }
        //if the instance is not null
    }
}