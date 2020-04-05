package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

class ClickedPostViewModel @Inject constructor(private val repository : PostRepository) : ViewModel(){

    private val commentListener : PostListener? = null
    var commentsLiveList : CommentLive = CommentLive()
    var comment : String? = null
    private var commentsList = mutableListOf<Comment>()
    var pKey: String? = null
    var userID : String? = null
    var classkey : String? = null
    var crn : String? = null
    var title: String? = null
    var text: String? = null
    //private var getCommentsJob: Job? = null
    private var postKey : String? = null
    var comuserid: String? = null
    var usercomkey: String?= null
    var ctext: String? = null
    var usercrn: String? = null
    var postukey: String? = null


    fun getComments() : CommentLive
    init {

    }

    fun noCommentsCheckForCommPosts(callback: PostRepository.FirebaseCallbackNoComments){
      repository.noCommentsCheckForCommPosts(crn!!, classkey!!, callback)
    }

    fun getComments(callback: CommentListFromFlow)
    {
       if(pKey.isNullOrEmpty())
       {
           postKey = pKey
           commentListener?.onFailure("Post key not found")
       }
        repository.getComments(classkey!!, crn!!, object : FirebaseData.FirebaseCallbackCommentFlow {
            override fun onCallback(flow: Flow<Comment>) {
                viewModelScope.launch {
                    var commflow = flow.toList()
                    callback.onList(commflow)
                }
            }
        })

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
        if(commentsList.size != -1)
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

    interface CommentListFromFlow {
        fun onList(list: List<Comment>)
    }



}