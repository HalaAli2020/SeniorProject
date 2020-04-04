package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class ClickedPostViewModel @Inject constructor(private val repository : PostRepository) : ViewModel(){

    private val CommentListener : PostListener? = null
    var commentsLiveList : CommentLive = CommentLive()
    var comment : String? = null
    private var CommentsList = mutableListOf<Comment>()
    var pKey: String? = null
    var userID : String? = null
    var classkey : String? = null
    var crn : String? = null
    var title: String? = null
    var text: String? = null
    //private var getCommentsJob: Job? = null
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
       if(pKey.isNullOrEmpty())
       {
           PostKey = pKey
           CommentListener?.onFailure("Post key not found")
       }
        commentsLiveList = repository.getComments(classkey!!, crn!!)

        return commentsLiveList
    }


    fun editComment(){
        repository.editNewComment(comuserid!!, usercomkey!!, comment!!, usercrn!!, postukey!!)
    }


    fun newComment()
    {

        repository.newComment(pKey!!,comment!!, classkey!!, userID!!, crn!!)
    }
    fun checkcomments() : Boolean
    {
        if(CommentsList.size != -1)
        {
            return true
        }
        return false
    }

    fun reportUserComment(accusedID: String, complaintext: String, crn: String, classkey: String, comKey: String){
        repository.reportUserComment(accusedID, complaintext, crn, classkey, comKey)

    }

    fun blockUser(UserID: String){
        repository.blockUser(UserID)
    }






}