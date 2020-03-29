package com.example.seniorproject.data.repositories
import android.net.Uri
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.User
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

//private val Firebase: FirebaseData
@Singleton
class UserAuthRepo @Inject constructor(private val Firebase: FirebaseData) {

    suspend fun RegisterUserEmail(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String,
        username: String,
        callback: EmailCallback
    ) {
        Firebase.RegisterUserEmail(firebaseAuth, email, password, username, callback)
    }


    suspend fun LoginUserAccount(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String,
        callback: EmailCallback
    ) {
        fun currentUser() = Firebase.CurrentUser()

        suspend fun LoginUserAccount(
            firebaseAuth: FirebaseAuth,
            email: String,
            password: String,
            callback: EmailCallback
        ) {
            Firebase.LoginUserEmail(firebaseAuth, email, password, callback)
        }

        suspend fun resetUserPassword(
            firebaseAuth: FirebaseAuth,
            email: String,
            callback: EmailCallback
        ) {
            Firebase.resetUserPassword(firebaseAuth, email, callback)
        }

        fun fetchCurrentUserName() = Firebase.fetchCurrentUserName()

        fun logout(Firebase: FirebaseData) {
            Firebase.logout()
        }
    }
}