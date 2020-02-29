package com.example.seniorproject.Profile

import com.example.seniorproject.data.Firebase.FirebaseData

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val Firebase: FirebaseData) {

    fun getUserProfilePosts() = Firebase.getUserProfilePosts()
    //may also need user


    companion object {
        @Volatile
        private var instance: ProfileRepository? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance
                    ?: ProfileRepository(firebasedata).also { instance = it }
            }
    }

}
