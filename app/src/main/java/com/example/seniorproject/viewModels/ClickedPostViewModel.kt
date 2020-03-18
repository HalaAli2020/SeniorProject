package com.example.seniorproject.viewModels

import androidx.lifecycle.*
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.Job
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
    var comuserid: String? = null
    var usercomkey: String?= null
    var ctext: String? = null
    var usercrn: String? = null
    var postukey: String? = null


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


    fun editComment(){
        repository.editNewComment(comuserid!!, usercomkey!!, ctext!!, Comment!!, usercrn!!, postukey!!)
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

    fun reportUserComment(accusedID: String, complaintext: String, crn: String, classkey: String, comKey: String){
        repository.reportUserComment(accusedID, complaintext, crn, classkey, comKey)

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