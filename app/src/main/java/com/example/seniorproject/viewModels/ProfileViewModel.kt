package com.example.seniorproject.viewModels


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Utils.CheckCallback
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.interfaces.*
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProfileViewModel @Inject constructor(private val repository: PostRepository) : ViewModel(){

    var posts: PostLiveData = PostLiveData()
    var comments: CommentLive = CommentLive()
    private var postKey : String? = null
    val commentListener : PostListener? = null
    var otherEmail : String = "no email available"
    var currentUsername : String = "no username available"
    var otherBio : String = "no bio available"
    var noPostCheck : Boolean? = null
    var noCommentsCheck : Boolean? = null


    fun getUserProfilePosts(UserID : String, callback: PostListFromFlow) {

        repository.getUserProfilePosts(UserID, object: FirebaseCallbackPostFlow {
            override fun onCallback(flow: Flow<Post>) {
                viewModelScope.launch {
                    var postFlow = flow.toList()
                    callback.onList(postFlow)
                }
            }
        })

    }


    fun getUserProfileComments(UserID : String, callback: CommentListFromFlow){
        repository.getUserProfileComments(UserID, object: FirebaseCallbackCommentFlow {
            override fun onCallback(flow: Flow<Comment>) {
                viewModelScope.launch {
                    var commentflow = flow.toList()
                    callback.onList(commentflow)
                }
            }
        })
    }


    fun deletePost(Classkey: String, crn: String, userID: String)
    {

        repository.deleteNewPost(Classkey, crn, userID)
    }

    fun deleteCommentFromUserProfile(PKey: String, crn: String, Classkey: String, userID: String)
    {
        if(PKey.isEmpty())
        {
            postKey = PKey
            commentListener?.onFailure("Post key not found")

        }
        repository.deleteNewCommentFromUserProfile(PKey, crn, Classkey, userID)
    }


    fun deleteCommentFromCommPosts(PKey: String, crn: String, Classkey: String)
    {
        if(PKey.isEmpty())
        {
            postKey = PKey
            commentListener?.onFailure("Post key not found")

        }
        repository.deleteNewCommentFromCommPosts(PKey, crn, Classkey)
    }

    fun uploadUserProfileImage(selectedPhotoUri: Uri) = repository.uploadUserProfileImage(selectedPhotoUri)

    fun currentUser() = repository.currentUser()

    fun saveNewUsername(username: String) = repository.saveNewUsername(username)

    var user = repository.currentUser()

    fun readPhotoValue(useridm: String, callback: EmailCallback) {
        repository.readPhotoValue(useridm, callback)
    }

    fun fetchEmail(UserID: String, callback : EmailCallback) : String {
        repository.fetchEmail(UserID, object : FirebaseCallbackItem {
            override fun onStart() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onMessage(data: DataSnapshot): String {
                val email = data.child("email").getValue(String::class.java)
                otherEmail = email ?: "no email in success"
                callback.getEmail(otherEmail)
                return otherEmail
            }
        })
        return otherEmail
    }
//used in UserProfileActivity to get the current users username in real time
    fun fetchUsername(UserID: String, callback : EmailCallback) : String {
        repository.fetchUsername(UserID, object : FirebaseCallbackItem{
            override fun onStart() {

            }

            override fun onFailure() {

            }

            override fun onMessage(data: DataSnapshot): String {
                val email = data.child("Username").getValue(String::class.java)
                currentUsername = email ?: "no email in success"
                callback.getEmail(currentUsername)
                return currentUsername
            }
        })
        return currentUsername
    }

    fun fetchBio(UserID: String, callback: EmailCallback) : String {
        repository.fetchBio(UserID, object : FirebaseCallbackItem{
            override fun onStart() {

            }

            override fun onFailure() {

            }

            override fun onMessage(data: DataSnapshot): String {
                val bio = data.child("UserBio").getValue(String::class.java) ?: "no bio"
                otherBio = bio
                callback.getEmail(otherBio)
                return otherBio
            }

        })
        return otherBio
    }


    fun getclassnamesforusername() = repository.getclassnamesforusername()

    fun sendClassnameForUsername() = repository.sendClassnameForUsername()

    fun saveUserbio(bio : String) = repository.saveUserbio(bio)

    //fun fetchCurrentBio() = repository.fetchCurrentBio()

    fun noPostsChecker(UserID: String, callback: CheckCallback) : Boolean
    {
        repository.noPostsChecker(UserID, object : FirebaseCallbackBool {
            override fun onStart() {  }
            override fun onFailure() {  }
            override fun onSuccess(data: DataSnapshot) : Boolean {
                if (!data.child("Posts").exists())
                {
                    noPostCheck = true
                    callback.check(noPostCheck ?: false)
                }
                else
                {
                    callback.check(noPostCheck ?: false)
                    noPostCheck = false //here
                }
                return  noPostCheck ?: false
            }
        })
        return  noPostCheck ?: false
    }

}