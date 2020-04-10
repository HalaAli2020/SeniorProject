package com.example.seniorproject.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.Utils.FlowCallback
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.*
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PostRepository @Inject constructor(private val Firebase: FirebaseData) {
    val post: Post? = null
    val commentList: MutableList<Comment> = mutableListOf()
    val commentL = CommentLive()
    var sessionUser: User? = null
    var listClassesco: Flow<Post> = flow { }
    var listClassescopast: Flow<MutableList<Post>> = flow { }
    var listClasses: PostLiveData = PostLiveData()
    var classList: MutableLiveData<MutableList<CRN>> = MutableLiveData()
    var userSUB: MutableLiveData<MutableList<String>> = MutableLiveData()
    var profilePosts: PostLiveData = PostLiveData()
    var newProfilePosts: Post? = null
    var comments: CommentLive = CommentLive()
    var newProfileComments: Comment? = null
    var finallist: MutableList<Post> = mutableListOf()


    private var getCommentsJob: Job? = null

    fun saveNewPost(text: String, title: String, CRN: String) =
        Firebase.saveNewPosttoUser(text, title, CRN)

    fun uploadUserProfileImage(selectedPhotoUri: Uri) =
        Firebase.uploadImageToFirebaseStorage(selectedPhotoUri)

    fun getSubscribedPosts() = Firebase.getSubscribedPosts()

    fun getCommunityPosts(className: String, callback: FlowCallback) {
        Firebase.getClassPosts(className, object : FirebaseCallbackPost {
            override fun onStart() {}
            override fun onFailure() {}
            override fun onSuccess(data: DataSnapshot) {
                val postData: Iterable<DataSnapshot> = data.child("Posts").children
                val postList: MutableList<Post> = mutableListOf()
                for (post in postData) {
                    val newPost = post.getValue(Post::class.java)
                    newPost?.let { postList.add(it) }
                }
                val listCor: Flow<Post> = postList.asFlow()
                callback.onFlow(listCor)
                val scope = CoroutineScope(Dispatchers.IO)
            }
        })
    }

    fun getPostComments(Key: String, subject: String, callbackComment: FirebaseData.FirebaseCallbackCommentFlow) {
        Firebase.getClassComments(Key, subject, object : PostRepository.FirebaseCallbackComment {
            override fun onStart() {
            }

            override fun onFailure() {
            }

            override fun onSuccess(data: DataSnapshot) {
                val commentsData: Iterable<DataSnapshot> = data.children
                val postComments: MutableList<Comment> = mutableListOf()
                for (comment in commentsData) {
                    val newComment = comment.getValue(Comment::class.java)
                    newComment?.let { postComments.add(it) }
                }

                val postCommentsCO: Flow<Comment> = postComments.asFlow()
                callbackComment.onCallback(postCommentsCO)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    postCommentsCO.collect { it ->
                        it.text
                        Log.d("soupcollect", "comm is $it")
                    }
                    val check = postCommentsCO.toList()
                    Log.d("souprepo", "start here")
                    for (item in check) {
                        var getext = item.text
                        Log.d("souprepo", "comm is $getext")
                    }
                    Log.d("souprepo", "end here")
                }
            }
        })
    }

    fun getUserProfileComments(userID: String, callbackComment: FirebaseData.FirebaseCallbackCommentFlow) {
        Firebase.getUserProfileComments(userID, object : FirebaseCallbackComment {
            override fun onFailure() {}
            override fun onStart() {}

            override fun onSuccess(data: DataSnapshot) {
                val commdetail: Iterable<DataSnapshot> = data.children
                val profileCommentList: MutableList<Comment> = mutableListOf()
                for (n in commdetail) {
                    val newComment = n.getValue(Comment::class.java)
                    newComment?.let { profileCommentList.add(it) }
                }

                val listCor: Flow<Comment> = profileCommentList.asFlow()
                callbackComment.onCallback(listCor)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    listCor.collect { it ->
                        it.text
                        Log.d("soupcollect", "comm is $it")
                    }
                    val check = listCor.toList()
                    Log.d("souprepo", "start here")
                    for (item in check) {
                        var getext = item.text
                        Log.d("souprepo", "comm is $getext")
                    }
                    Log.d("souprepo", "end here")
                }
                // comments.value = profileCommentLis
            }
        })
    }

    fun getUserProfilePosts(userID: String, callbackPost: FirebaseData.FirebaseCallbackPostFlow)  {
        Firebase.listenForUserProfilePosts(userID, object : FirebaseCallbackPost {
            override fun onFailure() {}
            override fun onStart() {}

            override fun onSuccess(data: DataSnapshot) {
                val dataProfilePosts: Iterable<DataSnapshot> = data.children
                val listProfilePosts: MutableList<Post> = mutableListOf()
                for(post in dataProfilePosts){
                    val userPost = post.getValue(Post::class.java)
                    userPost?.let { listProfilePosts.add(it) }
                }

                val listCor: Flow<Post> = listProfilePosts.asFlow()
                callbackPost.onCallback(listCor)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    listCor.collect { it ->
                        it.text
                        Log.d("soupcollect", "comm is $it")
                    }
                    val check = listCor.toList()
                    Log.d("souprepo", "start here")
                    for (item in check) {
                        var getext = item.text
                        Log.d("souprepo", "comm is $getext")
                    }
                    Log.d("souprepo", "end here")
                }

            }
        })
    }

    fun noCommentsCheckForCommPosts(subject: String, Key: String, callback: FirebaseCallbackNoComments) =
        Firebase.noCommentsCheckerForCommPosts(subject, Key, callback)

    fun saveNewImgPosttoUser(title: String, text: String, CRN: String, uri: Uri, imagePost: Boolean) =
        Firebase.saveNewImgPosttoUser(title, text, CRN, uri, imagePost)


    fun newComment(PKey: String, Comment: String, Classkey: String, UserID: String, crn: String) {
        Firebase.saveNewComment(Comment, PKey, Classkey, UserID, crn)
    }

    fun deleteNewPost(postKey: String, crn: String, userID: String) {
        Firebase.deleteNewPost(postKey, crn, userID)
    }

    fun deleteNewCommentFromUserProfile(ClassKey: String, crn: String, comKey: String, userID: String) {
        Firebase.deleteNewCommentFromUserProfile(ClassKey, crn, comKey, userID)
    }

    fun deleteNewCommentFromCommPosts(postKey: String, crn: String, classkey: String) {
        Firebase.deleteNewCommentFromCommPosts(postKey, crn, classkey)
    }

    fun editNewComment(userID: String, usercomkey: String, ntext: String, crn: String, postKey: String) {
        Firebase.editComment(userID, usercomkey, ntext, crn, postKey)
    }


    fun editPost(crn: String, postKey: String, ctext: String, ctitle: String, ntext: String, ntitle: String, userID: String) {
        Firebase.editPost(crn, postKey, ctext, ctitle, ntext, ntitle, userID)
    }

    fun blockUser(UserID: String) {
        Firebase.blockUser(UserID)
    }

    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String) {
        Firebase.reportUserPost(accusedID, complaintext, crn, classkey)

    }

    fun reportUserComment(accusedID: String, complaintext: String, crn: String, classkey: String, comKey: String) {
        Firebase.reportUserComment(accusedID, complaintext, crn, classkey, comKey)

    }

    fun fetchCurrentUserName() = Firebase.fetchCurrentUserName()

    fun currentUser() = Firebase.currentUser()



    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }

    fun getsublist2(): MutableLiveData<MutableList<String>> {
        val subList: MutableList<String> = mutableListOf()
        Firebase.listenUserSub(object : FirebaseCallbackString {
            override fun onFailure() {}

            override fun onStart() {}

            override fun onSuccess(data: DataSnapshot) {
                val size = data.hasChildren()
                Log.d("Size", size.toString())
                //var has :HashMap<String,String>? = hashMapOf()
                val sublist = data.children
                for (x in sublist) {
                    Log.d("usersub", x.getValue(String::class.java)!!)
                    subList.add(x.getValue(String::class.java)!!)
                }
                userSUB.value = subList
            }
        })

        return userSUB
    }

    fun remUsersub(crn: String) {
        Firebase.removeUserSub(crn)
        Firebase.removeClassSub(crn)
    }

    fun getclassnamesforusername() = Firebase.getclassnamesforusername()

    fun sendClassnameForUsername() = Firebase.sendClassnameForUsername()



    fun readPhotoValue(useridm: String, callback: EmailCallback) {
        Firebase.readPhotoValue(useridm, callback)
    }

    fun fetchEmail(UserID: String, callbackItem: FirebaseCallbackItem) =
        Firebase.fetchEmail(UserID, callbackItem)

    fun saveNewUsername(username: String) = Firebase.saveNewUsername(username)

    fun saveUserbio(bio: String) = Firebase.saveUserbio(bio)

    fun fetchBio(UserID: String, callbackItem: FirebaseCallbackItem) =
        Firebase.fetchBio(UserID, callbackItem)

    fun fetchUsername(UserID: String, callbackItem: FirebaseCallbackItem) =
        Firebase.fetchUsername(UserID, callbackItem)

    fun noPostsChecker(UserID: String, callbackBool: FirebaseCallbackBool) =
        Firebase.noPostsChecker(UserID, callbackBool)

    fun noCommentsChecker(UserID: String, callbackBool: FirebaseCallbackBool) =
        Firebase.noCommentsChecker(UserID, callbackBool)


    companion object {
        @Volatile
        private var instance: PostRepository? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(firebasedata).also { instance = it }
            }
    }


    interface FirebaseCallbackPost {

        fun onStart()
        fun onFailure()
        fun onSuccess(data: DataSnapshot)
    }

    interface FirebaseCallbackBool {

        fun onStart()
        fun onFailure()
        fun onSuccess(data: DataSnapshot): Boolean
    }

    interface FirebaseCallbackNoComments {
        fun onEmpty(nocomlist: Boolean)
        fun onFull(comlistpresent: Boolean)
    }

    interface FirebaseCallbackComment {
        fun onStart()
        fun onFailure()
        fun onSuccess(data: DataSnapshot)
    }

    interface FirebaseCallbackCRN {
        fun onStart()
        fun onFailure()
        fun onSuccess(data: DataSnapshot)
    }

    interface FirebaseCallbackString {
        fun onStart()
        fun onFailure()
        fun onSuccess(data: DataSnapshot)
    }

    interface FirebaseCallbackItem {
        fun onStart()
        fun onFailure()
        fun onMessage(data: DataSnapshot): String
    }


}

