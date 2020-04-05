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
    var listClassesco: Flow<Post> = flow{ }
    var listClassescopast: Flow<MutableList<Post>> = flow{ }
    var listClasses: PostLiveData = PostLiveData()
    var classList: MutableLiveData<MutableList<CRN>> = MutableLiveData()
    var userSUB: MutableLiveData<MutableList<String>> = MutableLiveData()
    var profilePosts: PostLiveData = PostLiveData()
    var newProfilePosts: Post? = null
    var comments : CommentLive = CommentLive()
    var newProfileComments: Comment? = null
    var finallist: MutableList<Post> = mutableListOf()


    private var getCommentsJob: Job? = null

    fun saveNewPost(text: String, title: String, CRN: String) = Firebase.saveNewPosttoUser(text, title, CRN)

    fun uploadUserProfileImage(selectedPhotoUri: Uri) =
        Firebase.uploadImageToFirebaseStorage(selectedPhotoUri)

    fun getSubscribedPosts() = Firebase.getSubscribedPosts()

    fun getComments(ClassKey: String, subject: String, callback: FirebaseData.FirebaseCallbackCommentFlow){
        Firebase.listenComments(ClassKey, subject, callback)
    }

    fun noCommentsCheckForCommPosts(subject: String, Key: String, callback: PostRepository.FirebaseCallbackNoComments)
            = Firebase.noCommentsCheckerForCommPosts(subject, Key,callback)

    fun saveNewImgPosttoUser(title : String, text:String, CRN: String, uri: Uri, imagePost : Boolean)
    = Firebase.saveNewImgPosttoUser(title,text,CRN,uri,imagePost)


    fun newComment(PKey: String, Comment: String, Classkey: String, UserID: String, crn: String) {
        Firebase.saveNewComment(Comment, PKey, Classkey, UserID, crn)
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

    fun editNewComment(
        userID: String,
        usercomkey: String,
        ntext: String,
        crn: String,
        postKey: String
    ) {
        Firebase.editComment(userID, usercomkey, ntext, crn, postKey)
    }


    fun editPost(crn: String, postKey: String, ctext: String, ctitle: String, ntext: String, ntitle: String,
                 userID: String){
        Firebase.editPost(crn, postKey,ctext,ctitle, ntext, ntitle, userID)
    }

    fun blockUser(UserID: String){
        Firebase.blockUser(UserID)
    }


    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String){
        Firebase.reportUserPost(accusedID, complaintext, crn, classkey)

    }

    fun reportUserComment(accusedID: String, complaintext: String, crn: String, classkey: String, comKey: String){
        Firebase.reportUserComment(accusedID, complaintext, crn, classkey, comKey)

    }
    fun fetchCurrentUserName() = Firebase.fetchCurrentUserName()

    fun currentUser() = Firebase.currentUser()

    fun getClassPostsco(className: String, callback: FlowCallback){
        Firebase.getClassPosts(className, object : FirebaseCallbackPost {
            override fun onStart() {
            }

            override fun onFailure() {
            }

            override fun onSuccess(data: DataSnapshot) {
                val postdetail: Iterable<DataSnapshot> = data.child("Posts").children
                val savedPostsList: MutableList<Post> = mutableListOf()
              // var listCor : Flow<Post> = flow {
                    for (n in postdetail) {
                        val newPost = Post()
                        newPost.let {

                            //Log.d("ACCESSING", newPost?.text)
                            it.title = n.child("title").getValue(String::class.java)
                            //Log.d("Community post", data.child("title").getValue(String::class.java))
                            it.text = n.child("text").getValue(String::class.java)
                            //newPost.author = pos.child("author").getValue(String::class.java)
                            //it.subject = className
                            Log.d("CRN", data.key!!)
                            it.subject = n.child("subject").getValue(String::class.java)
                            it.Ptime =  n.child("Timestamp").getValue(String::class.java)
                            it.key = n.child("key").getValue(String::class.java)
                            it.Ptime = n.child("Ptime").getValue(String::class.java)
                            it.classkey = n.child("Classkey").getValue(String::class.java)
                            it.userID = n.child("UserID").getValue(String::class.java)
                            it.author = n.child("author").getValue(String::class.java)
                            it.uri = n.child("uri").getValue(String::class.java)

                            savedPostsList.add(newPost)

                           // emit(newPost)
                        }
                    }
              //  }
                val listCor : Flow<Post> = savedPostsList.asFlow()
                callback.onFlow(listCor)
                val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                listCor.collect {
                    Log.d("soupcollect", "post is $it")
                }
            val check = listCor.toList()
            Log.d("souprepo", "start here")
            for(item in check){
                Log.d("souprepo", "post is $item")
            }
            Log.d("souprepo", "end here")
            }
                //when i convert listCor to list from here it returns only last post
                /*
                    var check = listCor.toList()
                    Log.d("soupfinal","start here")
                    for(item in check){
                        Log.d("souplist","post is $item")
                    }
                    Log.d("soupfinal","end here")
                }*/
                //i can collect in realtime fine here, but when i add each item to arraylist of posts on collect
                //and then loop inside the arraylist it returns only last post.
            }
        })
    }

    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }

    fun getsublist2() : MutableLiveData<MutableList<String>>
    {
        val subList : MutableList<String> = mutableListOf()
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

    fun getUserProfilePosts(userID: String) : PostLiveData {
        val profilePostsList: MutableList<Post> = mutableListOf()
        Firebase.listenForUserProfilePosts(userID, object : FirebaseCallbackPost{
            override fun onFailure() {}
            override fun onStart() {}

            override fun onSuccess(data: DataSnapshot) {
                if (!data.child("text").exists()) {
                    Log.d("post", "doesn't exist")
                    val emptyPost = Post("No Posts","" ,"","")
                    val profilePostL: MutableList<Post> = mutableListOf()
                    profilePostL.add(emptyPost)
                    profilePosts.value = profilePostL
                }
                else if (data.child("text").exists()) {
                    //noPostsCheck = false
                    val newProfilePost = Post()
                    try {
                        newProfilePost.let {
                            it.text = data.child("text").value.toString()
                            it.title = data.child("title").value.toString()
                            it.key = data.child("key").value.toString()
                            // class key is key for this post
                            it.classkey = data.child("Classkey").value.toString()
                            // user who posted id
                            it.userID = data.child("UserID").value.toString()
                            // need to change this later subject should be subject crn should be different
                            it.subject = data.child("subject").value.toString()
                            it.Ptime=data.child("Ptime").value.toString()
                            it.author = data.child("author").value.toString()
                            it.uri = data.child("uri").value.toString()
                            // not setting author
                        }
                    } catch (e: Exception) {
                        Log.d("Data Error", "error converting to post")
                    }

                    if (newProfilePost.key != null) {
                        Log.d("profileposts", newProfilePost.title ?: " Accessing profile post title")
                        Log.d("profileposts", newProfilePost.text ?: " Accessing profile post text")
                        Log.d("profileposts", newProfilePost.key ?: " Accessing profile post title")
                        Log.d("profileposts", newProfilePost.classkey ?: " Accessing profile post title")
                        Log.d("profileposts", newProfilePost.userID ?: " Accessing profile post title")
                        Log.d("profileposts", newProfilePost.crn ?: " Accessing profile post title")

                        profilePostsList.add(newProfilePost)
                        Log.d("profileposts", newProfilePost.key ?: " Accessing profile post title")
                        newProfilePosts = newProfilePost

                    }
                    profilePosts.value = profilePostsList
                }
            }
        })
        return profilePosts
    }

    fun readPhotoValue(useridm: String, callback: EmailCallback) {
        Firebase.readPhotoValue(useridm, callback)
    }

    fun getUserProfileComments(userID: String) : CommentLive {
        val profileCommentList : MutableList<Comment> = mutableListOf()
        Firebase.listenForUserProfileComments(userID, object :FirebaseCallbackComment {
            override fun onFailure() {}
            override fun onStart() {}
            override fun onSuccess(data: DataSnapshot) {
                if (!data.child("text").exists()) {
                    Log.d("comment", "doesn't exist")
                    val emptyComment = Comment("No Comments","" ,"","","")
                    profileCommentList.add(emptyComment)
                    comments.value = profileCommentList
                }
                else if (data.child("text").exists()) {
                    val newComment = Comment()
                    try {
                        newComment.let {
                            it.text = data.child("text").value.toString()
                            it.classkey = data.child("Classkey").value.toString()
                            it.PosterID = data.child("PosterID").value.toString()
                            it.Postkey = data.child("Postkey").value.toString()
                            it.profileComKey = data.child("ProfileComKey").value.toString()
                            it.Ptime = data.child("Ptime").value.toString()
                            it.userComkey = data.child("UserComkey").value.toString()
                            it.author = data.child("author").value.toString()
                            it.crn = data.child("crn").value.toString()
                        }
                    } catch (e: Exception) {
                        Log.d("Data Error", "error converting to post")
                    }
                        profileCommentList.add(newComment)
                        newProfileComments = newComment
                    }
                    comments.value = profileCommentList
                }
    })
        return comments
    }

    fun fetchEmail(UserID: String,callbackItem : FirebaseCallbackItem) = Firebase.fetchEmail(UserID,callbackItem)

    fun saveNewUsername(username: String) = Firebase.saveNewUsername(username)

    fun saveUserbio(bio : String) = Firebase.saveUserbio(bio)

    fun fetchBio(UserID: String,callbackItem: FirebaseCallbackItem) = Firebase.fetchBio(UserID,callbackItem)

    //fun fetchCurrentBio() = Firebase.fetchCurrentBio()

    fun noPostsChecker(UserID: String, callbackBool: FirebaseCallbackBool) = Firebase.noPostsChecker(UserID, callbackBool)

    fun noCommentsChecker(UserID: String, callbackBool: FirebaseCallbackBool) = Firebase.noCommentsChecker(UserID, callbackBool)




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
        fun onSuccess(data: DataSnapshot) : Boolean
    }

    interface FirebaseCallbackNoComments {
        fun onEmpty(nocomlist: Boolean)
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
        fun onMessage(data: DataSnapshot) : String
    }


}

