package com.example.seniorproject.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.Utils.FlowCallback
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.interfaces.*
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.Post
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
    var userSUB: MutableLiveData<MutableList<String>> = MutableLiveData()

    //calls corresponding function from firebase file
    fun saveNewPost(text: String, title: String, CRN: String) = Firebase.saveNewPosttoUser(text, title, CRN)
    //calls corresponding function from firebase file

    ////calls corresponding function from firebase file
    fun uploadUserProfileImage(selectedPhotoUri: Uri) =
        Firebase.uploadImageToFirebaseStorage(selectedPhotoUri)


/*This Function is called from getSubsP in HomeframentViewModel it uses the list of subs gotten for the current user to grab the recent posts from
each class the user is subscribed to this uses coroutines */
    suspend fun getSubscribedPosts(value : List<String>) : Flow<Post> = flow {
        val postL : MutableList<Post> = mutableListOf()
        val limit = getPostperclass(value.size)
        val onSuccessJob : Job? = null

        val job = CoroutineScope(Dispatchers.IO).launch {
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
                          var count = 0

                            for (n in data.children.reversed())
                            {
                                val p = Post()

                                p.let {
                                    it.title = n.child("title").getValue(String::class.java)
                                    //Log.d("Community post", data.child("title").getValue(String::class.java))
                                    it.text = n.child("text").getValue(String::class.java)
                                    //newPost.author = pos.child("author").getValue(String::class.java)
                                    //it.subject = className
                                    Log.d("What", n.child("title").getValue(String::class.java)!!)
                                    Log.d("Count", count.toString())
                                    it.subject = n.child("subject").getValue(String::class.java)
                                    it.Ptime =  n.child("Timestamp").getValue(String::class.java)
                                    it.key = n.child("key").getValue(String::class.java)
                                    it.Ptime = n.child("Ptime").getValue(String::class.java)
                                    it.Classkey = n.child("Classkey").getValue(String::class.java)
                                    it.UserID = n.child("UserID").getValue(String::class.java)
                                    it.author = n.child("author").getValue(String::class.java)
                                    it.uri = n.child("uri").getValue(String::class.java)
                                }
                                postL.add(p)
                                count++
                                if(count >= limit)
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

/*This function gets the users list of subs from the firebase database by using the FirebaseData function getUserSub  and then once all the values are gotten it will emit the results using kotlin flow to create a
* cold flow pipeline so that the calling function getSubsP in HomeFragmentViewModel can then use those values to get the post for those specific classes */
   suspend fun getUsersSubs() : Flow<String> = flow {
        val subList : MutableList<String> = mutableListOf()
       var ob : Job? = null
      val send = object : FirebaseValuecallback {
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
                   subList.add(x.getValue(String::class.java)!!)

               }

           }
       }
      val job = CoroutineScope(Dispatchers.IO). launch {
         var ob = Firebase.getUserSub(send)

          kotlinx.coroutines.delay(1000)
        }
       job.join()
            Log.d("Before For", subList.size.toString())
            for (n in subList) {
                Log.d("emitting", n)
                emit(n)
            }
    }

/*This function is used to find out how many posts per class the application should grab based on the number of communities the user is a part of  */
private fun getPostperclass(f : Int) : Int
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

    //calls corresponding function from firebase file. Creates Kotlin Flow of posts found in the community posts screen with .asFlow
    //extension. Flow travels upstream through MVVM using the callback.
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

    //calls corresponding function from firebase file. Creates Kotlin Flow of comments found in the clicked post screen with .asFlow
    //extension. Flow travels upstream through MVVM using the callback
    fun getPostComments(Key: String, subject: String, callbackComment: FirebaseCallbackCommentFlow) {
        Firebase.getClassComments(Key, subject, object : FirebaseCallbackComment {
            override fun onStart() {
            }

            override fun onFailure() {
            }

            override fun onSuccess(data: DataSnapshot) {
                val commdetail: Iterable<DataSnapshot> = data.children
                val profileCommentList : MutableList<Comment> = mutableListOf()
                val comment = Comment("no comment", "", "", "", "")
                profileCommentList.add(comment)
                for(n in commdetail){
                    val newComment = Comment()

                    newComment.let {
                        it.text = n.child("text").value.toString()
                        it.classkey = n.child("Classkey").value.toString()
                        it.PosterID = n.child("PosterID").value.toString()
                        it.Postkey = n.child("Postkey").value.toString()
                        it.profileComKey = n.child("ProfileComKey").value.toString()
                        it.Ptime = n.child("Ptime").value.toString()
                        it.userComkey = n.child("UserComkey").value.toString()
                        it.author = n.child("author").value.toString()
                        it.crn = n.child("crn").value.toString()

                        profileCommentList.add(newComment)
                    }


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

                    }

                }

            }
        })
    }

    //calls corresponding function from firebase file. Creates Kotlin Flow of posts in user profile with .asFlow extension. Flow
    //travels upstream through MVVM using the callback.
    fun getUserProfilePosts(userID: String, callbackPost: FirebaseCallbackPostFlow)  {
        Firebase.getUserProfilePosts(userID, object : FirebaseCallbackPost {
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
            }
        })
    }

    //calls corresponding function from firebase file. Grabs data snapshot of subscribed classes and adds them to list
    //puts that mutable list into live data to be observed.
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

    //calls corresponding function from firebase file
    fun noCommentsCheckForCommPosts(subject: String, Key: String, callback: FirebaseCallbackNoComments) =
        Firebase.noCommentsCheckerForCommPosts(subject, Key, callback)

    //calls corresponding function from firebase file
    fun saveNewImgPosttoUser(title: String, text: String, CRN: String, uri: Uri, imagePost: Boolean) =
        Firebase.saveNewImgPosttoUser(title, text, CRN, uri, imagePost)

    //calls corresponding function from firebase file
    fun newComment(PKey: String, Comment: String, Classkey: String, UserID: String, crn: String) {
        Firebase.saveNewComment(Comment, PKey, Classkey, UserID, crn)
    }

    //calls corresponding function from firebase file
    fun deleteNewPost(postKey: String, crn: String, userID: String) {
        Firebase.deleteNewPost(postKey, crn, userID)
    }

    //calls corresponding function from firebase file
    fun deleteNewCommentFromUserProfile(ClassKey: String, crn: String, comKey: String, userID: String) {
        Firebase.deleteNewCommentFromUserProfile(ClassKey, crn, comKey, userID)
    }

    //calls corresponding function from firebase file
    fun deleteNewCommentFromCommPosts(postKey: String, crn: String, classkey: String) {
        Firebase.deleteNewCommentFromCommPosts(postKey, crn, classkey)
    }

    //calls corresponding function from firebase file
    fun editNewComment(userID: String, usercomkey: String, ntext: String, crn: String, postKey: String) {
        Firebase.editComment(userID, usercomkey, ntext, crn, postKey)
    }

    //calls corresponding function from firebase file
    fun editPost(crn: String, postKey: String, ctext: String, ctitle: String, ntext: String, ntitle: String, userID: String) {
        Firebase.editPost(crn, postKey, ctext, ctitle, ntext, ntitle, userID)
    }

    //calls corresponding function from firebase file
    fun blockUser(UserID: String) {
        Firebase.blockUser(UserID)
    }


    fun getBlockedUsers(callback: FirebaseCallbackUserListFlow){
        Firebase.getBlockedUsers(object: FirebaseCallbackString{
            override fun onStart() {
            }

            override fun onFailure() {
            }

            override fun onSuccess(data: DataSnapshot) {
                val blockedUserList : MutableList<String> = mutableListOf()
                for(block in data.children){
                    blockedUserList.add(block.value as String)
                }
                var listCor = blockedUserList.asFlow()
                callback.onFlow(listCor)
            }

        })
    }


    fun getUserProfileComments(userID: String, callbackComment: FirebaseCallbackCommentFlow){
        Firebase.getUserProfileComments(userID, object: FirebaseCallbackComment{
            override fun onFailure() {}

            override fun onStart() {}

            override fun onSuccess(data: DataSnapshot) {
                val commdetail: Iterable<DataSnapshot> = data.children
                val profileCommentList : MutableList<Comment> = mutableListOf()
                for(n in commdetail){
                    val newComment = Comment()
                    newComment.let {
                        it.text = n.child("text").value.toString()
                        it.classkey = n.child("Classkey").value.toString()
                        it.PosterID = n.child("PosterID").value.toString()
                        it.Postkey = n.child("Postkey").value.toString()
                        it.profileComKey = n.child("ProfileComKey").value.toString()
                        it.Ptime = n.child("Ptime").value.toString()
                        it.userComkey = n.child("UserComkey").value.toString()
                        it.author = n.child("author").value.toString()
                        it.crn = n.child("crn").value.toString()

                        profileCommentList.add(newComment)
                    }
                }

                val listCor : Flow<Comment> = profileCommentList.asFlow()
                callbackComment.onCallback(listCor)
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


    //calls corresponding functions from firebase file
    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }


    //calls corresponding function from firebase file
    fun remUsersub(crn: String) {
        Firebase.removeUserSub(crn)
        Firebase.removeClassSub(crn)
    }

    //calls corresponding function from firebase file
    fun getclassnamesforusername() = Firebase.getclassnamesforusername()


    //calls corresponding function from firebase file
    fun sendClassnameForUsername() = Firebase.sendClassnameForUsername()



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
    fun checkSubscription(className: String, callbacksubBool: FirebaseCallbacksubBool) =
        Firebase.checkSubscription(className,callbacksubBool)



    companion object {
        @Volatile
        private var instance: PostRepository? = null

        //function is created to work with dagger injection in creating the viewmodel factories for the project. See InjectorUtils
        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(firebasedata).also { instance = it }
            }
    }





}

