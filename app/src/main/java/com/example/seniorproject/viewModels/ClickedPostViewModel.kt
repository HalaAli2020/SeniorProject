package com.example.seniorproject.viewModels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.repositories.PostRepository
import com.example.seniorproject.data.repositories.UserAuthRepo
import javax.inject.Inject

class ClickedPostViewModel @Inject constructor(private val repository : PostRepository) : ViewModel(){

    val CommentListener : PostListener? = null
    var CommentsList : CommentLive = CommentLive()
    var Comment : String? = null
    var PKey: String? = null
    private var PostKey : String? = null

    fun getComments(PKey: String) : CommentLive
    {
       if(PKey.isNullOrEmpty())
       {
           PostKey = PKey
           CommentListener?.onFailure("Post key not found")

       }
        CommentsList = repository.getComments(PKey)

        return CommentsList
    }
    fun newComment()
    {

        repository.newComment(PKey!!,Comment!!)
    }



}