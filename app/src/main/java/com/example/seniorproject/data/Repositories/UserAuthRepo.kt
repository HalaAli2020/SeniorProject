package com.example.seniorproject.data.Repositories
import androidx.lifecycle.LiveData
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.User
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

//private val Firebase: FirebaseData
@Singleton
class UserAuthRepo @Inject constructor(private val Firebase: FirebaseData) {

    fun login(email: String, password: String) = Firebase.LoginUser(email,password)
    //should user and password be a livedata object??????? return

    fun register(username: String, email: String, password: String) = Firebase.RegisterUser(username, email, password)

    fun currentUser(Firebase: FirebaseData) {
        Firebase.CurrentUser()
    }
    fun logout(Firebase: FirebaseData)
    {
        Firebase.logout()
    }
    /*companion object{
        @Volatile private var instance: UserAuthRepo? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: UserAuthRepo(firebasedata).also { instance = it }
            }
        //if the instance is not null
    }*/
}