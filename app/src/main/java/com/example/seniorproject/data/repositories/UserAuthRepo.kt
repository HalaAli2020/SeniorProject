package com.example.seniorproject.data.repositories

import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.data.Firebase.FirebaseData
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

//repository class layer used to communicate between the viewmodel and the Firebase data file
@Singleton
class UserAuthRepo @Inject constructor(private val Firebase: FirebaseData) {

    //register user function is a suspend because of async properties
    suspend fun registerUserEmail(firebaseAuth: FirebaseAuth, email:String ,password:String, username: String, callback: EmailCallback){
        Firebase.registerUserEmail(firebaseAuth, email, password, username, callback)
    }

    //Login user function is a suspend function because of async properties
    suspend fun loginUserAccount(firebaseAuth: FirebaseAuth, email:String, password:String, callback: EmailCallback){
        Firebase.loginUserEmail(firebaseAuth, email, password, callback)
    }

    //ResetUserPassword is a suspend function because of async properties
    suspend fun resetUserPassword(firebaseAuth: FirebaseAuth, email: String, callback: EmailCallback){
        Firebase.resetUserPassword(firebaseAuth, email, callback)
    }

    //returns current user
    fun currentUser() = Firebase.currentUser()
}
