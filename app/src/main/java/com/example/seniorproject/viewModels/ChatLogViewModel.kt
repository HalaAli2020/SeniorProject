package com.example.seniorproject.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.ChatMessage
import com.example.seniorproject.data.repositories.MessagesRepo
import javax.inject.Inject

class ChatLogViewModel @Inject constructor(private val repository: MessagesRepo) :
    ViewModel() {


    var chatMessage: String? = null
    var toID: String? = null
    var profileURI: String? = "null"
    var messages: MutableLiveData<List<ChatMessage>>? = null
    lateinit var username: String

    fun sendMessage() {
        if (!chatMessage.isNullOrEmpty()) {
            repository.sendMessage(chatMessage, toID, username, profileURI)
        }
    }

    fun getChatMessages(): MutableLiveData<List<ChatMessage>>? {
        messages = repository.getMessages(toID)
        return messages
    }

}