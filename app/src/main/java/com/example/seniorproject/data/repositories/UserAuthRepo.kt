
package com.example.seniorproject.data.repositories
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.User
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

//private val Firebase: FirebaseData
@Singleton
class UserAuthRepo @Inject constructor(private val Firebase: FirebaseData) {
    
    fun login(email: String, password: String) = Firebase.LoginUser(email,password)
    //should user and password be a livedata object??????? return

    fun register(username: String, email: String, password: String) =
        Firebase.RegisterUser(username, email, password)
    
    fun currentUser(Firebase: FirebaseData) {
        Firebase.CurrentUser()
    }
    
    fun logout(Firebase: FirebaseData)
    {
        Firebase.logout()
    }
}