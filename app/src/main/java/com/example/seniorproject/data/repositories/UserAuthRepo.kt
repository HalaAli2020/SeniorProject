package com.example.seniorproject.data.repositories
import android.net.Uri
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.data.Firebase.FirebaseData
import com.google.firebase.auth.FirebaseAuth
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

    suspend fun resetUserPassword(firebaseAuth: FirebaseAuth, email: String, callback: EmailCallback){
        Firebase.resetUserPassword(firebaseAuth, email, callback)
    }

    fun currentUser() = Firebase.CurrentUser()

    suspend fun LoginUserAccount(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String,
        callback: EmailCallback
    ) {
        Firebase.LoginUserEmail(firebaseAuth, email, password, callback)
    }

}
