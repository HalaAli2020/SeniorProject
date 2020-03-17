package com.example.seniorproject.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.ChatMessage
import com.example.seniorproject.data.models.LatestMessage
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.MessagesRepo
import javax.inject.Inject

class MessagesFragmentViewModel @Inject constructor(private val repository: MessagesRepo) :
    ViewModel() {



    fun getRecentMessages(): MutableLiveData<List<LatestMessage>> {
        var latestMessagesMap = repository.getRecentMessages()
        return latestMessagesMap
    }

}