package com.example.seniorproject.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.seniorproject.data.Firebase.FirebaseData

import com.example.seniorproject.data.models.*
import com.example.seniorproject.viewModels.CommunityPostViewModel
import com.google.firebase.database.DataSnapshot
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
    var SessionUser: User? = null
    var listClasses: PostLiveData = PostLiveData()
    var classList: MutableLiveData<MutableList<CRN>> = MutableLiveData()
    var UserSUB: MutableLiveData<MutableList<String>> = MutableLiveData()
    var profilePosts: PostLiveData = PostLiveData()
    var newProfilePosts: Post? = null
    var Comments : CommentLive = CommentLive()
    var newProfileComments: Comment? = null
    var savedPostsList: MutableList<Post> = mutableListOf()


    private var getCommentsJob: Job? = null

    fun saveNewPost(text: String, title: String, CRN: String) {
        Firebase.saveNewPosttoUser(text, title, CRN)
    }

    fun uploadUserProfileImage(selectedPhotoUri: Uri) =
        Firebase.uploadImageToFirebaseStorage(selectedPhotoUri)

    //fun getSavedPosts() = Firebase.getSavedPost()
    fun getSavedUserPosts() {
        //val pos = Firebase.getSavedUserPost()

    }

    fun getSubscribedPosts() = Firebase.getSubscribedPosts()
    //fun getEmail() = Firebase.getEmail()


    fun getpostKey(PKey: String) {

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

    fun deleteNewPost(postKey: String, crn: String, userID: String) {
        Firebase.deleteNewPost(postKey, crn, userID)
    }

    fun deleteNewCommentFromUserProfile(
        ClassKey: String,
        crn: String,
        comKey: String,
        userID: String
    ) {
        Firebase.deleteNewCommentFromUserProfile(ClassKey, crn, comKey, userID)
    }

    fun deleteNewCommentFromCommPosts(postKey: String, crn: String, classkey: String) {
        Firebase.deleteNewCommentFromCommPosts(postKey, crn, classkey)
    }

    fun editNewComment(
        userID: String,
        usercomkey: String,
        ctext: String,
        ntext: String,
        crn: String,
        postKey: String
    ) {
        Firebase.editComment(userID, usercomkey, ctext, ntext, crn, postKey)
    }


    fun editPost(
        crn: String, postKey: String, ctext: String, ctitle: String, ntext: String, ntitle: String,
        userID: String
    ) {
        Firebase.editPost(crn, postKey, ctext, ctitle, ntext, ntitle, userID)
    }

    fun SessionUser() = Firebase.CurrentUserL()
   /* fun blockComment(UserID: String, text: String, crn: String, postID: String) {
        Firebase.blockUserComment(UserID, text, crn, postID)
    }

    fun blockPost(UserID: String, crn: String, classkey: String) {
        Firebase.blockUserPost(UserID, crn, classkey)
    }
*/
    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String) {
        Firebase.reportUserPost(accusedID, complaintext, crn, classkey)

    }

    fun reportUserComment(
        accusedID: String,
        complaintext: String,
        crn: String,
        classkey: String,
        comKey: String
    ) {
        Firebase.reportUserComment(accusedID, complaintext, crn, classkey, comKey)

    }

    fun currentUser() = Firebase.CurrentUser()

    fun fetchSessionUserName() = SessionUser!!.username

    fun getClasses(): MutableLiveData<MutableList<CRN>> {
        Firebase.getClasses(object : FirebaseCallbackCRN {
            override fun onStart() {

            }

            override fun onFailure() {

            }

            override fun onSuccess(data: DataSnapshot) {
                for (datas in data.children) {
                    var classnames = CRN(datas.key!!)
                    val CC: Long = 2

                    //if(datas.childrenCount == CC)
                    /*classnames.let { x ->
                        x.name = datas.key!!
                        Log.d(datas, x.name)
                        //x.Subject = datas.child("subject").getValue(String::class.java)

                        //x.SUBLIST = SeparateList(datas.child("SubList"))
                    }*/

                    Log.d("getclasses", classnames.name)
                    classList.value?.add(classnames)


                }
            }

        })
        return classList
    }

    fun getClassPosts(className: String): PostLiveData {
        var newPost = Post()
        Firebase.getClassPosts(className, object : FirebaseCallbackPost {
            override fun onSuccess(data: DataSnapshot) {


                var savedPostsList: MutableList<Post> = mutableListOf()
                //val newPost = Post()
                //var postss = data.child("Posts")
                Log.d("Children", data.child("Posts").childrenCount.toString())
                var postdetails: Iterable<DataSnapshot> = data.child("Posts").children
                for (n in postdetails) {
                     newPost = Post()
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
                        it.Classkey = n.child("Classkey").getValue(String::class.java)
                        it.UserID = n.child("UserID").getValue(String::class.java)
                        it.author = n.child("author").getValue(String::class.java)

                        // comments might need to be gotten separatley to properly convert values




                    savedPostsList.add(newPost)
                 }
                }

                //repository.saveNewPost(newPost)
                //adapter.add(PostFrag(newPost.title, newPost.text))
                listClasses.value = savedPostsList


            }

            override fun onFailure() {
                Log.d("Faiure", "onfailurecalled")
            }

            override fun onStart() {

            }

        })


        return listClasses
    }


    fun getUserSub(): MutableLiveData<MutableList<String>>? {
        var SubList: MutableList<String> = mutableListOf()
        var SUBS: MutableLiveData<MutableList<String>> = MutableLiveData()
        Firebase.sendUserSUB(object : FirebaseCallbackString {
            override fun onFailure() {

            }

            override fun onStart() {

            }

            override fun onSuccess(d: DataSnapshot) {
                val size = d.hasChildren()
                Log.d("Size", size.toString())
                //var has :HashMap<String,String>? = hashMapOf()
                val Sublist = d.children
                for (x in Sublist) {
                    Log.d("usersub", x.getValue(String::class.java)!!)
                    SubList.add(x.getValue(String::class.java)!!)
                }
                SUBS.value = SubList
            }
        })
        return SUBS
    }


    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }

    fun getSubs(): MutableList<String> {
        var SubList: MutableList<String> = mutableListOf()
        Firebase.listenClasses(object : FirebaseCallbackCRN {
            override fun onFailure() {
                TODO("Not yet implemented")
            }

            override fun onStart() {
                TODO("Not yet implemented")
            }

            override fun onSuccess(data: DataSnapshot) {
                for (datas in data.children) {
                    var classnames = CRN(datas.key!!)
                    val CC: Long = 2

                    //if(datas.childrenCount == CC)
                    /*classnames.let { x ->
                        x.name = datas.key!!
                        Log.d(datas, x.name)
                        //x.Subject = datas.child("subject").getValue(String::class.java)

                        //x.SUBLIST = SeparateList(datas.child("SubList"))
                    }*/

                    Log.d("getclasses", classnames.name)
                    classList.value?.add(classnames)
                    Firebase.listenUserSub(object : FirebaseCallbackString {
                        override fun onFailure() {

                        }

                        override fun onStart() {

                        }

                        override fun onSuccess(d: DataSnapshot) {
                            val size = d.hasChildren()
                            Log.d("Size", size.toString())
                            //var has :HashMap<String,String>? = hashMapOf()
                            val Sublist = d.children
                            for (x in Sublist) {
                                Log.d("usersub", x.getValue(String::class.java)!!)
                                SubList.add(x.getValue(String::class.java)!!)
                            }
                            //UserSUB = SubList
                        }
                    })


                }
            }
        })


        for (n in classList.value!!.iterator()) {
            if (SubList.contains(n.name)) {
                n.Subscribed = true
            }
        }

        return SubList
    }
    fun getsublist() : MutableList<String>
    {
        var SubList : MutableList<String> = mutableListOf()
        Firebase.listenUserSub(object : FirebaseCallbackString {
            override fun onFailure() {

            }

            override fun onStart() {

            }

            override fun onSuccess(d: DataSnapshot) {
                val size = d.hasChildren()
                Log.d("Size", size.toString())
                //var has :HashMap<String,String>? = hashMapOf()
                val Sublist = d.children
                for (x in Sublist) {
                    Log.d("usersub", x.getValue(String::class.java)!!)
                    SubList.add(x.getValue(String::class.java)!!)
                }
                //UserSUB = SubList
            }
        })
        return SubList
    }

    fun remUsersub(crn: String) {
        Firebase.removeUserSub(crn)
        Firebase.removeClassSub(crn)
    }

    fun getclassnamesforusername() = Firebase.getclassnamesforusername()

    fun sendClassnameForUsername() = Firebase.sendClassnameForUsername()

    fun getUserProfilePosts(userID: String) : PostLiveData {
        var profilePostsList: MutableList<Post> = mutableListOf()
        Firebase.listenForUserProfilePosts(userID, object : FirebaseCallbackPost{
            override fun onFailure() {

            }

            override fun onStart() {

            }

            override fun onSuccess(p0: DataSnapshot) {
                if (p0.child("text").exists() == false) {
                    Log.d("post", "doesn't exist")
                    val emptyPost = Post("no Posts","" ,"","")
                    var profilePostL: MutableList<Post> = mutableListOf()
                    profilePostL.add(emptyPost)
                    profilePosts.value = profilePostL
                    //noPostsCheck = true
                }
                else {
                    //noPostsCheck = false
                    val newProfilePost = Post()
                    try {
                        newProfilePost.let {
                            it.text = p0.child("text").value.toString()
                            it.title = p0.child("title").value.toString()
                            it.key = p0.child("key").value.toString()
                            // class key is key for this post
                            it.Classkey = p0.child("Classkey").value.toString()
                            // user who posted id
                            it.UserID = p0.child("UserID").value.toString()
                            // need to change this later subject should be subject crn should be different
                            it.subject = p0.child("subject").value.toString()
                            it.Ptime=p0.child("Ptime").value.toString()
                            it.author = p0.child("author").value.toString()
                            // not setting author
                        }
                    } catch (e: Exception) {
                        Log.d("Data Error", "error converting to post")
                    }



                    if (newProfilePost != null) {
                        Log.d("profileposts", newProfilePost.title ?: " Accessing profile post title")
                        Log.d("profileposts", newProfilePost.text ?: " Accessing profile post text")
                        Log.d("profileposts", newProfilePost.key ?: " Accessing profile post title")
                        Log.d("profileposts", newProfilePost.Classkey ?: " Accessing profile post title")
                        Log.d("profileposts", newProfilePost.UserID ?: " Accessing profile post title")
                        Log.d("profileposts", newProfilePost.crn ?: " Accessing profile post title")

                        profilePostsList.add(newProfilePost)
                        Log.d("profileposts", newProfilePost.key ?: " Accessing profile post title")

                        newProfilePosts = newProfilePost

                    }
                    profilePosts.value = profilePostsList
                    //Log.d("TAG", "Comments loaded")
                }
                //return profile posts?
            }
        })
        return profilePosts
    }

    fun getUserProfileComments(userID: String) : CommentLive {
        var profileCommentList : MutableList<Comment> = mutableListOf()
        Firebase.listenForUserProfileComments(userID, object :FirebaseCallbackComment {
            override fun onFailure() {

            }

            override fun onStart() {

            }

            override fun onSuccess(p0: DataSnapshot) {
                if (p0.child("text").exists() == false) {
                    Log.d("comment", "doesn't exist")
                    val emptycomment = Comment("No Comments", "", "", "", "")
                    var profileCommentL: MutableList<Comment> = mutableListOf()
                    profileCommentL.add(emptycomment)
                    Comments.value = profileCommentL
                } else {
                    var newComment = Comment()
                    try {
                        newComment.let {
                            it.text = p0.child("text").value.toString()
                            it.Classkey = p0.child("Classkey").value.toString()
                            it.PosterID = p0.child("PosterID").value.toString()
                            it.Postkey = p0.child("Postkey").value.toString()
                            it.ProfileComKey = p0.child("ProfileComKey").value.toString()
                            it.Ptime = p0.child("Ptime").value.toString()
                            it.UserComkey = p0.child("UserComkey").value.toString()
                            it.author = p0.child("author").value.toString()
                            it.crn = p0.child("crn").value.toString()
                        }
                    } catch (e: Exception) {
                        Log.d("Data Error", "error converting to post")
                    }

                    if (newComment != null) {
                        Log.d("profile comments", newComment.text ?: " Accessing profile comment author")
                        profileCommentList.add(newComment)
                        //newProfilePosts = newProfilePost
                        newProfileComments = newComment
                    }


                    Comments.value = profileCommentList
                    //Log.d("TAG", "Comments loaded")
            }

        }
    })
        return Comments
    }

    fun fetchEmail(UserID: String) = Firebase.fetchEmail(UserID)

    fun saveNewUsername(username: String) = Firebase.saveNewUsername(username)

    fun saveUserbio(bio: String) = Firebase.saveUserbio(bio)

    fun fetchBio(UserID: String) = Firebase.fetchBio(UserID)

    fun fetchCurrentBio() = Firebase.fetchCurrentBio()

    fun noPostsChecker(UserID: String) = Firebase.noPostsChecker(UserID)

    fun noCommentsChecker(UserID: String) = Firebase.noCommentsChecker(UserID)
    fun blockUser(UserID: String) = Firebase.blockUser(UserID)


    companion object {
        @Volatile
        private var instance: PostRepository? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(firebasedata).also { instance = it }
            }
    }

    interface FirebaseCallback {
        fun onCallback(list: MutableList<User>?)
    }

    interface FirebaseMessagseCallback {
        fun onCallback(list: MutableList<ChatMessage>?)
    }

    interface FirebaseRecentMessagseCallback {
        fun onCallback(list: List<LatestMessage>)
    }

    interface FirebaseCallbackPost {

        fun onStart()
        fun onFailure()
        fun onSuccess(data: DataSnapshot)
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


}