package com.example.seniorproject.data.repositories
import android.net.Uri
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.ChatMessage
import com.example.seniorproject.data.models.User
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

//private val Firebase: FirebaseData
@Singleton
class MessagesRepo @Inject constructor(private val Firebase: FirebaseData) {

    companion object {
        @Volatile
        private var instance: MessagesRepo? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: MessagesRepo(firebasedata).also { instance = it }
            }
    }


    fun getUsers() = Firebase.getUsers()

    fun sendMessage(chatMessage: String, toID: String, username: String){
        Firebase.sendMessage(chatMessage, toID, username)
    }

    fun getMessages(toID: String?) = Firebase.getMessages(toID)

    fun getRecentMessages() = Firebase.getRecentMessages()
}