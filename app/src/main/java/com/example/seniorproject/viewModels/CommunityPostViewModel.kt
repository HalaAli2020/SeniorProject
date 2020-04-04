package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.example.seniorproject.Utils.*
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class CommunityPostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private var listClassesco: Flow<Post> = flow { }
    private var listpostclass: MutableList<Post> = mutableListOf()
    private var babylistnum: List<Post> = mutableListOf()
    private var listofposts: List<Post> = mutableListOf()
    private var postliveData: MutableLiveData<MutableList<Post>>? = null
    private var livepostclass: LiveData<Post>? = null
    private var listClasses: PostLiveData? = null
    private lateinit var className: String
    //private lateinit var className: String

    @InternalCoroutinesApi
    fun getClassesco(className: String, callback: MutableListCallback){
        repository.getClassPostsco(className, object: FlowCallback{
            override fun onFlow(flowCallback: Flow<Post>) {
                viewModelScope.launch {
                    listofposts = flowCallback.toList()
                    callback.onList(listofposts)
                }
            }
        })
    }

    fun blockUser(UserID: String){
        repository.blockUser(UserID)
    }

    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String){
        repository.reportUserPost(accusedID, complaintext, crn, classkey)
    }





}