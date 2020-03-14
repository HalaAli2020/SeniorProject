package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.ChatMessage
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.MessagesRepo
import javax.inject.Inject

class ChatLogViewModel @Inject constructor(private val repository: MessagesRepo) :
    ViewModel() {

    //var chatReference: String? = null
    var chatMessage: String? = null
    var toID: String? = null
    var messages: MutableLiveData<List<ChatMessage>>? = null


    fun sendMessage() {
        if (!chatMessage.isNullOrEmpty()) {
            repository.sendMessage(chatMessage!!, toID!!)
        }
    }

    fun listenForMessages() {
        messages = repository.listenForMessages(toID)
    }

}