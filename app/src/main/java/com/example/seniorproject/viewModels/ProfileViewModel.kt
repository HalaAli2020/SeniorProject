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
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProfileViewModel @Inject constructor(private val repository: PostRepository) : ViewModel(){

    private var postKey : String? = null
    val commentListener : PostListener? = null
    var otherEmail : String = "no email available"
    var currentUsername : String = "no username available"
    var otherBio : String = "no bio available"

    //this function calls its corresponding functions in the repository and launches viewModelscope coroutine where it takes the flow
    //of posts recieved from the flow callback and converts it to a list. this list is traveled upwards to the view using another
    //callback
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

    //this function calls its corresponding functions in the repository and launches viewModelscope coroutine where it takes the flow
    //of comments recieved from the flow callback and converts it to a list. this list is traveled upwards to the view using another
    //callback
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

    //this function calls its corresponding functions in the repository
    fun deletePost(Classkey: String, crn: String, userID: String)
    {

        repository.deleteNewPost(Classkey, crn, userID)
    }

    //this function calls its corresponding functions in the repository
    fun deleteCommentFromUserProfile(PKey: String, crn: String, Classkey: String, userID: String)
    {
        if(PKey.isEmpty())
        {
            postKey = PKey
            commentListener?.onFailure("Post key not found")

        }
        repository.deleteNewCommentFromUserProfile(PKey, crn, Classkey, userID)
    }

    //this function calls its corresponding functions in the repository
    fun deleteCommentFromCommPosts(PKey: String, crn: String, Classkey: String)
    {
        if(PKey.isEmpty())
        {
            postKey = PKey
            commentListener?.onFailure("Post key not found")

        }
        repository.deleteNewCommentFromCommPosts(PKey, crn, Classkey)
    }

    //the below three functions call their corresponding functions in the repository
    fun uploadUserProfileImage(selectedPhotoUri: Uri) = repository.uploadUserProfileImage(selectedPhotoUri)

    fun currentUser() = repository.currentUser()

    fun saveNewUsername(username: String) = repository.saveNewUsername(username)

    //grabs current user
    var user = repository.currentUser()


    fun readPhotoValue(useridm: String, callback: EmailCallback) {
        repository.readPhotoValue(useridm, callback)
    }

    //this function calls the repository fetchEmail and travels the value up into the view using a callback
    fun fetchEmail(UserID: String, callback : EmailCallback){
        repository.fetchEmail(UserID, object : FirebaseCallbackItem {
            override fun onStart() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onMessage(data: DataSnapshot){
                val email = data.child("email").getValue(String::class.java)
                otherEmail = email ?: "no email in success"
                callback.getEmail(otherEmail)
            }
        })
    }

//used in UserProfileActivity to get the current users username in real time
    fun fetchUsername(UserID: String, callback : EmailCallback){
        repository.fetchUsername(UserID, object : FirebaseCallbackItem{
            override fun onStart() {

            }

            override fun onFailure() {

            }

            override fun onMessage(data: DataSnapshot){
                val email = data.child("Username").getValue(String::class.java)
                currentUsername = email ?: "no email in success"
                callback.getEmail(currentUsername)
            }
        })
    }

    //this function calls the repository fetchBio and travels the value up into the view using a callback
    fun fetchBio(UserID: String, callback: EmailCallback){
        repository.fetchBio(UserID, object : FirebaseCallbackItem{
            override fun onStart() {

            }

            override fun onFailure() {

            }

            override fun onMessage(data: DataSnapshot){
                val bio = data.child("UserBio").getValue(String::class.java) ?: "no bio"
                otherBio = bio
                callback.getEmail(otherBio)
            }

        })
    }

    //the below three functions call their corresponding functions in the repository
    fun getclassnamesforusername() = repository.getclassnamesforusername()

    fun sendClassnameForUsername() = repository.sendClassnameForUsername()

    fun saveUserbio(bio : String) = repository.saveUserbio(bio)

}