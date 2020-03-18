package com.example.seniorproject.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.MessagesRepo
import javax.inject.Inject

class NewMessageViewModel @Inject constructor(private val repository: MessagesRepo) :
    ViewModel() {

    var users: MutableLiveData<List<User>>? = null

    fun fetchUsers(): MutableLiveData<List<User>>? {
        if (users == null) {
            users = MutableLiveData()
            loadUsers()
        }
        return users
    }

    private fun loadUsers() {
        users = repository.getUsers()
    }

}