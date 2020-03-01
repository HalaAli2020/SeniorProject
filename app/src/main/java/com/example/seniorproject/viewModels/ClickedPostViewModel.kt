package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.repositories.PostRepository
import com.example.seniorproject.data.repositories.UserAuthRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import javax.inject.Inject

class ClickedPostViewModel @Inject constructor(private val repository : PostRepository) : ViewModel(){

    val CommentListener : PostListener? = null
    var CommentsLiveList : CommentLive = CommentLive()
    var Comment : String? = null
    var CommentsList = mutableListOf<Comment>()
    var PKey: String? = null
    var UserID : String? = null
    var Classkey : String? = null
    var crn : String? = null
    var title: String? = null
    var text: String? = null
    private var getCommentsJob: Job? = null
    private var PostKey : String? = null
    init {

    }

    fun getComments() : CommentLive
    {
       if(PKey.isNullOrEmpty())
       {
           PostKey = PKey
           CommentListener?.onFailure("Post key not found")

       }
        CommentsLiveList = repository.getComments(Classkey!!, crn!!)

        return CommentsLiveList
    }
    fun newComment()
    {

        repository.newComment(PKey!!,Comment!!, Classkey!!, UserID!!, crn!!)
    }
    fun checkcomments() : Boolean
    {
        if(CommentsList != null)
        {
            return true
        }
        return false
    }

    fun deleteComment()
    {
        if(PKey.isNullOrEmpty())
        {
            PostKey = PKey
            CommentListener?.onFailure("Post key not found")

        }
        repository.getComments(Classkey!!, crn!!)
    }
    /*fun getCommentsCO(PKey: String) : CommentLive? {
        getCommentsJob = viewModelScope.launch {
            repository.getCommentsCO(PKey).collect {
                CommentsLiveList.value = it.value
                Log.d("valuse", )
            }

        }
        return CommentsLiveList
    }*/





}