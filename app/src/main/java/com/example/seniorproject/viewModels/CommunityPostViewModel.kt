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