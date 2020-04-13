package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Utils.FlowCallback
import com.example.seniorproject.Utils.MutableListCallback
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommunityPostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private var listofposts: List<Post> = mutableListOf()


    //this function uses a viewmodel scope coroutine body to convert the flow of posts recieved through the flow callback into a list.
    //this list is then traveled upwards into the view using another callback
    //internal coroutines api tag is needed when using coroutine bodies.
    @InternalCoroutinesApi
    fun getClassesco(className: String, callback: MutableListCallback){
        repository.getCommunityPosts(className, object: FlowCallback{
            override fun onFlow(flowCallback: Flow<Post>) {
                viewModelScope.launch {
                    listofposts = flowCallback.toList()
                    callback.onList(listofposts)
                }
            }
        })
    }

    //these functions call their corresponding function in repository
    fun blockUser(UserID: String){
        repository.blockUser(UserID)
    }

    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String){
        repository.reportUserPost(accusedID, complaintext, crn, classkey)
    }
}