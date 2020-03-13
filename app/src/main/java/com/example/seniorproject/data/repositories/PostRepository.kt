package com.example.seniorproject.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.models.Post
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PostRepository @Inject constructor(private val Firebase: FirebaseData) {
    val post: Post? = null
    val CommentList: MutableList<Comment> = mutableListOf()
    val CommentL = CommentLive()
    private var getCommentsJob: Job? = null

    fun saveNewPost(Title: String, Text: String, Subject: String, CRN: String) {
        val post = Post(Title, Text, CRN)
        Firebase.saveNewPosttoUser(post, "1", CRN)
    }

    fun uploadUserProfileImage(selectedPhotoUri: Uri) =
        Firebase.uploadImageToFirebaseStorage(selectedPhotoUri)

    fun getSubscribedPosts() = Firebase.getSubscribedPosts()
    //fun getEmail() = Firebase.getEmail()

    fun getpostKey(PKey: String)
    {

    }

    fun getComments(ClassKey: String, subject: String): CommentLive {
        return Firebase.getComments(ClassKey, subject)
    }
    /*suspend fun getCommentsCO(PKey: String) : Flow<CommentLive> = flow {
         val flo = Firebase.getCommentsCO(PKey)
        flo.asFlow()
         /*flo.onCompletion()
         {
             flo.collect {
                 value -> CommentList.add(value!!)
                 Log.d("Flow", value.text)
             }
             CommentL.value = CommentList
         }*/
         emit(flo)





     }*/


    fun newComment(PKey: String, Comment: String, Classkey: String, UserID: String, crn: String) {
        Firebase.saveNewComment(Comment, PKey, Classkey, UserID, crn)
        //Firebase.saveNewCommentClass(Comment ,PKey, Classkey, UserID, crn)
    }

    fun deleteNewPost(postKey: String, crn: String, userID: String){
        Firebase.deleteNewPost(postKey, crn, userID)
    }

    fun deleteNewCommentFromUserProfile(ClassKey: String, crn: String, comKey: String, userID: String){
        Firebase.deleteNewCommentFromUserProfile(ClassKey, crn, comKey, userID)
    }

    fun deleteNewCommentFromCommPosts(postKey: String, crn: String, classkey: String){
        Firebase.deleteNewCommentFromCommPosts(postKey, crn, classkey)
    }

    fun editNewComment(userID: String, usercomkey:String, ctext: String, ntext: String, crn: String, postKey: String){
        Firebase.editComment(userID, usercomkey, ctext, ntext, crn, postKey)
    }


    fun editPost(crn: String, postKey: String, ctext: String, ctitle: String, ntext: String, ntitle: String,
                 userID: String){
        Firebase.editPost(crn, postKey, ctext, ctitle, ntext, ntitle, userID)
    }

    fun currentUser() = Firebase.CurrentUser()

    fun fetchCurrentUserName() = Firebase.fetchCurrentUserName()

    fun getClasses() = Firebase.sendClist()

    fun getClassPosts(className: String) = Firebase.getClassPosts(className)

    fun getUserSub(): MutableList<String>? {
        return Firebase.sendUserSUB()

    }

    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }

    fun remUsersub(crn: String) {
        Firebase.removeUserSub(crn)
        Firebase.removeClassSub(crn)
    }


    fun getUserProfilePosts(userID: String) = Firebase.getUserProfilePosts(userID)

    fun getUserProfileComments(userID: String) = Firebase.getUserProfileComments(userID)

    fun fetchEmail(UserID: String) = Firebase.fetchEmail(UserID)


    companion object {
        @Volatile
        private var instance: PostRepository? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(firebasedata).also { instance = it }
            }
    }
}