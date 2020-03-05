package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class CommunityPostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private var listClasses : PostLiveData? = null
    private lateinit var className: String

    private fun getClasses(className: String){
        listClasses = repository.getClassPosts(className)
    }

    fun returnClassPosts(className: String): PostLiveData {
        getClasses(className)
        return listClasses!!
    }

    fun deletePost(Classkey: String, crn: String)
    {

        repository.deleteNewPost(Classkey, crn)
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