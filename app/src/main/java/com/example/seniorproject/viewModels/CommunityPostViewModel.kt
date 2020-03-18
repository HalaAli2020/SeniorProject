package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class CommunityPostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    var listClasses : PostLiveData? = null
    private lateinit var className: String
    var posts : PostLiveData = PostLiveData()

    private fun getClasses(className: String){
        listClasses = repository.getClassPosts(className)

    }

    fun returnClassPosts(className: String): PostLiveData {
        getClasses(className)
        return listClasses!!
    }

    fun blockPost(UserID: String, crn: String, classkey: String){
        repository.blockPost(UserID, crn, classkey)
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