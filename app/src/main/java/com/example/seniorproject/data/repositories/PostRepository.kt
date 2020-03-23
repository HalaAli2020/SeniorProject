package com.example.seniorproject.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
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

    fun saveNewPost(text: String, title: String, CRN: String) {
        Firebase.saveNewPosttoUser(text, title, CRN)
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

    fun saveNewImgPosttoUser(title : String, text:String, Subject: String, CRN: String, uri: Uri, imagePost : Boolean)
    = Firebase.saveNewImgPosttoUser(title,text,Subject,CRN,uri,imagePost)

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

    fun blockComment(UserID: String, text: String, crn: String, postID: String){
        Firebase.blockUserComment(UserID , text , crn , postID)
    }

    fun blockPost(UserID: String, crn: String, classkey: String){
        Firebase.blockUserPost(UserID, crn, classkey)
    }

    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String){
        Firebase.reportUserPost(accusedID, complaintext, crn, classkey)

    }

    fun reportUserComment(accusedID: String, complaintext: String, crn: String, classkey: String, comKey: String){
        Firebase.reportUserComment(accusedID, complaintext, crn, classkey, comKey)

    }

    fun currentUser() = Firebase.CurrentUser()

    fun fetchCurrentUserName() = Firebase.fetchCurrentUserName()

    fun getClasses() = Firebase.sendClist()

    fun getClassPosts(className: String) = Firebase.getClassPosts(className)

    fun getUserSub()= Firebase.sendUserSUB()


    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }

    fun remUsersub(crn: String) {
        Firebase.removeUserSub(crn)
        Firebase.removeClassSub(crn)
    }

    fun getclassnamesforusername() = Firebase.getclassnamesforusername()

    fun sendClassnameForUsername() = Firebase.sendClassnameForUsername()

    fun getUserProfilePosts(userID: String) = Firebase.getUserProfilePosts(userID)

    fun getUserProfileComments(userID: String) = Firebase.getUserProfileComments(userID)

    fun fetchEmail(UserID: String) = Firebase.fetchEmail(UserID)

    fun saveNewUsername(username: String) = Firebase.saveNewUsername(username)

    fun saveUserbio(bio : String) = Firebase.saveUserbio(bio)

    fun fetchBio(UserID: String) = Firebase.fetchBio(UserID)

    fun fetchCurrentBio() = Firebase.fetchCurrentBio()

    fun noPostsChecker(UserID: String) = Firebase.noPostsChecker(UserID)

    fun noCommentsChecker(UserID: String) = Firebase.noCommentsChecker(UserID)


    companion object {
        @Volatile
        private var instance: PostRepository? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(firebasedata).also { instance = it }
            }
    }
}