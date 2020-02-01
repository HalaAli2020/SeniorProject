package com.example.seniorproject.data.Repositories
import com.example.seniorproject.data.Firebase.FirebaseData

class UserAuthRepo (
    //repository communicates with backend
    private val Firebase: FirebaseData
){
    fun login(email: String, password: String) = Firebase.LoginUser(email,password)

    fun register(username: String, email: String, password: String) = Firebase.RegisterUser(username, email, password)

    fun currentUser() = Firebase.CurrentUser()

    fun logout() = Firebase.logout()

    companion object{
        @Volatile private var instance: UserAuthRepo? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: UserAuthRepo(firebasedata).also { instance = it }
            }
        //if the instance is not null
    }
}