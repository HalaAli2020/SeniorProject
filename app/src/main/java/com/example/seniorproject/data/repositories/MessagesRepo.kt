package com.example.seniorproject.data.repositories
import com.example.seniorproject.data.Firebase.FirebaseData
import javax.inject.Inject
import javax.inject.Singleton


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

    fun sendMessage(chatMessage: String?, toID: String?, username: String?, profileImageUrl: String?){
        Firebase.sendMessage(chatMessage, toID, username, profileImageUrl)
    }

    fun getMessages(toID: String?) = Firebase.getMessages(toID)

    fun getRecentMessages() = Firebase.getRecentMessages()
}