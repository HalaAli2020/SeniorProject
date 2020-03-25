package com.example.seniorproject.data.Firebase
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.Utils.Callback
import com.example.seniorproject.data.models.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dagger.multibindings.ClassKey
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*
import java.util.logging.Handler
import javax.security.auth.Subject
import kotlin.collections.HashMap
import com.example.seniorproject.data.models.User
import com.google.firebase.auth.ActionCodeSettings
import java.lang.IllegalArgumentException
import kotlin.collections.ArrayList
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await


//import kotlin.reflect.jvm.internal.impl.load.java.lazy.ContextKt.child
//import javax.swing.UIManager.put



private const val TAG = "MyLogTag"
private const val GTAG = "editproftag"
private const val PTAG = "CLASSLIST"

@Singleton
@Suppress("unused")
class FirebaseData @Inject constructor() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    //var savedPosts: MutableLiveData<List<Post>> = MutableLiveData()
    var savedPosts: PostLiveData = PostLiveData()
    var userPosts: PostLiveData = PostLiveData()
    var Comments: CommentLive = CommentLive()
    var profilePosts: PostLiveData = PostLiveData()
   // var authlisteneruser : FirebaseAuth.AuthStateListener? = null


    private lateinit var postlistener: ValueEventListener
    private lateinit var userprofile: User
    var otherEmail: String? = null
    var otherBio: String? = null
    var newComments: Comment? = null
    var noPostsCheck: Boolean = false
    var noCommentsCheck: Boolean = false
    var saveImageurl: String? = null


    var newProfilePosts: Post? = null
    var newProfileComments: Comment? = null


    var classList: MutableList<CRN> = mutableListOf()
    var cList: MutableList<String> = mutableListOf()
    var classPostList: PostLiveData = PostLiveData()
    var MainPosts: MutableList<Post> = mutableListOf()
    var UserSUB: MutableList<String>? = null
    fun CurrentUser() = FirebaseAuth.getInstance().currentUser


    fun CurrentUserL() {
        val reference =
            FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!)

        //var changed: Boolean = false
        //var classList: MutableLiveData<List<String>> = MutableLiveData()
        //var classPostList: PostLiveData = PostLiveData()

        postlistener = object : ValueEventListener {
            //var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onDataChange(p0: DataSnapshot) {
                val username = p0.child("Username").getValue(String::class.java)
                val email = p0.child("email").getValue(String::class.java)
                var profileImageUrl = p0.child("profileImageUrl").getValue(Uri::class.java)
                userprofile = User(username, email, firebaseAuth.uid, profileImageUrl)

                Log.d("USERNAME", username!!)
                Log.d("USER", userprofile.username!!)

                /*if (userprofile != null) {
                    Log.d("ACCESSING", userprofile.username)
                    //savedPostsList.add(newPost)
                    //repository.saveNewPost(newPost)
                    //adapter.add(PostFrag(newPost.title, newPost.text))
                }*/


            }

            override fun onCancelled(p0: DatabaseError) {

            }


        }
        reference.addValueEventListener(postlistener)

    }
    // = firebaseAuth.currentUser


    fun logout() {
        firebaseAuth.signOut()

    }

    fun fetchEmail(UserID: String): String {

        val ref = FirebaseDatabase.getInstance().getReference("/users/$UserID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val email = p0.child("email").getValue(String::class.java)
                Log.d(TAG, "Current user fetched ${email}")
                otherEmail = email ?: ""
            }
        })

        return otherEmail ?: ""
    }

    fun fetchBio(UserID: String): String {

        //if it does no exist then bio = bio

        val ref = FirebaseDatabase.getInstance().getReference("/users/$UserID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val bio = p0.child("UserBio").getValue(String::class.java) ?: "no bio"
                Log.d(TAG, "Current user fetched ${bio}")
                otherBio = bio
            }
        })

        return otherBio ?: "no bio"
    }

    fun fetchCurrentBio(): String {
        val UserID = firebaseAuth.uid ?: "null"
        val ref = FirebaseDatabase.getInstance().getReference("/users/$UserID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val bio = p0.child("UserBio").getValue(String::class.java) ?: "no bio"
                Log.d(TAG, "Current user fetched ${bio}")
                otherBio = bio
            }
        })

        return otherBio ?: "no bio"
    }


    fun saveUserbio(bio: String) {

        val userID = firebaseAuth.uid ?: "null"
        FirebaseDatabase.getInstance().getReference("/users/$userID")
            .child("/UserBio").setValue(bio)
    }


    //saves new username to database and auth profile
    fun saveNewUsername(username: String) {
        val userID = firebaseAuth.uid ?: "null"

        FirebaseDatabase.getInstance().getReference("/users/$userID")
            .child("/Username").setValue(username)

        //var to get old username to compare
        //a nested for loop to check comments in every post

        listenForUserProfilePosts(userID)
        //do I need this?

        var x = 0

        while (x < profilePosts.value!!.size) {
            val post: Post = profilePosts.value!![x]
            val pkey: String = post.key.toString()
            if (pkey != "null") {
                Log.d("PTAG", "UNDER HERE")
                Log.d("PTAG", "the size is :  ${profilePosts.value!!.size}")
                Log.d("PTAG", "Pkey is:  ${pkey}")
                FirebaseDatabase.getInstance().getReference("/users/$userID/Posts/$pkey")
                    .child("/author").setValue(username)
            }
            //this should be the problem
            x++
        }

        val user = CurrentUser()
        val profileUpdates =
            UserProfileChangeRequest.Builder().setDisplayName(username).build()
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        TAG, "profile updated, emitter complete?:  ${CurrentUser()?.displayName} ."
                    )
                } else {
                    Log.d(TAG, "in else in fetch current user")
                }
            }


        getclassnamesforusername()
        sendClassnameForUsername()
        x = 0
        while (x < sendClassnameForUsername().size) {
            var classn = sendClassnameForUsername()[x]
            val ref = FirebaseDatabase.getInstance().getReference("/Subjects/${classn}/Posts")
            ref.orderByKey().addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    Log.d(PTAG, p0.getKey().toString())
                    var key = p0.getKey().toString()
                    val newref = FirebaseDatabase.getInstance()
                        .getReference("/Subjects/${classn}/Posts/${key}").child("/author")
                    if (userID == p0.child("UserID").value.toString()) {
                        newref.setValue(username)
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    Log.d(PTAG, p0.getKey().toString())
                    var key = p0.getKey().toString()
                    val newref = FirebaseDatabase.getInstance()
                        .getReference("/Subjects/${classn}/Posts/${key}").child("/author")
                    if (userID == p0.child("UserID").value.toString()) {
                        newref.setValue(username)
                        changeCommunityCommentUsername(username, classn, key)
                    }

                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    Log.d(TAG, "in else in fetch current user")
                }

            })
            x++
        }

        x = 0
        while (x < getUserProfilePosts(userID).value!!.size) {
            val post: Post = getUserProfilePosts(userID).value!![x]
            val pkey: String = post.key.toString()
            if (pkey != "null") {
                Log.d("PTAG", "Pkey is:  ${pkey}")
                FirebaseDatabase.getInstance().getReference("/users/$userID/Posts/$pkey")
                    .child("/author").setValue(username)
            }
            x++
        }
        x = 0
        while (x < getUserProfileComments(userID).value!!.size) {
            val comment = getUserProfileComments(userID).value!![x]
            val ckey: String = comment.ProfileComKey.toString()
            if (ckey != "null") {
                Log.d("PTAG", "key is:  ${ckey}")
                FirebaseDatabase.getInstance().getReference("/users/$userID/Comments/$ckey")
                    .child("/author").setValue(username)
            }
            x++
        }

    }

    fun changeCommunityCommentUsername(username: String, classname: String, pkey: String) {
        // you can get the username, and class via parameter
        //then you can just fill them in and make the query
        val userID = firebaseAuth.uid
        val ref = FirebaseDatabase.getInstance()
            .getReference("/Subjects/${classname}/Posts/${pkey}/Comments")
        ref.orderByKey().addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d(PTAG, p0.getKey().toString()) // the comment key
                var key = p0.getKey().toString()
                val newref = FirebaseDatabase.getInstance()
                    .getReference("/Subjects/${classname}/Posts/${pkey}/Comments/${key}")
                    .child("/author")
                if (userID == p0.child("PosterID").value.toString()) {
                    newref.setValue(username)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d(PTAG, p0.getKey().toString()) // the comment key
                var key = p0.getKey().toString()
                val newref = FirebaseDatabase.getInstance()
                    .getReference("/Subjects/${classname}/Posts/${pkey}/Comments/${key}")
                    .child("/author")
                if (userID == p0.child("PosterID").value.toString()) {
                    newref.setValue(username)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                Log.d(TAG, "in else in fetch current user")
            }

        })


    }


    fun getclassnamesforusername() {

        val reference = FirebaseDatabase.getInstance().reference.child("/Subjects")
        reference.orderByKey().addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d(PTAG, p0.getKey().toString())
                var classname = p0.getKey().toString()
                cList.add(classname)
                //val checker = cList.size
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d(PTAG, p0.getKey().toString())
                var classname = p0.getKey().toString()
                cList.add(classname)
                //val checker = cList.size
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        //if userid matched then change the author
    }

    fun sendClassnameForUsername(): MutableList<String> {
        getclassnamesforusername()
        //val check = cList.size
        return cList
    }


    fun fetchCurrentUserName() {
        var uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                val currentUser = p0.child("Username").getValue(String::class.java)
                Log.d(TAG, "Current user fetched ${currentUser}")
                var usernameForum = currentUser
                val user = CurrentUser()
                val profileUpdates =
                    UserProfileChangeRequest.Builder().setDisplayName(usernameForum).build()
                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(
                                TAG,
                                "profile updated, emitter complete?:  ${CurrentUser()?.displayName} ."
                            )
                        } else {
                            Log.d(TAG, "in else in fetch current user")
                        }
                    }
            }
        })
    }


    suspend fun RegisterUserEmail(firebaseAuth: FirebaseAuth, email:String ,password:String, username: String): AuthResult?{
        try {
            val data = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = FirebaseAuth.getInstance().uid
            uid?.let { it1 ->
                saveUserToFirebaseDatabase(
                    username,
                    email,
                    it1,
                    null
                )
            }
            val user = FirebaseAuth.getInstance().currentUser
            user?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        //add toast message that email was sent
                    }
                }
            return data
        }
        catch (e: Exception) {
            return null
        }
    }

    suspend fun resetUserPassword(firebaseAuth: FirebaseAuth, email: String){
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
        }
        catch (e: Exception) {
            Log.d("Failure", "reset password was not sent to user's email")
        }
    }

    //@Provides
    suspend fun LoginUserEmail(firebaseAuth: FirebaseAuth, email:String ,password:String): AuthResult?{
        try {
          /*  authlisteneruser = FirebaseAuth.AuthStateListener(object : FirebaseAuth.AuthStateListener, (FirebaseAuth) -> Unit {
                override fun invoke(p1: FirebaseAuth) {
                }

                override fun onAuthStateChanged(p0: FirebaseAuth) {
                    var user = p0.currentUser
                    if (user!!.isEmailVerified) {*/
                        val data = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                        return data
           /*         }
                }
            })*/

            }
        catch (e: Exception) {
                return null
            }

    }

    private fun saveUserToFirebaseDatabase(
        username: String,
        email: String,
        password: String,
        profileImageUrl: Uri?
    ) {
        Log.d("Debug", "entered firebase database function")
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        //val refP = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = User(username, email, password, profileImageUrl)
        val userin = user.toMap()

        ref.setValue(userin).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "saving to database worked")
            } else {
                Log.d(TAG, "not saved")
            }
        }.addOnFailureListener {
            Log.d(TAG, "Error ${it.message}")
        }
    }


    /*fun saveNewPost(postTitle: String, postText: String, postSubject: String) {
        val reference = FirebaseDatabase.getInstance().getReference("/posts").push()

        if (postTitle.isNotEmpty() && postText.isNotEmpty()) {
            val post = Post(postTitle, postText, postSubject)

            reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
        }
    }*/


    fun listenForUserProfilePosts(uid: String): PostLiveData {
        Log.d(TAG, "getUserProfilePosts listener called")

        //set list to empty here?

        val reference = FirebaseDatabase.getInstance().getReference("users/$uid").child("Posts")
         reference.addChildEventListener(object : ChildEventListener {
            var profilePostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if (p0.child("text").exists() == false) {
                    Log.d("post", "doesn't exist")
                    val emptyPost = Post("no Posts","" ,"","")
                    var profilePostL: MutableList<Post> = mutableListOf()
                    profilePostL.add(emptyPost)
                    profilePosts.value = profilePostL
                    noPostsCheck = true
                } else {
                    noPostsCheck = false
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
                            it.uri = p0.child("uri").value.toString()
                            // not setting author
                        }
                    } catch (e: Exception) {
                        Log.d("Data Error", "error converting to post")
                    }



                    if (newProfilePost.key != null) {
                        Log.d(TAG, newProfilePost.title ?: " Accessing profile post title")
                        Log.d(TAG, newProfilePost.text ?: " Accessing profile post text")
                        Log.d(TAG, newProfilePost.key ?: " Accessing profile post title")
                        Log.d(TAG, newProfilePost.Classkey ?: " Accessing profile post title")
                        Log.d(TAG, newProfilePost.UserID ?: " Accessing profile post title")
                        Log.d(TAG, newProfilePost.crn ?: " Accessing profile post title")

                        profilePostsList.add(newProfilePost)
                        Log.d(TAG, newProfilePost.key ?: " Accessing profile post title")

                        newProfilePosts = newProfilePost

                    }
                    profilePosts.value = profilePostsList
                    //Log.d("TAG", "Comments loaded")
                }
                //return profile posts?
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })
        Log.d("Post function return", "Post function return")

        //only do this for the current user?
        // for other users just check the value?
        val comref = FirebaseDatabase.getInstance().getReference("users/$uid")
         comref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child("Posts").exists() == false) {
                    Log.d("comment", "doesn't exist")
                    val emptyPost = Post("no Posts","" ,"","")
                    var profilePostL: MutableList<Post> = mutableListOf()
                    profilePostL.add(emptyPost)
                    profilePosts.value = profilePostL
                    noPostsCheck = true
                }
            }
        })

        return profilePosts
    }

    fun noPostsChecker(userID: String): Boolean {
        listenForUserProfilePosts(userID)
        return noPostsCheck
    }

    fun noCommentsChecker(userID: String): Boolean {
        listenForUserProfilePosts(userID)
        return noCommentsCheck
    }


    fun listenForUserProfileComments(uid: String): CommentLive {
        Log.d(TAG, "getUserProfile comments listener called")


        val reference = FirebaseDatabase.getInstance().getReference("users/$uid").child("Comments")
         reference.addChildEventListener(object : ChildEventListener {
            var profileCommentList: MutableList<Comment> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if (p0.child("text").exists() == false) {
                    Log.d("comment", "doesn't exist")
                    val emptycomment = Comment("No Comments", "", "", "", "")
                    var profileCommentL: MutableList<Comment> = mutableListOf()
                    profileCommentL.add(emptycomment)
                    Comments.value = profileCommentL
                    noCommentsCheck = true
                } else {
                    noCommentsCheck = false
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

                    if (newComment.ProfileComKey != null) {
                        Log.d(TAG, newComment.text)
                        profileCommentList.add(newComment)
                        //newProfilePosts = newProfilePost
                        newProfileComments = newComment
                    }


                    Comments.value = profileCommentList
                    //Log.d("TAG", "Comments loaded")

                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })
        Log.d("Post function return", "Post function return")

        val comref = FirebaseDatabase.getInstance().getReference("users/$uid")
        comref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child("Comments").exists() == false) {
                    Log.d("comment", "doesn't exist")
                    val emptycomment = Comment("no Comments" , "", "", "", "")
                    var profileCommentL: MutableList<Comment> = mutableListOf()
                    profileCommentL.add(emptycomment)
                    Comments.value = profileCommentL
                    noCommentsCheck = true
                } else {
                    noCommentsCheck = false
                }
            }
        })

        return Comments
    }


    fun getUserProfilePosts(userID: String): PostLiveData {
        Log.d(TAG, "getUserProfilePosts called")
        if (userID == "null") {
            var uid = FirebaseAuth.getInstance().uid ?: "error"
            listenForUserProfilePosts(uid)
        } else {
            var uid = userID
            listenForUserProfilePosts(uid)
        }
        return profilePosts
    }


    fun getUserProfileComments(userID: String): CommentLive {
        Log.d(TAG, "getUserProfile comments called")
        if (userID == "null") {
            var uid = FirebaseAuth.getInstance().uid ?: "error"
            listenForUserProfilePosts(uid)
        } else {
            var uid = userID
            listenForUserProfileComments(uid)
        }
        return Comments
    }


    fun getCommentsCO(Key: String): CommentLive {

        val uid = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("users/$uid/Post/$Key").child("Comments")

         reference.addChildEventListener(object : ChildEventListener {
            var savedCommentList: MutableList<Comment> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newComment = p0.getValue(Comment::class.java)


                if (newComment != null) {
                    Log.d("ACCESSING", newComment.text)
                    savedCommentList.add(newComment)

                    newComments = newComment
                    //repository.saveNewPost(newPost)
                    //adapter.add(PostFrag(newPost.title, newPost.text))

                }
                Comments.value = savedCommentList
                //Log.d("TAG", "Comments loaded")


            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })
        Log.d("Comment return", "comment return")
        return Comments


    }

    fun getComments(Key: String, subject: String): CommentLive {
        //val uid = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("Subjects/$subject/Posts/$Key/Comments")

        reference.addChildEventListener(object : ChildEventListener {
            var savedCommentList: MutableList<Comment> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newComment = p0.getValue(Comment::class.java)

                if (newComment != null) {
                    Log.d("ACCESSING", newComment.text)
                    if (savedCommentList.isNullOrEmpty()) {
                        savedCommentList.add(newComment)
                    }
                    savedCommentList.add(newComment)
                    //repository.saveNewPost(newPost)
                    //adapter.add(PostFrag(newPost.title, newPost.text))
                }
                Comments.value = savedCommentList

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })
        return Comments

    }


    fun saveNewCommentC(
        text: String, postID: String, ClassKey: String, UserID: String, crn: String
    ) {


        //val subject = Subject
        //val ClassID = Classkey
        val userID = firebaseAuth.uid
        val comment = Comment(text, "", userID, crn, postID)
        //FIX userprofile not init post.author = userprofile.username!!
        //val Class_key = FirebaseDatabase.getInstance().getReference(CRN).child("Posts").push().key
        //FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID")
        val User_key = FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID")
            .child("Comments").push().key


        //val Class_key = FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID").child("Comments").push().key
        // implement in viewmodel
        //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
        //val dataupdates = HashMap<String, Any>()
        val comementvalues = comment.toMap()
        //dataupdates["$subject/$ClassID/Post/$Class_key"] = postvalues
        //dataupdates["$userID/Posts/$User_key"] = postvalues
        FirebaseDatabase.getInstance().getReference("users/$userID/Post/$postID")
            .child("Comments/$User_key").setValue(comementvalues)


        /*Class_reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
         */

        //}
    }

    fun deleteNewPost(postKey: String, crn: String, userID: String) {
        val refSubPath = FirebaseDatabase.getInstance().getReference("/Subjects/$crn")

        val refUserPath = FirebaseDatabase.getInstance().getReference("/users/$userID")

        val queryuserref: Query =
            refUserPath.child("Posts").orderByChild("Classkey").equalTo(postKey)

        val refComment = FirebaseDatabase.getInstance().getReference("/users/$userID")

        val queryComment: Query =
            refComment.child("Comments").orderByChild("Postkey").equalTo(postKey)

        val query: Query = refSubPath.child("Posts").orderByChild("Classkey").equalTo(postKey)

        queryComment.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (comment in p0.children) {
                        // val refc= comment.getValue(Comment::class.java)
                        comment.ref.removeValue()
                        //refkey.child("/Subjects/$crn/Posts/$ClassKey/Comments/-M1MwaJ6UaXu2fdByTCU").removeValue()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        query.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (post in p0.children) {
                        // val refc= comment.getValue(Comment::class.java)
                        post.ref.removeValue()
                        //refkey.child("/Subjects/$crn/Posts/$ClassKey/Comments/-M1MwaJ6UaXu2fdByTCU").removeValue()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        //  val refkey = FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$ClassKey")
        queryuserref.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (post in p0.children) {
                        // val refc= comment.getValue(Comment::class.java)
                        post.ref.removeValue()
                        //refkey.child("/Subjects/$crn/Posts/$ClassKey/Comments/-M1MwaJ6UaXu2fdByTCU").removeValue()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


    }



    fun deleteNewCommentFromUserProfile(postKey: String, crn: String, comKey: String, userID: String)
    {
        val refkeyuser = FirebaseDatabase.getInstance().getReference("/users/$userID")

       // val refkeycominpost = FirebaseDatabase.getInstance().getReference("/users/$userID/Posts/$userPostKey")

        val query: Query = refkeyuser.child("Comments").orderByChild("ProfileComKey").equalTo(comKey)

       // val querycominpost: Query = refkeycominpost.child("Comments").orderByChild("UserPostkey").equalTo(userPostKey)


      /*  querycominpost.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(comment in p0.children){
                        // val refc= comment.getValue(Comment::class.java)
                        comment.ref.removeValue()
                        //refkey.child("/Subjects/$crn/Posts/$ClassKey/Comments/-M1MwaJ6UaXu2fdByTCU").removeValue()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })*/


        // val refkey = FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$ClassKey/Comments")
        query.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(comment in p0.children){
                        // val refc= comment.getValue(Comment::class.java)
                        comment.ref.removeValue()
                        //refkey.child("/Subjects/$crn/Posts/$ClassKey/Comments/-M1MwaJ6UaXu2fdByTCU").removeValue()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }

    fun deleteNewCommentFromCommPosts(postKey: String, crn: String, classkey: String)
    {
        val refkey2 = FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$postKey")


        //classkey for user path is same as true postkey for comment in subject path

        val queryuser: Query = refkey2.child("Comments").orderByChild("Classkey").equalTo(classkey)

        // val refkey = FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$ClassKey/Comments")
        queryuser.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(comment in p0.children){
                        // val refc= comment.getValue(Comment::class.java)
                        comment.ref.removeValue()
                        //refkey.child("/Subjects/$crn/Posts/$ClassKey/Comments/-M1MwaJ6UaXu2fdByTCU").removeValue()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }

    fun saveNewComment(text: String, postID: String, ClassKey: String, UserID: String, crn: String
    ) {


        //val subject = Subject
        //val ClassID = Classkey
        val userID = firebaseAuth.uid
        val author= firebaseAuth.currentUser?.displayName
        val comment = Comment(text, "", userID, crn, postID)
        comment.author= author
        Log.d("BigMoods", crn)
        val subpath = FirebaseDatabase.getInstance().getReference("/users/$userID")
        subpath.child("Subscriptions").orderByValue().addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(sub in p0.children){
                        // val refc= comment.getValue(Comment::class.java)
                        if(sub.getValue() == crn){
                            val Class_key = FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$ClassKey").child("Comments").push().key
                            val User_key = FirebaseDatabase.getInstance().getReference("/users/$UserID/Posts/$postID").child("Comments").push().key
                            //for profile
                            val Profile_key = FirebaseDatabase.getInstance().getReference("/users/$UserID").child("Comments").push().key

                            val Post_key: String? = FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$ClassKey").key


                            // implement in viewmodel
                            //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
                            comment.Classkey = Class_key
                            comment.UserComkey = User_key
                            comment.ProfileComKey = Profile_key
                            comment.Postkey = Post_key

                            val dataupdates = HashMap<String, Any>()
                            val comementvalues = comment.toMap()
                            dataupdates["Subjects/$crn/Posts/$ClassKey/Comments/$Class_key"] = comementvalues
                            //dataupdates["users/$UserID/Posts/$postID/Comments/$User_key"] = comementvalues
                            //for profile
                            //dataupdates["users/$UserID/Comments/$Profile_key"] = comementvalues
                            //FirebaseDatabase.getInstance().getReference("users/$userID/Post/$postID").child("Comments/$User_key").setValue(comementvalues)
                            FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)

                            //for profile
                            FirebaseDatabase.getInstance().getReference("/users/$userID")
                                .child("/Comments/$Profile_key").setValue(comementvalues)
                        }
                        else{
                            Log.d("Not subscribed","you cannot comment on a forum you are not subscribed in.")
                        }
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        /*Class_reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
         */

        //}
    }

    fun editComment(userID: String, usercomkey:String, ctext: String, ntext: String, crn: String, postKey: String){
        var comkey = FirebaseDatabase.getInstance().getReference("/users/$userID")

        val queryuser =comkey.child("Comments").orderByChild("ProfileComKey").equalTo(usercomkey)

        queryuser.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(comment in p0.children){
                        // val refc= comment.getValue(Comment::class.java)
                        comment.ref.child("text").setValue(ntext)
                        //refkey.child("/Subjects/$crn/Posts/$ClassKey/Comments/-M1MwaJ6UaXu2fdByTCU").removeValue()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        val refkey2 = FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$postKey")

        val queryuser2 =refkey2.child("Comments").orderByChild("ProfileComKey").equalTo(usercomkey)

        queryuser2.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(comment in p0.children){
                        // val refc= comment.getValue(Comment::class.java)
                        comment.ref.child("text").setValue(ntext)
                        //refkey.child("/Subjects/$crn/Posts/$ClassKey/Comments/-M1MwaJ6UaXu2fdByTCU").removeValue()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }


    fun editPost(crn: String, postKey: String, ctext: String, ctitle: String, ntext: String, ntitle: String,
                 userID: String){
        val refsubpath = FirebaseDatabase.getInstance().getReference("/Subjects/$crn")

        var refuserpath = FirebaseDatabase.getInstance().getReference("/users/$userID")

        val querypost: Query = refsubpath.child("Posts").orderByChild("Classkey").equalTo(postKey)

        val queryupost: Query = refuserpath.child("Posts").orderByChild("Classkey").equalTo(postKey)

        querypost.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(post in p0.children){
                        // val refc= comment.getValue(Comment::class.java)
                        post.ref.child("text").setValue(ntext)
                        post.ref.child("title").setValue(ntitle)
                        //refkey.child("/Subjects/$crn/Posts/$ClassKey/Comments/-M1MwaJ6UaXu2fdByTCU").removeValue()
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        queryupost.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(post in p0.children){
                        post.ref.child("text").setValue(ntext)
                        post.ref.child("title").setValue(ntitle)
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun blockUser(UserID: String) {

        val userID = firebaseAuth.uid

        val ref = FirebaseDatabase.getInstance().getReference("users/$userID")
        //ref.child("BlockedUsers").push().setValue(UserID)
         ref.child("BlockedUsers").orderByValue().
            addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists() == false){
                        ref.child("BlockedUsers").push().setValue(UserID)
                    }
                    if(p0.exists()){
                        for(block in p0.children){
                            if(block.getValue() != UserID){
                                ref.child("BlockedUsers").push().setValue(UserID)
                            }
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })

    }

    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String){

        val accuserID = firebaseAuth.currentUser?.email

        var report = Reports(accuserID!!, accusedID, complaintext, crn, classkey)

        val post = FirebaseDatabase.getInstance().getReference("Subjects/$crn/Posts/$classkey").key
        val user = FirebaseDatabase.getInstance().getReference("Subjects/$crn/Posts/$accusedID").key
        val text = FirebaseDatabase.getInstance().getReference("Subjects/$crn/Posts/$complaintext").key


        report.classkey= post!!
        report.complaintext= text!!
        report.accusedID = user!!

        //val querypost: Query = getreport.child("Posts").orderByChild("Classkey").equalTo(classkey)



        val dataupdates = HashMap<String, Any>()
        val reportvalues = report.toMap()
        dataupdates["Reports/$classkey"] = reportvalues
        FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)
    }

    fun reportUserComment(accusedID: String, complaintext: String, crn: String, classkey: String, comkey: String){

        val accuserID = firebaseAuth.currentUser?.email

        var report = Reports(accuserID!!, accusedID, complaintext, crn, classkey)

        val post = FirebaseDatabase.getInstance().getReference("Subjects/$crn/Posts/$classkey/Comments/$comkey").key
        val user = FirebaseDatabase.getInstance().getReference("Subjects/$crn/Posts/$classkey/Comments/$accusedID").key
        val text = FirebaseDatabase.getInstance().getReference("Subjects/$crn/Posts/$classkey/Comments/$complaintext").key


        report.classkey= post!!
        report.complaintext= text!!
        report.accusedID = user!!


        //val querypost: Query = getreport.child("Posts").orderByChild("Classkey").equalTo(classkey)



        val dataupdates = HashMap<String, Any>()
        val reportvalues = report.toMap()
        dataupdates["Reports/$classkey"] = reportvalues
        FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)
    }



    fun readPostValues(crn: String, postkey: String, callBack : Callback){
        FirebaseDatabase.getInstance().getReference("Subjects/$crn/Posts/$postkey").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }


            override fun onDataChange(p0: DataSnapshot) {
                var title= p0.child("title").getValue(String::class.java)
                var text= p0.child("text").getValue(String::class.java)
                var key = p0.child("key").getValue(String::class.java)
                var ptime= p0.child("Ptime").getValue(String::class.java)
                var classkey= p0.child("Classkey").getValue(String::class.java)
                var user= p0.child("UserID").getValue(String::class.java)
                var author = p0.child("author").getValue(String::class.java)
                var uri = p0.child("uri").getValue(String::class.java)
                var list: ArrayList<String> = arrayListOf()
                list.add(0, title!!)
                list.add(1, text!!)
                list.add(2, key!!)
                list.add(3, ptime!!)
                list.add(4, classkey!!)
                list.add(5, user!!)
                list.add(6, author!!)
                list.add(7,uri!!)
                callBack.onCallback(list)
            }
            /*override fun onDataChange(p0: DataSnapshot){
                if(p0.exists()){
                    var ptext = p0.getValue().toString()
                    callBack.onCallback(ptext)
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }*/
        })
    }

    // CRN is a placeholder for a class object
    fun saveNewImgPosttoUser(title: String, text: String, Subject: String, CRN: String, uri: Uri, imagePost: Boolean) {

        if (uri == null) {
            return
        }
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(uri).addOnSuccessListener {
            saveImageurl = "test to see HELLO"
            ref.downloadUrl.addOnSuccessListener {
                val urii = it
                saveImageurl = urii.toString()
                var post = Post(title, text, CRN,"")
                //val subject = Subject
                val userID = firebaseAuth.uid
                val author = firebaseAuth.currentUser?.displayName
                post.UserID = userID
                post.author = author
                post.imagePost = imagePost
                val User_key =
                    FirebaseDatabase.getInstance().getReference("/users/$userID").child("Posts")
                        .push().key
                val Class_key =
                    FirebaseDatabase.getInstance().getReference("/Subjects/$CRN").child("Posts")
                        .push().key
                post.key = User_key
                post.Classkey = Class_key
                val dataupdates = HashMap<String, Any>()
                post.uri = saveImageurl
                val postvalues = post.toMap()
                dataupdates["/Subjects/$CRN/Posts/$Class_key"] = postvalues
                dataupdates["/users/$userID/Posts/$User_key"] = postvalues
                FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)
            }
        }

    }




    fun saveNewPosttoUser(text: String, title:String, CRN: String) {
        //val subject = Subject
        // val ClassID = Classkey
        val userID = firebaseAuth.uid
        val author= firebaseAuth.currentUser?.displayName
        val post = Post(title, text, CRN,"")
        post.UserID = userID
        post.author=author
        //FIX userprofile not init post.author = userprofile.username!!
        //val Class_key = FirebaseDatabase.getInstance().getReference(CRN).child("Posts").push().key
        //FirebaseDatabase.getInstance().getReference("/users/$userID")
        val subpath = FirebaseDatabase.getInstance().getReference("/users/$userID")
         subpath.child("Subscriptions").orderByValue().addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(sub in p0.children){
                        // val refc= comment.getValue(Comment::class.java)
                        if(sub.getValue() == CRN){
                            val User_key = FirebaseDatabase.getInstance().getReference("/users/$userID").child("Posts").push().key
                            val Class_key = FirebaseDatabase.getInstance().getReference("/Subjects/$CRN").child("Posts").push().key
                            post.key = User_key
                            post.Classkey = Class_key
                            //post.author= auth_key
                            // implement in viewmodel
                            //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
                            val dataupdates = HashMap<String, Any>()
                            val postvalues = post.toMap()
                            dataupdates["/Subjects/$CRN/Posts/$Class_key"] = postvalues
                            //dataupdates["/Subjects/$CRN/Posts/$postID/$author"] = postvalues
                            dataupdates["/users/$userID/Posts/$User_key"] = postvalues

                            // FirebaseDatabase.getInstance().getReference("users/$userID").child("Post/$User_key").setValue(postvalues)
                            FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)
                        }
                        else{
                            Log.d("Not subscribed","you cannot post to a forum you are not subscribed in.")
                        }
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun getUserSub() {

        val uid = FirebaseAuth.getInstance().uid
//        Log.d("uid", uid!!)
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid/Subscriptions").orderByValue()
        reference.addValueEventListener(object : ValueEventListener {
            val SubList: MutableList<String> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val size = p0.hasChildren()
                Log.d("Size", size.toString())
                //var has :HashMap<String,String>? = hashMapOf()
                val Sublist = p0.children
                for (x in Sublist) {
                    Log.d("usersub", x.getValue(String::class.java)!!)
                    SubList.add(x.getValue(String::class.java)!!)
                }
                UserSUB = SubList
            }
        })
    }
    /*private fun listenForSubscribedPosts2(sub: MutableList<String>){
        var count = 0
       for (n in sub.iterator())
       {
           var start = count
           var ref = FirebaseDatabase.getInstance().getReference("Subject/$n/Posts").limitToFirst(2)

           ref.addListenerForSingleValueEvent(object : ValueEventListener {
               override fun onDataChange(p0: DataSnapshot) {

               }

               override fun onCancelled(p0: DatabaseError) {
                   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
               }
           })
          while (count != (start + 2))
          {
              Log.d("waiting", "")
          }
       }

    }*/

    fun sendUserSUB(): MutableList<String>? {
        getUserSub()
        var sub = UserSUB
        return sub
    }

    //, Subject: String
    fun addUserSUB(crn: String) {
        if (UserSUB == null) {
            getUserSub()
        }
        //var SubAdd = HashMap<String, String>()
        //SubAdd[crn] = Subject
        val uid = FirebaseAuth.getInstance().uid
        var ref = FirebaseDatabase.getInstance().getReference("users/$uid/Subscriptions")
        ref.push().setValue(crn)
        val rref = FirebaseDatabase.getInstance().getReference("Subjects/$crn/SubList")
        rref.push().setValue(uid)

    }

    fun removeUserSub(crn: String) {
        val uid = FirebaseAuth.getInstance().uid
        Log.d("uid", uid!!)
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid/Subscriptions")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            val SubList: MutableList<String> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val size = p0.hasChildren()
                Log.d("Size", size.toString())
                //var has :HashMap<String,String>? = hashMapOf()
                val Sublist = p0.children
                for (x in Sublist) {
                    if (crn == x.getValue(String::class.java)) {
                        Log.d("Remove", "remove hit")
                        x.key
                        reference.child(x.key!!).removeValue()
                    } else {
                        Log.d("usersub", x.getValue(String::class.java)!!)
                        SubList.add(x.getValue(String::class.java)!!)
                    }
                }

                UserSUB = SubList
            }
        })
    }

    fun removeClassSub(crn: String) {
        val uid = FirebaseAuth.getInstance().uid
        Log.d("uid", uid!!)
        val reference = FirebaseDatabase.getInstance().getReference("Subjects/$crn/SubList")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            val SubList: MutableList<String> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val size = p0.hasChildren()
                Log.d("Size", size.toString())
                //var has :HashMap<String,String>? = hashMapOf()
                val Sublist = p0.children
                for (x in Sublist) {
                    if (uid == x.getValue(String::class.java)) {
                        Log.d("Remove", "remove hit")

                        reference.child(x.key!!).removeValue()
                    } else {
                        Log.d("usersub", x.getValue(String::class.java)!!)
                        SubList.add(x.getValue(String::class.java)!!)
                    }
                }

                UserSUB = SubList
            }
        })

    }


    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri) {

        Log.d(TAG, "photo url is null")

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                Log.d("Pic", "Successfully uploaded picture: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    it.toString()

                    Log.d("Pic", "File location: $it")


                    var user = CurrentUser()

                    val profileUpdates = UserProfileChangeRequest.Builder().setPhotoUri(it).build()
                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(
                                    TAG,
                                    "profile image updated, emitter complete?:  ${CurrentUser()?.photoUrl} ."
                                )
                            } else {
                                Log.d(TAG, "profile image failed to update")
                            }
                        }

                    //uploadImageToFirebaseDatabase(it)


                }
            }

        val uid = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid")
        reference.child("/profileImageUrl").setValue(FirebaseAuth.getInstance().currentUser?.photoUrl.toString())


        /*reference.addChildEventListener(object : ChildEventListener {
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

            }

        })*/



    }

    //this function is commented out because it fails to retrieve image in Firebase Storage and
    //store it under user

    /* fun uploadImageToFirebaseDatabase(profileImageUrl: Uri){
         val uid = FirebaseAuth.getInstance().uid ?: ""
         val username = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
         val email = FirebaseAuth.getInstance().currentUser?.email ?: ""
         val useref = FirebaseDatabase.getInstance().getReference("/users/$uid")

         //val user = User(username, email, uid, profileImageUrl)
         //var user = CurrentUser()
         val user = User(username, email, uid, profileImageUrl)
         val userin = user.toMap()

         useref.setValue(userin).addOnSuccessListener {


             Log.d(TAG, "profile image is added to Firebase Database schema")

         }


     }*/

    fun getClassPosts(className: String): PostLiveData {
        val reference = FirebaseDatabase.getInstance().getReference("Subjects/$className/Posts")

        reference.addChildEventListener(object : ChildEventListener {
            var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                //val post = p0.getValue()
                //val newPost = Post()
                //var postdetails: Iterable<DataSnapshot> = p0.children
                //for (n in postdetails) {
                var newPost = Post()
                newPost.let {

                    //Log.d("ACCESSING", newPost?.text)
                    it.title = p0.child("title").getValue(String::class.java)
                    it.text = p0.child("text").getValue(String::class.java)
                    //newPost.author = pos.child("author").getValue(String::class.java)
                    it.subject = className
                    Log.d("CRN", p0.key!!)
                    //newPost.subject = post.child("subject").getValue(String ::class.java)
                    //newPost.ptime = pos.child("Timestamp").getValue(Long::class.java)
                    it.key = p0.child("key").getValue(String::class.java)
                    it.Ptime = p0.child("Ptime").getValue(String::class.java)
                    it.Classkey = p0.child("Classkey").getValue(String::class.java)
                    it.UserID = p0.child("UserID").getValue(String::class.java)
                    it.author= p0.child("author").getValue(String::class.java)
                    it.uri = p0.child("uri").value.toString()

                    // comments might need to be gotten separatley to properly convert values



                    savedPostsList.add(newPost)

                }

                //repository.saveNewPost(newPost)
                //adapter.add(PostFrag(newPost.title, newPost.text))


                classPostList.value = savedPostsList
            }




            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })

        return classPostList
    }

    fun getClasses() {
        val reference = FirebaseDatabase.getInstance().reference.child("/Subjects")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            var classes: MutableList<String> = mutableListOf()
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val Clist: MutableList<CRN> = mutableListOf()

                for (datas in dataSnapshot.children) {
                    val classnames = CRN()
                    val CC: Long = 2
                    //if(datas.childrenCount == CC)
                    classnames?.let { x ->
                        x.name = datas.key!!
                        //x.Subject = datas.child("subject").getValue(String::class.java)
                        x.SUBLIST = SeparateList(datas.child("SubList"))
                    }
                    classList.add(classnames)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })


    }


    fun SeparateList(p0: DataSnapshot): List<String>? {
        var SubList: MutableList<String> = mutableListOf()
        for (datas in p0.children) {
            if (datas != null)
                SubList.add(datas.getValue(String::class.java)!!)
        }
        return SubList
    }

    fun sendClist(): MutableList<CRN> {
        if (classList.isEmpty())
            getClasses()

        return classList
    }


    /*private fun listenForSubscribedPosts(){
        val reference = FirebaseDatabase.getInstance().getReference("posts").limitToFirst(10)

        reference.addChildEventListener(object : ChildEventListener {
            var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if(UserSUB!!.contains(p0.child("subject").getValue().toString())) {
                    val newPost = Post()
                    try {
                        newPost.let {
                            it.text = p0.child("text").getValue().toString()
                            it.title = p0.child("title").getValue().toString()
                            it.key = p0.child("Key").getValue().toString()
                        }
                    } catch (e: Exception) {
                        Log.d("Data Error", "error converting to post")
                    }


                    if (newPost != null) {
                        Log.d("ACCESSING", newPost?.text)
                        savedPostsList.add(newPost)
                    }
                    savedPosts.value = savedPostsList
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })
    }*/

    private fun listenForSubscribedPosts2(sub: MutableList<String>?){
        val reference = FirebaseDatabase.getInstance().getReference("Subjects")
        var sub: MutableList<String>? = sendUserSUB()
        reference.addChildEventListener(object : ChildEventListener {
            var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if(sub.isNullOrEmpty()){
                    savedPosts.value = savedPostsList
                }
                else {
                    Log.d("FIRST1-:", p0.key)
                    if (sub!!.contains(p0.key)) {
                        for (p2 in p0.getChildren()) {
                            Log.d("FIRST2---:", p2.key)
                            if (p2.key == "Posts") {
                                var counter = 0
                                for (p3 in p2.getChildren().reversed()) {
                                    Log.d("FIRST3-------:", p3.key)
                                    val newPost = Post()
                                    try {
                                        newPost.let {
                                            it.text = p3.child("text").value.toString()
                                            it.title = p3.child("title").value.toString()
                                            it.key = p3.child("key").value.toString()
                                            // class key is key for this post
                                            it.Classkey = p3.child("Classkey").value.toString()
                                            // user who posted id
                                            it.UserID = p3.child("UserID").value.toString()
                                            // need to change this later subject should be subject crn should be different
                                            it.subject = p3.child("subject").value.toString()
                                            it.Ptime=p3.child("Ptime").value.toString()
                                            it.author = p3.child("author").value.toString()
                                            it.uri = p0.child("uri").value.toString()
                                        }
                                    } catch (e: Exception) {
                                        Log.d("Data Error", "error converting to post")
                                    }

                                    if (newPost.title != null) {
                                        //Log.d("ACCESSING", newPost?.text)
                                        savedPostsList.add(newPost)
                                    }

                                    savedPosts.value = savedPostsList
                                    counter++
                                    if (counter == 2) break
                                }
                            }
                        }
                    }
                }
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }



    fun getSubscribedPosts(): PostLiveData {
        var sub: MutableList<String>? = sendUserSUB()
        if(sub!=null) {
            listenForSubscribedPosts2(sub)
        }
        return savedPosts
    }


    private fun listenforUserPosts() {
        // val userID = FirebaseAuth.getInstance().currentUser
        val reference =
            FirebaseDatabase.getInstance().getReference("users/1XN3H62rMJhe6CiCbN4os2TNp5H2")
                .child("Post")


        reference.addChildEventListener(object : ChildEventListener {
            var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newPost: Post = Post()
                try {
                    newPost.let {
                        it.text = p0.child("text").value.toString()
                        it.title = p0.child("title").value.toString()
                        it.key = p0.child("Key").value.toString()
                        it.key = p0.child("crn").value.toString()
                        it.uri = p0.child("uri").value.toString()
                        it.key = p0.child("subject").value.toString()
                        //added crn so it would show up in home frag
                    }
                } catch (e: Exception) {
                    Log.d("Data Error", "error converting to post")
                }


                if (newPost.title != null) {
                    //Log.d("ACCESSING", newPost?.text)
                    savedPostsList.add(newPost)

                    //repository.saveNewPost(newPost)
                    //adapter.add(PostFrag(newPost.title, newPost.text))
                }
                savedPosts.value = savedPostsList

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }



        })

    }


    /*private fun getclass(subject :String ,crn : String)
    {
        val reference = FirebaseDatabase.getInstance().getReference("Subjects/$subject/$crn/Posts")


        reference.addChildEventListener(object : ChildEventListener {
            var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // decide if you want to put comments into post now or load them later if now how to do it?

                var newPost = Post()
                //= p0.getValue(Post::class.java)

                var postdetails :Iterable<DataSnapshot> = p0.children
                for(post in postdetails)
                {
                    newPost.title = post.child("title").getValue(String ::class.java)
                    newPost.text = post.child("text").getValue(String ::class.java)
                    newPost.author = post.child("author").getValue(String ::class.java)
                    newPost.crn = post.child("crn").getValue(String ::class.java)
                    //newPost.subject = post.child("subject").getValue(String ::class.java)
                    newPost.ptime = post.child("Timestamp").getValue(Long ::class.java)
                    // comments might need to be gotten separatley to properly convert values
                }


                if (newPost != null) {
                    Log.d("ACCESSING", newPost?.text)
                    if(savedPostsList.size < 3)
                    {
                        savedPostsList.add(newPost)
                    }



                    //repository.saveNewPost(newPost)
                    //adapter.add(PostFrag(newPost.title, newPost.text))
                }
                savedPosts.value = savedPostsList

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }



        })
    }*/


    fun getUsers(): MutableLiveData<List<User>>?{

        var UserList: MutableLiveData<List<User>>? = MutableLiveData()
        setUsers(object: FirebaseCallback{
            override fun onCallback(list: MutableList<User>?) {
                UserList?.value=list
            }
        })

        return UserList
    }

    private fun setUsers(firebaseCallback: FirebaseCallback) {
        val ref = FirebaseDatabase.getInstance().getReference("users")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            var UserList: MutableList<User> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(p1 in p0.children) {
                    var user = User()
                    user.username = p1.child("Username").getValue(String::class.java)
                    user.uid = p1.child("uid").getValue(String::class.java)
                    val imageProfURL = p1.child("profileImageUrl").getValue(String::class.java)
                    if(!imageProfURL.isNullOrEmpty()) {
                        user.profileImageUrl = Uri.parse(imageProfURL)
                    }

                    if (user.uid != null) {
                        UserList.add(user)
                    }
                }

                firebaseCallback.onCallback(UserList)
            }

        })
    }


    fun getMessages(toId: String?): MutableLiveData<List<ChatMessage>>?{
        var messages: MutableLiveData<List<ChatMessage>>? = MutableLiveData()
        listenForMessages(object: FirebaseMessagseCallback{
            override fun onCallback(list: MutableList<ChatMessage>?) {
                messages?.value=list
            }
        }, toId)

        return messages
    }

    fun listenForMessages(firebaseCallback: FirebaseMessagseCallback, toId: String?){
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener{
            var chatList: MutableList<ChatMessage> = mutableListOf()
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                //for(p1 in p0.children) {

                val chatMessage = p0.getValue(ChatMessage::class.java)
                //chatMessage.uid = p1.child("uid").getValue(String::class.java)

                if (chatMessage != null) {
                    chatList.add(chatMessage)
                }
                //}

                firebaseCallback.onCallback(chatList)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }


    fun sendMessage(message: String, toID: String, username: String){
        //val message = editText_chatLog.text.toString()

        val fromID = FirebaseAuth.getInstance().uid

        //if(fromID == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toID/$fromID").push()

        val chatMessage = ChatMessage(
            reference.key!!,
            message,
            fromID!!,
            toID,
            System.currentTimeMillis() / 1000
        )

        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d("ChatLog", "Saved our chat message")
            //editText_chatLog.text.clear()
            //recycler_view_chatLog.scrollToPosition(adapter.itemCount-1)
        }

        toReference.setValue(chatMessage)

        val latestChatMessage = LatestMessage(
            reference.key!!,
            message,
            fromID,
            toID,
            username,
            1-(System.currentTimeMillis() / 1000)
        )

        val latestChatMessage2 = LatestMessage(
            reference.key!!,
            message,
            fromID,
            toID,
            FirebaseAuth.getInstance().currentUser?.displayName,
            1-(System.currentTimeMillis() / 1000)
        )

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromID/$toID")
        latestMessageRef.setValue(latestChatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toID/$fromID")
        latestMessageToRef.setValue(latestChatMessage2)
    }


    fun getRecentMessages(): MutableLiveData<List<LatestMessage>>{
        val latestMessagesMap = MutableLiveData<List<LatestMessage>>()
        listenForLatestMessage(object: FirebaseRecentMessagseCallback{
            override fun onCallback(list: List<LatestMessage>) {
                latestMessagesMap.value=list

            }
        })
        return latestMessagesMap
    }

    private fun listenForLatestMessage(firebaseCallback: FirebaseRecentMessagseCallback){
        val fromId = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId").orderByChild("timestamp")
        ref.addChildEventListener(object: ChildEventListener{
            var latestMessages: MutableList<LatestMessage> = mutableListOf()

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(LatestMessage::class.java) ?: return
                latestMessages.add(chatMessage)

                firebaseCallback.onCallback(latestMessages)
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(LatestMessage::class.java) ?: return

                latestMessages.forEachIndexed{index, element->
                    if(element.username==chatMessage.username){
                        latestMessages[index] = chatMessage
                    }
                }

                firebaseCallback.onCallback(latestMessages)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
        })
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



    companion object {
        @Volatile
        private var instance: FirebaseData? = null

        //rights to this properties are immediate visible ot other threats
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FirebaseData().also { instance = it }
            }
        //if the instance is not n
    }

}














