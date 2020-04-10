package com.example.seniorproject.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.Utils.FlowCallback
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.interfaces.FirebaseValuecallback
import com.example.seniorproject.data.models.*
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
    //calls corresponding function from firebase file
    fun saveNewPost(text: String, title: String, CRN: String) = Firebase.saveNewPosttoUser(text, title, CRN)
    //calls corresponding function from firebase file
    fun uploadUserProfileImage(selectedPhotoUri: Uri) =
        Firebase.uploadImageToFirebaseStorage(selectedPhotoUri)

    suspend fun getSubscribedPosts(value : List<String>) : Flow<Post> = flow {
        var postL : MutableList<Post> = mutableListOf()
        var limit = getPostperclass(value.size)
        var onSuccessJob : Job? = null

        var job = CoroutineScope(Dispatchers.IO).launch {
            for (n in value)
            {
                Firebase.getOneClass(n, object : FirebaseValuecallback
                {
                    override fun onFailure() {

                    }

                    override fun onStart() {

                    }

                    override fun onSuccess(data: DataSnapshot)  {
                      launch(Dispatchers.Default) {
                            for (n in data.children)
                            {
                                var p = Post()
                                var count = 0
                                p.let {
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
                                }
                                postL.add(p)
                                count++
                                if(count <= limit)
                                {
                                    break
                                }
                            }
                        }
                    }
                })

            }
            kotlinx.coroutines.delay(1000)

        }

        job.join()
        onSuccessJob?.join()
        for (n in postL)
        {
            emit(n)
        }

    }


   suspend fun getUsersSubs() : Flow<String> = flow {
        var SubList : MutableList<String> = mutableListOf()
       var ob : Job? = null
      var send = object : FirebaseValuecallback {
           override fun onFailure() {

           }

           override fun onStart() {

           }



           override fun onSuccess(data: DataSnapshot)  {
               val size = data.hasChildren()
               Log.d("Size", size.toString())
               //var has :HashMap<String,String>? = hashMapOf()
               val sublist = data.children
               for (x in sublist) {
                   Log.d("usersub", x.getValue(String::class.java)!!)
                   SubList.add(x.getValue(String::class.java)!!)
               }

           }
       }
      var job = CoroutineScope(Dispatchers.IO). launch {
         var ob = Firebase.getUserSub(send)

          kotlinx.coroutines.delay(1000)
        }
       job.join()
            Log.d("Before For", SubList.size.toString())
            for (n in SubList) {
                Log.d("emitting", n)
                emit(n)
            }




    }
/*This function is used to find out how many posts per class the application should grab based on the number of communities the user is a part of  */
    fun getPostperclass(f : Int) : Int
    {
        if(f <= 2)
        {
            return 5
        }
        else if (f in 3..5)
        {
            return 3
        }
        else if (f > 5)
        {
            return 2
        }
        return 1

    }

    fun getClassComments(Key: String, subject: String, callbackComment: FirebaseData.FirebaseCallbackCommentFlow){
        Firebase.getClassComments(Key, subject, object: PostRepository.FirebaseCallbackComment{
            override fun onStart() {
            }

            override fun onFailure() {
            }

            override fun onSuccess(data: DataSnapshot) {
                val commdetail: Iterable<DataSnapshot> = data.children
                val profileCommentList : MutableList<Comment> = mutableListOf()
                for(n in commdetail){
                    val newComment = Comment()
                    //  try {
                    newComment.let {
                        it.text = n.child("text").value.toString()
                        it.Classkey = n.child("Classkey").value.toString()
                        it.PosterID = n.child("PosterID").value.toString()
                        it.Postkey = n.child("Postkey").value.toString()
                        it.profileComKey = n.child("ProfileComKey").value.toString()
                        it.Ptime = n.child("Ptime").value.toString()
                        it.userComkey = n.child("UserComkey").value.toString()
                        it.author = n.child("author").value.toString()
                        it.crn = n.child("crn").value.toString()

                        profileCommentList.add(newComment)
                    }
                    //  } catch (e: Exception) {
                    Log.d("Data Error", "error converting to post")
                    //   }
                }

                val listCor : Flow<Comment> = profileCommentList.asFlow()
                callbackComment.onCallback(listCor)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    listCor.collect {
                        it -> it.text
                        Log.d("soupcollect", "comm is $it")
                    }
                    val check = listCor.toList()
                    Log.d("souprepo", "start here")
                    for(item in check){
                        var getext = item.text
                        Log.d("souprepo", "comm is $getext")
                    }
                    Log.d("souprepo", "end here")
                }
                // comments.value = profileCommentLis
            }
        })
    }
    //calls corresponding function from firebase file
    fun noCommentsCheckForCommPosts(
        subject: String,
        Key: String,
        callback: FirebaseCallbackNoComments
    ) = Firebase.noCommentsCheckerForCommPosts(subject, Key, callback)
    //calls corresponding function from firebase file
    fun saveNewImgPosttoUser(
        title: String,
        text: String,
        CRN: String,
        uri: Uri,
        imagePost: Boolean
    ) = Firebase.saveNewImgPosttoUser(title, text, CRN, uri, imagePost)

    //calls corresponding function from firebase file
    fun newComment(PKey: String, Comment: String, Classkey: String, UserID: String, crn: String) {
        Firebase.saveNewComment(Comment, PKey, Classkey, UserID, crn)
    }
    //calls corresponding function from firebase file
    fun deleteNewPost(postKey: String, crn: String, userID: String) {
        Firebase.deleteNewPost(postKey, crn, userID)
    }
    //calls corresponding function from firebase file
    fun deleteNewCommentFromUserProfile(
        ClassKey: String,
        crn: String,
        comKey: String,
        userID: String
    ) {
        Firebase.deleteNewCommentFromUserProfile(ClassKey, crn, comKey, userID)
    }
    //calls corresponding function from firebase file
    fun deleteNewCommentFromCommPosts(postKey: String, crn: String, classkey: String) {
        Firebase.deleteNewCommentFromCommPosts(postKey, crn, classkey)
    }
    //calls corresponding function from firebase file
    fun editNewComment(
        userID: String,
        usercomkey: String,
        ntext: String,
        crn: String,
        postKey: String
    ) {
        Firebase.editComment(userID, usercomkey, ntext, crn, postKey)
    }

    //calls corresponding function from firebase file
    fun editPost(
        crn: String, postKey: String, ctext: String, ctitle: String, ntext: String, ntitle: String,
        userID: String
    ) {
        Firebase.editPost(crn, postKey, ctext, ctitle, ntext, ntitle, userID)
    }
    //calls corresponding function from firebase file
    fun blockUser(UserID: String) {
        Firebase.blockUser(UserID)
    }

    fun getTheUserProfileComments(userID: String, callbackComment: FirebaseData.FirebaseCallbackCommentFlow){
        Firebase.getTheUserProfileComments(userID, object: FirebaseCallbackComment{
            override fun onFailure() {}

            override fun onStart() {}

            override fun onSuccess(data: DataSnapshot) {
                val commdetail: Iterable<DataSnapshot> = data.children
                val profileCommentList : MutableList<Comment> = mutableListOf()
                for(n in commdetail){
                    val newComment = Comment()
                    //  try {
                    newComment.let {
                        it.text = n.child("text").value.toString()
                        it.Classkey = n.child("Classkey").value.toString()
                        it.PosterID = n.child("PosterID").value.toString()
                        it.Postkey = n.child("Postkey").value.toString()
                        it.profileComKey = n.child("ProfileComKey").value.toString()
                        it.Ptime = n.child("Ptime").value.toString()
                        it.userComkey = n.child("UserComkey").value.toString()
                        it.author = n.child("author").value.toString()
                        it.crn = n.child("crn").value.toString()

                        profileCommentList.add(newComment)
                    }
                    //  } catch (e: Exception) {
                    Log.d("Data Error", "error converting to post")
                    //   }
                }

                val listCor : Flow<Comment> = profileCommentList.asFlow()
                callbackComment.onCallback(listCor)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    listCor.collect {
                            it -> it.text
                        Log.d("soupcollect", "comm is $it")
                    }
                    val check = listCor.toList()
                    Log.d("souprepo", "start here")
                    for(item in check){
                        var getext = item.text
                        Log.d("souprepo", "comm is $getext")
                    }
                    Log.d("souprepo", "end here")
                }
                // comments.value = profileCommentLis
            }
        })
    }
    //calls corresponding function from firebase file
    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String) {
        Firebase.reportUserPost(accusedID, complaintext, crn, classkey)

    }
    //calls corresponding function from firebase file
    fun reportUserComment(
        accusedID: String,
        complaintext: String,
        crn: String,
        classkey: String,
        comKey: String
    ) {
        Firebase.reportUserComment(accusedID, complaintext, crn, classkey, comKey)

    }
    //calls corresponding function from firebase file
    fun fetchCurrentUserName() = Firebase.fetchCurrentUserName()
    //calls corresponding function from firebase file
    fun currentUser() = Firebase.currentUser()

    fun getClassPostsco(className: String, callback: FlowCallback) {
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
                        it.Ptime = n.child("Timestamp").getValue(String::class.java)
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
                val listCor: Flow<Post> = savedPostsList.asFlow()
                callback.onFlow(listCor)
                val scope = CoroutineScope(Dispatchers.IO)
                /*scope.launch {
                    listCor.collect {
                        Log.d("soupcollect", "post is $it")
                    }
                    val check = listCor.toList()
                    Log.d("souprepo", "start here")
                    for (item in check) {
                        Log.d("souprepo", "post is $item")
                    }
                    Log.d("souprepo", "end here")
                }*/

            }
        })
    }
    //calls corresponding function from firebase file
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
    //calls corresponding function from firebase file
    fun getclassnamesforusername() = Firebase.getclassnamesforusername()
    //calls corresponding function from firebase file
    fun sendClassnameForUsername() = Firebase.sendClassnameForUsername()

    fun getUserProfilePosts(userID: String): PostLiveData {
        val profilePostsList: MutableList<Post> = mutableListOf()
        Firebase.listenForUserProfilePosts(userID, object : FirebaseCallbackPost {
            override fun onFailure() {}
            override fun onStart() {}

            override fun onSuccess(data: DataSnapshot) {
                if (!data.child("text").exists()) {
                    Log.d("post", "doesn't exist")
                    val emptyPost = Post("No Posts", "", "", "")
                    val profilePostL: MutableList<Post> = mutableListOf()
                    profilePostL.add(emptyPost)
                    profilePosts.value = profilePostL
                } else if (data.child("text").exists()) {
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
                            it.Ptime = data.child("Ptime").value.toString()
                            it.author = data.child("author").value.toString()
                            it.uri = data.child("uri").value.toString()
                            // not setting author
                        }
                    } catch (e: Exception) {
                        Log.d("Data Error", "error converting to post")
                    }

                    if (newProfilePost.key != null) {
                        Log.d(
                            "profileposts",
                            newProfilePost.title ?: " Accessing profile post title"
                        )
                        Log.d("profileposts", newProfilePost.text ?: " Accessing profile post text")
                        Log.d("profileposts", newProfilePost.key ?: " Accessing profile post title")
                        Log.d(
                            "profileposts",
                            newProfilePost.classkey ?: " Accessing profile post title"
                        )
                        Log.d(
                            "profileposts",
                            newProfilePost.userID ?: " Accessing profile post title"
                        )
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

    //calls corresponding function from firebase file
    fun readPhotoValue(useridm: String, callback: EmailCallback) {
        Firebase.readPhotoValue(useridm, callback)
    }

    //calls corresponding function from firebase file
    fun fetchEmail(UserID: String, callbackItem: FirebaseCallbackItem) =
        Firebase.fetchEmail(UserID, callbackItem)

    //calls corresponding function from firebase file
    fun saveNewUsername(username: String) = Firebase.saveNewUsername(username)

    //calls corresponding function from firebase file
    fun saveUserbio(bio: String) = Firebase.saveUserbio(bio)

    //calls corresponding function from firebase file
    fun fetchBio(UserID: String, callbackItem: FirebaseCallbackItem) =
        Firebase.fetchBio(UserID, callbackItem)

    //calls corresponding function from firebase file
    fun fetchUsername(UserID: String, callbackItem: FirebaseCallbackItem) =
        Firebase.fetchUsername(UserID, callbackItem)

    //calls corresponding function from firebase file
    fun noPostsChecker(UserID: String, callbackBool: FirebaseCallbackBool) =
        Firebase.noPostsChecker(UserID, callbackBool)

    //calls corresponding function from firebase file
    fun noCommentsChecker(UserID: String, callbackBool: FirebaseCallbackBool) =
        Firebase.noCommentsChecker(UserID, callbackBool)

    //calls corresponding function from firebase file
    fun checkSubscription(className: String, callbacksubBool: FirebaseCallbacksubBool) =
        Firebase.checkSubscription(className,callbacksubBool)


    companion object {
        @Volatile
        private var instance: PostRepository? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(firebasedata).also { instance = it }
            }
    }

//firebase interface used with data snapshots to move data in realtime through MVVM architechture
    interface FirebaseCallbackPost {

        fun onStart()
        fun onFailure()
        fun onSuccess(data: DataSnapshot)
    }

    interface FirebaseCallbacksubBool {
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
    interface FirebaseCallbackSubs {
        fun onFailure()
        fun onStart()
        fun onSuccess(data: List<String>)
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

