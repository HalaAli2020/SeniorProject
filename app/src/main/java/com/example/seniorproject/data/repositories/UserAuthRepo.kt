package com.example.seniorproject.data.repositories
import com.example.seniorproject.data.Firebase.FirebaseData
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

//private val Firebase: FirebaseData
@Singleton
class UserAuthRepo @Inject constructor(private val Firebase: FirebaseData) {

    //should user and password be a livedata object??????? return

    suspend fun RegisterUserEmail(firebaseAuth: FirebaseAuth, email:String ,password:String, username: String){
        Firebase.RegisterUserEmail(firebaseAuth, email, password, username)
    }

    suspend fun LoginUserAccount(firebaseAuth: FirebaseAuth, email:String, password:String){
        Firebase.LoginUserEmail(firebaseAuth, email, password)
    }

    suspend fun resetUserPassword(firebaseAuth: FirebaseAuth, email: String){
        Firebase.resetUserPassword(firebaseAuth, email)
    }

    fun currentUser() = Firebase.CurrentUser()

    //fun fetchCurrentUserName() = Firebase.fetchCurrentUserName()

    /*fun logout(Firebase: FirebaseData)
    {
        Firebase.logout()
    }*/
}