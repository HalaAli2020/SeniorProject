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

    fun saveNewPost(post : Post) = Firebase.saveNewPost(post, "1", " 2", "3")

    fun getSavedPosts() = Firebase.getSavedPost()

    fun getComments(Key : String) = Firebase.getComments(Key)


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