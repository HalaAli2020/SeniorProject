package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CommunityPostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private var listClassesco: Flow<Post> = flow{ }
    private var listClasses : PostLiveData? = null
    private lateinit var className: String

    @InternalCoroutinesApi
    fun getClassesco(className: String) : Flow<Post> {
        listClassesco = repository.getClassPostsco(className)

        /*viewModelScope.launch{
            Log.d("soup", "before collect")
            listClassesco = repository.getClassPostsco(className)
            Log.d("soup", "dummy spot")
            listClassesco.collect{
                    value -> Log.d("soup", "this is the post $value")
            }
            Log.d("soup","collected the titles.")
        } this works but you have to refresh twice to see post values in logcat*/
        return listClassesco
        }

  /*  @InternalCoroutinesApi
    fun returnClassPostsco(className: String): Flow<Post> {
        getClassesco(className)
        return listClassesco
    }*/

    private fun getClasses(className: String){
        listClasses = repository.getClassPosts(className)
    }

    fun returnClassPosts(className: String): PostLiveData {
        getClasses(className)
        return listClasses!!
    }

    fun blockUser(UserID: String){
        repository.blockUser(UserID)
    }

    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String){
        repository.reportUserPost(accusedID, complaintext, crn, classkey)
    }

   fun checknull() : Boolean
   {
       if (listClasses == null)
       {
           return true
       }
       else
       {
           return false
       }
   }



}