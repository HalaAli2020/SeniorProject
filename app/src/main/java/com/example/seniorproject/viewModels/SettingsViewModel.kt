package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.interfaces.FirebaseCallbackItem
import com.example.seniorproject.data.interfaces.FirebaseCallbackUserListFlow
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    fun getBlockedUsersList(callback: BlockedUserListCallback){
        repository.getBlockedUsers(object: FirebaseCallbackUserListFlow{
            override fun onFlow(flow: Flow<String>) {
                viewModelScope.launch {
                    var blockedlist = flow.toList()
                    callback.onList(blockedlist)
                }
            }
        })
    }

    fun unblockUser(username : String , callback : EmailCallback) = repository.unblockUser(username, object : FirebaseCallbackItem {
        override fun onStart() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onFailure() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onMessage(data: DataSnapshot) {
            callback.getEmail("done")
        }

    })

}

interface BlockedUserListCallback{
    fun onList(list: List<String>)
}