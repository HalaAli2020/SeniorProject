package com.example.seniorproject.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Utils.CheckCallback
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.interfaces.CommentListFromFlow
import com.example.seniorproject.data.interfaces.FirebaseCallbackCommentFlow
import com.example.seniorproject.data.interfaces.FirebaseCallbackNoComments
import com.example.seniorproject.data.interfaces.FirebaseCallbacksubBool
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

class ClickedPostViewModel @Inject constructor(private val repository : PostRepository) : ViewModel(){

    private val commentListener : PostListener? = null
    var comment : String? = null
    private var commentsList = mutableListOf<Comment>()
    var pKey: String? = null
    var userID : String? = null
    var classkey : String? = null
    var crn : String? = null
    var title: String? = null
    var text: String? = null
    private var postKey : String? = null
    var comuserid: String? = null
    var usercomkey: String?= null
    var ctext: String? = null
    var usercrn: String? = null
    var postukey: String? = null
    var boolcom = MutableLiveData<Boolean>()
    var boolsub : Boolean? = null


    init {

    }
    //calls corresponding function from post repository gets binded variable contents from clicked post xml file
    fun noCommentsCheckForCommPosts(callback: FirebaseCallbackNoComments){
      repository.noCommentsCheckForCommPosts(crn!!, classkey!!, callback)
    }
/* When the uses clicks on a post this function will get the comments from the backend and send back the data using a callback 
* */
    fun getClassComments(callback: CommentListFromFlow)
    {
        if(pKey.isNullOrEmpty())
        {
            postKey = pKey
            commentListener?.onFailure("Post key not found")
        }
        repository.getPostComments(classkey!!, crn!!, object : FirebaseCallbackCommentFlow {
            override fun onCallback(flow: Flow<Comment>) {
                viewModelScope.launch {
                    var commflow = flow.toList()
                    callback.onList(commflow)
                }
            }
        })

    }
    //calls corresponding function from post repository
    fun editComment(){
        repository.editNewComment(comuserid!!, usercomkey!!, comment!!, usercrn!!, postukey!!)
    }

/* This function is called when a user has created a new comment it will check to make sure it has the correct data in the
* entered and then send it to the repository so that it can be sent on to the database
* */

    fun newComment(comment : String)
    {
        repository.newComment(pKey!!,comment, classkey!!, userID!!, crn!!)
    }

    fun checkcomments() : Boolean
    {
        if(commentsList.size != -1)
        {
            return true
        }
        return false
    }
    //calls corresponding function from post repository
    fun reportUserComment(accusedID: String, complaintext: String, crn: String, classkey: String, comKey: String){
        repository.reportUserComment(accusedID, complaintext, crn, classkey, comKey)

    }
    //calls corresponding function from post repository
    fun blockUser(UserID: String){
        repository.blockUser(UserID)
    }


    //takes classname and searches for a match in the users subscriptions
    fun checkSubscriptions(classname : String, checkCallback: CheckCallback)
    {
        repository.checkSubscription(classname,object : FirebaseCallbacksubBool {
            override fun onStart() {  }
            override fun onFailure() { }

            override fun onSuccess(data: DataSnapshot) {
                if (data.exists()) {
                    for (sub in data.children) {
                        if (sub.value == classname) {
                            //callback lets frontend know to make successful comment toast message
                            boolsub = true
                            checkCallback.check(boolsub ?: false)
                            return
                        }
                    }
                    //callback lets frontend know to make unsuccessful comment toast message
                    boolsub = false
                    checkCallback.check(boolsub ?: false)
                }

            }
        })
    }

}