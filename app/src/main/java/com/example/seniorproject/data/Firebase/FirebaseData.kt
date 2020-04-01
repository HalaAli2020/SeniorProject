package com.example.seniorproject.data.Firebase
import android.net.Uri
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.Utils.Callback
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.data.models.*
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.PostRepository
import kotlin.collections.ArrayList
import com.example.seniorproject.viewModels.SearchViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await






private const val TAG = "MyLogTag"
//private const val GTAG = "editproftag"
private const val PTAG = "CLASSLIST"

@Singleton
@Suppress("unused")
class FirebaseData @Inject constructor() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }



    var savedPosts: PostLiveData = PostLiveData()
    var userPosts: PostLiveData = PostLiveData()
    var Comments: CommentLive = CommentLive()
    var profilePosts: PostLiveData = PostLiveData()


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
    var UserSUB : MutableLiveData<MutableList<String>> = MutableLiveData()
    fun CurrentUser() = FirebaseAuth.getInstance().currentUser


    fun CurrentUserL() {
        val reference =
            FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!)



        postlistener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val username = p0.child("Username").getValue(String::class.java)
                val email = p0.child("email").getValue(String::class.java)
                var profileImageUrl = p0.child("profileImageUrl").getValue(String::class.java)
                userprofile = User(username, email, firebaseAuth.uid, profileImageUrl)

                Log.d("USERNAME", username!!)
                Log.d("USER", userprofile.username!!)

            }

            override fun onCancelled(p0: DatabaseError) {

            }


        }
        reference.addValueEventListener(postlistener)

    }



    fun logout() {
        firebaseAuth.signOut()

    }

    fun fetchEmail(UserID: String, callbackEmail: PostRepository.FirebaseCallbackItem ) {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$UserID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                callbackEmail.onMessage(p0)
            }
        })
    }

    fun fetchBio(UserID: String, callbackbio: PostRepository.FirebaseCallbackItem) {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$UserID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                callbackbio.onMessage(p0)
            }
        })
    }

    fun fetchCurrentBio(): String {
        val userID = firebaseAuth.uid ?: "null"
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userID")
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



    fun saveNewUsername(username: String) {
        val userID = firebaseAuth.uid ?: "null"

        FirebaseDatabase.getInstance().getReference("/users/$userID")
            .child("/Username").setValue(username)

        listenForUserProfilePosts(userID, object : PostRepository.FirebaseCallbackPost{
            override fun onFailure() {

            }

            override fun onStart() {

            }

            override fun onSuccess(data: DataSnapshot) {

            }
        })

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

        changeuserpostname(username)
        changeusercommetname(username)
    }
    fun changeuserpostname(name : String)
    {
        var uid = FirebaseAuth.getInstance().uid
        var ref = FirebaseDatabase.getInstance().getReference("users/$uid/Posts")
        var plist : MutableList<Post> = mutableListOf()
        var p = Post()
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(x in p0.children)
                {
                   p.key = x.child("key").value.toString()
                    p.Classkey = x.child("Classkey").value.toString()
                    p.subject = x.child("subject").value.toString()
                    plist.add(p)

                }
            }
        })
        changeclassPname(plist, name)
        changePname(plist, name)
    }
    fun changeclassPname(l : MutableList<Post>, name: String)
    {
        for (x in l.iterator())
        {
            var ref = FirebaseDatabase.getInstance().getReference("Subjects/${x.subject}/${x.Classkey}")
            ref.child("author").setValue(name)
        }
    }
    fun changePname(l : MutableList<Post>, name: String)
    {
        var uid = FirebaseAuth.getInstance().uid
        for (x in l.iterator())
        {
            var ref = FirebaseDatabase.getInstance().getReference("users/$uid/${x.key}")
            ref.child("author").setValue(name)
        }
    }
    fun changeusercommetname(name : String)
    {
        var uid = FirebaseAuth.getInstance().uid
        var ref = FirebaseDatabase.getInstance().getReference("users/$uid/Comments")
        var plist : MutableList<Comment> = mutableListOf()
        var p = Comment()
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(x in p0.children)
                {
                    p.ProfileComKey = x.child("ProfileComKey").value.toString()
                    p.UserComkey = x.child("UserComkey").value.toString()
                    p.crn = x.child("crn").value.toString()
                    plist.add(p)

                }
            }
        })
        changeclassCPname(plist, name)
        changeCname(plist, name)

    }
    fun changeclassCPname(l : MutableList<Comment>, name: String)
    {
        for (x in l.iterator())
        {
            var ref = FirebaseDatabase.getInstance().getReference("Subjects/${x.crn}/${x.ProfileComKey}")
            ref.child("author").setValue(name)
        }
    }
    fun changeCname(l : MutableList<Comment>, name: String)
    {
        var uid = FirebaseAuth.getInstance().uid
        for (x in l.iterator())
        {
            var ref = FirebaseDatabase.getInstance().getReference("users/$uid/${x.UserComkey}")
            ref.child("author").setValue(name)
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


    suspend fun RegisterUserEmail(firebaseAuth: FirebaseAuth, email:String ,password:String, username: String, callback: EmailCallback): AuthResult?{
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
                        callback.getEmail("Account created!")
                        //add toast message that email was sent
                    }
                }
            return data
        }
        catch (e: Exception) {
            val code = e.message
            Log.d("soupregister", "error is $code")
            callback.getEmail(code!!)
            return null
        }
    }

    suspend fun resetUserPassword(firebaseAuth: FirebaseAuth, email: String, callback: EmailCallback){
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            callback.getEmail("Email sent!")
        }
        catch (e: Exception) {
            val code = e.message
            Log.d("soupreset", "error is $code")
            callback.getEmail(code!!)
        }
    }

    fun readPhotoValue(useridm: String, callback: EmailCallback) {
        val userref = FirebaseDatabase.getInstance().getReference("users/$useridm")
        userref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val photo = p0.child("profileImageUrl").getValue(String::class.java)
                    callback.getEmail(photo!!)
                    }
                }
        })
    }

    //@Provides
    suspend fun LoginUserEmail(firebaseAuth: FirebaseAuth, email:String ,password:String, callback: EmailCallback): AuthResult?{
        try {
            val data = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            callback.getEmail("Successful login!")
            return data
            }
        catch (e: FirebaseAuthException) {
                //callback.getEmail(e.errorCode)
            val code = e.message
            Log.d("soup", "error is $code")
            callback.getEmail(code!!)
            return null
               // return e.errorCode
            }
    }

    private fun saveUserToFirebaseDatabase(
        username: String,
        email: String,
        password: String,
        profileImageUrl: String?
    ) {
        Log.d("Debug", "entered firebase database function")
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        //val refP = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = User(username, email, password, profileImageUrl)

        val usern = CurrentUser()
        val profileUpdates =
            UserProfileChangeRequest.Builder().setDisplayName(username).build()
        usern?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        TAG, "profile updated, emitter complete?:  ${CurrentUser()?.displayName} ."
                    )
                } else {
                    Log.d(TAG, "in else in fetch current user")
                }
            }

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
       FirebaseDatabase.getInstance().getReference("users/$uid/UserBio").setValue("no bio")
        FirebaseDatabase.getInstance().getReference("users/$uid/profileImageUrl").setValue("null")
    }


    fun listenForUserProfilePosts(uid: String, callbackPost: PostRepository.FirebaseCallbackPost): PostLiveData {
        Log.d(TAG, "getUserProfilePosts listener called")
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid").child("Posts")
         reference.addChildEventListener(object : ChildEventListener {
            var profilePostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
             override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
             override fun onChildAdded(p0: DataSnapshot, p1: String?) {
               callbackPost.onSuccess(p0)
            }
             override fun onChildRemoved(p0: DataSnapshot) {}
        })
        Log.d("Post function return", "Post function return")

        val comref = FirebaseDatabase.getInstance().getReference("users/$uid")
         comref.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onCancelled(p0: DatabaseError) {
                 TODO("not implemented")
             }

             override fun onDataChange(p0: DataSnapshot) {
                 if (!p0.child("Posts").exists()) {
                     callbackPost.onSuccess(p0)
                     //noPostsCheck = true
                 }
             }
         })

        return profilePosts
    }

    fun listenForUserProfileComments(uid: String, callbackComment: PostRepository.FirebaseCallbackComment): CommentLive {
        Log.d(TAG, "getUserProfile comments listener called")
        //callbackComment.onStart()
        //is commenting this out why it noComments stopped showinf up?
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid").child("Comments")
        reference.addChildEventListener(object : ChildEventListener {
            var profileCommentList: MutableList<Comment> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                callbackComment.onSuccess(p0)
            }
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
        Log.d("Post function return", "Post function return")
        val comref = FirebaseDatabase.getInstance().getReference("users/$uid")
        comref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { TODO("not implemented") }
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.child("Comments").exists()) {
                    callbackComment.onSuccess(p0)
                }
              } })
        return Comments
    }


    fun noPostsChecker(userID: String, callbackbool: PostRepository.FirebaseCallbackBool): Boolean {
        val comref = FirebaseDatabase.getInstance().getReference("users/$userID")
        comref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { TODO("not implemented") }
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.child("Posts").exists()) {
                    callbackbool.onSuccess(p0)
                    noPostsCheck = true
                }
                else {
                    noPostsCheck = false
                    callbackbool.onSuccess(p0)

                }
            }
        })
        return noPostsCheck
    }

    fun noCommentsChecker(userID: String, callbackbool: PostRepository.FirebaseCallbackBool): Boolean {
        val com = FirebaseDatabase.getInstance().getReference("users/$userID")
        com.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { TODO("not implemented") }
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.child("Comments").exists()) {
                    callbackbool.onSuccess(p0)
                    noCommentsCheck = true

                }
                else if (p0.child("Comments").exists()){
                    callbackbool.onSuccess(p0)
                    noCommentsCheck = false
                }
            }
        })
        return noCommentsCheck
    }

    fun getUserProfilePosts(userID: String,  call : PostRepository.FirebaseCallbackPost): PostLiveData {
        Log.d(TAG, "getUserProfilePosts called")
        if (userID == "null") {
            var uid = FirebaseAuth.getInstance().uid ?: "error"
            listenForUserProfilePosts(uid, call )
        } else {
            var uid = userID
            listenForUserProfilePosts(uid, call)
        }
        return profilePosts
    }

    fun getUserProfileComments(userID: String, call : PostRepository.FirebaseCallbackComment): CommentLive {
        Log.d(TAG, "getUserProfile comments called")
        if (userID == "null") {
            var uid = FirebaseAuth.getInstance().uid ?: "error"
            listenForUserProfileComments(uid, call)
        } else {
            var uid = userID
            listenForUserProfileComments(uid, call)
        }
        return Comments
    }


    fun listenComments(Key: String, subject: String, callbackComment: FirebaseCallbackComment)
    {

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
        callbackComment.onCallback(Comments)

    }


    fun getCommentsCO(Key: String): CommentLive {

        //val uid = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("Subjects/Posts/$Key/Comments")

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
       //callbackComment.onCallback(Comments)
        return Comments

    }

    fun getComments(Key: String, subject: String): CommentLive {
        var com = CommentLive()
       listenComments(Key, subject, object : FirebaseCallbackComment{
           override fun onCallback(CommentL: CommentLive) {
               com.value = CommentL.value
           }
       })
        return com
    }


    fun saveNewCommentC(
        text: String, postID: String, crn: String
    ) {



        val userID = firebaseAuth.uid
        val comment = Comment(text, "", userID, crn, postID)
        val user_key = FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID")
            .child("Comments").push().key



        val comementvalues = comment.toMap()
        FirebaseDatabase.getInstance().getReference("users/$userID/Post/$postID")
            .child("Comments/$user_key").setValue(comementvalues)

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
                        comment.ref.removeValue()

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
                        post.ref.removeValue()

                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        queryuserref.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (post in p0.children) {
                        post.ref.removeValue()
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


        query.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(comment in p0.children){
                        comment.ref.removeValue()
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



        val queryuser: Query = refkey2.child("Comments").orderByChild("Classkey").equalTo(classkey)


        queryuser.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(comment in p0.children){
                        comment.ref.removeValue()
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
                        if(sub.getValue() == crn){
                            val class_key = FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$ClassKey").child("Comments").push().key
                            val user_key = FirebaseDatabase.getInstance().getReference("/users/$UserID/Posts/$postID").child("Comments").push().key
                            val profile_key = FirebaseDatabase.getInstance().getReference("/users/$UserID").child("Comments").push().key
                            val post_key: String? = FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$ClassKey").key



                            comment.Classkey = class_key
                            comment.UserComkey = user_key
                            comment.ProfileComKey = profile_key
                            comment.Postkey = post_key

                            val dataupdates = HashMap<String, Any>()
                            val comementvalues = comment.toMap()
                            dataupdates["Subjects/$crn/Posts/$ClassKey/Comments/$class_key"] = comementvalues
                            FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)

                            FirebaseDatabase.getInstance().getReference("/users/$userID")
                                .child("/Comments/$profile_key").setValue(comementvalues)
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
    }

    fun editComment(userID: String, usercomkey:String, ntext: String, crn: String, postKey: String){
        var comkey = FirebaseDatabase.getInstance().getReference("/users/$userID")

        val queryuser =comkey.child("Comments").orderByChild("ProfileComKey").equalTo(usercomkey)

        queryuser.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(comment in p0.children){
                        comment.ref.child("text").setValue(ntext)
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
                        comment.ref.child("text").setValue(ntext)
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
                        post.ref.child("text").setValue(ntext)
                        post.ref.child("title").setValue(ntitle)
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
                    if(!p0.exists()){
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
                if(!uri.isNullOrEmpty()){
                    list.add(7, uri)
                }
                callBack.onCallback(list)
            }
        })
    }

    // CRN is a placeholder for a class object
    fun saveNewImgPosttoUser(title: String, text: String, CRN: String, uri: Uri, imagePost: Boolean) {

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(uri).addOnSuccessListener {
            saveImageurl = "test to see HELLO"
            ref.downloadUrl.addOnSuccessListener {
                val urii = it
                saveImageurl = urii.toString()
                var post = Post(title, text, CRN,"")
                val userID = firebaseAuth.uid
                val author = firebaseAuth.currentUser?.displayName
                post.UserID = userID
                post.author = author
                post.imagePost = imagePost
                val user_key =
                    FirebaseDatabase.getInstance().getReference("/users/$userID").child("Posts")
                        .push().key
                val class_key =
                    FirebaseDatabase.getInstance().getReference("/Subjects/$CRN").child("Posts")
                        .push().key
                post.key = user_key
                post.Classkey = class_key
                val dataupdates = HashMap<String, Any>()
                post.uri = saveImageurl
                val postvalues = post.toMap()
                dataupdates["/Subjects/$CRN/Posts/$class_key"] = postvalues
                dataupdates["/users/$userID/Posts/$user_key"] = postvalues
                FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)
            }
        }

    }




    fun saveNewPosttoUser(text: String, title:String, CRN: String) {
        val userID = firebaseAuth.uid
        val author= firebaseAuth.currentUser?.displayName
        val post = Post(title, text, CRN,"")
        post.UserID = userID
        post.author = author
        val subpath = FirebaseDatabase.getInstance().getReference("/users/$userID")
         subpath.child("Subscriptions").orderByValue().addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(sub in p0.children){
                        // val refc= comment.getValue(Comment::class.java)
                        if(sub.getValue() == CRN){
                            val user_key = FirebaseDatabase.getInstance().getReference("/users/$userID").child("Posts").push().key
                            val class_key = FirebaseDatabase.getInstance().getReference("/Subjects/$CRN").child("Posts").push().key
                            post.key = user_key
                            post.Classkey = class_key
                            //post.author= auth_key
                            // implement in viewmodel
                            //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
                            val dataupdates = HashMap<String, Any>()
                            val postvalues = post.toMap()
                            dataupdates["/Subjects/$CRN/Posts/$class_key"] = postvalues
                            dataupdates["/users/$userID/Posts/$user_key"] = postvalues
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
    fun listenUserSub(callbackString: PostRepository.FirebaseCallbackString){
        val uid = FirebaseAuth.getInstance().uid
//        Log.d("uid", uid!!)
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid/Subscriptions").orderByValue()
        callbackString.onStart()
        reference.addValueEventListener(object : ValueEventListener {
            val SubList: MutableList<String> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {
                callbackString.onFailure()
            }

            override fun onDataChange(p0: DataSnapshot) {
                callbackString.onSuccess(p0)
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
                val sublist = p0.children
                for (x in sublist) {
                    Log.d("usersub", x.getValue(String::class.java)!!)
                    SubList.add(x.getValue(String::class.java)!!)
                }
                //UserSUB.value = SubList
            }
        })
    }

    fun sendUserSUB(call :PostRepository.FirebaseCallbackString) {
        listenUserSub(call)
    }

    fun addUserSUB(crn: String) {

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
                val sublist = p0.children
                for (x in sublist) {
                    if (crn == x.getValue(String::class.java)) {
                        Log.d("Remove", "remove hit")
                        x.key
                        reference.child(x.key!!).removeValue()
                    } else {
                        Log.d("usersub", x.getValue(String::class.java)!!)
                        SubList.add(x.getValue(String::class.java)!!)
                    }
                }

                UserSUB.value = SubList
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
                val sublist = p0.children
                for (x in sublist) {
                    if (uid == x.getValue(String::class.java)) {
                        Log.d("Remove", "remove hit")

                        reference.child(x.key!!).removeValue()
                    } else {
                        Log.d("usersub", x.getValue(String::class.java)!!)
                        SubList.add(x.getValue(String::class.java)!!)
                    }
                }

                UserSUB.value = SubList
            }
        })

    }

    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri) {

        //Log.d(TAG, "photo url is null")

        val filename = UUID.randomUUID().toString()
        //val uidm = FirebaseAuth.getInstance().uid
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        //val userref = FirebaseDatabase.getInstance().getReference("users/$uidm")
        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                Log.d("Pic", "Successfully uploaded picture: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    val urii = it
                    saveImageurl = urii.toString()
                    Log.d("Pic", "File location: $it")

                    var user = CurrentUser()

                    val uid = FirebaseAuth.getInstance().uid
                    val reference = FirebaseDatabase.getInstance().getReference("users/$uid")
                    reference.child("/profileImageUrl").setValue(saveImageurl)

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

    }

    fun listenforClassPosts(className: String, call : PostRepository.FirebaseCallbackPost)
    {

        val reference = FirebaseDatabase.getInstance().getReference("Subjects/$className")
        call.onStart()
        reference.addValueEventListener(object : ValueEventListener {
            var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {
                call.onFailure()
            }


            override fun onDataChange(p0: DataSnapshot) {
                call.onSuccess(p0)
            }



        })


    }

    fun getClassPosts(className: String, call : PostRepository.FirebaseCallbackPost) {
        listenforClassPosts(className, call)
    }

    fun getClasses(call: PostRepository.FirebaseCallbackCRN) {
        listenClasses(call)
    }

    fun combineSubs(listClasses : MutableLiveData<MutableList<CRN>>, UsersSubs :MutableList<String>) : Int
    {
        if(listClasses.value.isNullOrEmpty() || UsersSubs.isNullOrEmpty())
        {

            Log.d("Null", "one was null")
            return 0
        }
        else
        {
            for(data in listClasses.value!!.iterator())
            {
                if(UsersSubs.contains(data.name))
                {
                    data.Subscribed = true
                    Log.d("combine", data.name)
                }

            }
            return 1


        }

    }
    fun listenClasses(call: PostRepository.FirebaseCallbackCRN)
    {
        val reference = FirebaseDatabase.getInstance().getReference("Subjects")
        call.onStart()
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            var classes: MutableList<String> = mutableListOf()
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                call.onSuccess(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                call.onFailure()
            }
        })

    }


    fun SeparateList(p0: DataSnapshot): List<String>? {
        var subList: MutableList<String> = mutableListOf()
        for (datas in p0.children) {
            if (datas != null)
                subList.add(datas.getValue(String::class.java)!!)
        }
        return subList
    }


    private fun listenForSubscribedPosts2(callbackPost: FirebaseCallbackPost) {
        savedPosts = PostLiveData()
        val reference = FirebaseDatabase.getInstance().getReference("Subjects")
        var sub: MutableList<String> = mutableListOf()
         sendUserSUB(object : PostRepository.FirebaseCallbackString
        {
            override fun onFailure() {

            }

            override fun onStart() {

            }
            override fun onSuccess(data: DataSnapshot) {
                val size = data.hasChildren()
                Log.d("Size", size.toString())
                //var has :HashMap<String,String>? = hashMapOf()
                val sublist = data.children
                for (x in sublist) {
                    Log.d("usersub", x.getValue(String::class.java)!!)
                    sub.add(x.getValue(String::class.java)!!)
                }

            }
        })
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
                if (sub.isNullOrEmpty()) {
                    savedPosts.value = savedPostsList
                } else {
                    Log.d("FIRST1-:", p0.key ?: "no key")
                    if (sub.contains(p0.key)) {
                        for (p2 in p0.getChildren()) {
                            Log.d("FIRST2---:", p2.key ?: "no key")
                            if (p2.key == "Posts") {
                                var counter = 0
                                for (p3 in p2.getChildren().reversed()) {
                                    Log.d("FIRST3-------:", p3.key ?: "no key")
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
                                            it.uri = p3.child("uri").value.toString()
                                        }
                                    } catch (e: Exception) {
                                        Log.d("Data Error", "error converting to post")
                                    }

                                    if (newPost.key != null) {
                                        //Log.d("ACCESSING", newPost?.text)
                                        savedPostsList.add(newPost)
                                    }

                                    savedPosts.value = savedPostsList
                                    counter++
                                    if (counter == 2)
                                    {
                                        callbackPost.onCallback(savedPosts)
                                        break
                                    }
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

        var sub: MutableList<String> = mutableListOf()
        sendUserSUB(object : PostRepository.FirebaseCallbackString
        {
            override fun onFailure() {

            }

            override fun onStart() {

            }
            override fun onSuccess(data: DataSnapshot) {
                val size = data.hasChildren()
                Log.d("Size", size.toString())
                //var has :HashMap<String,String>? = hashMapOf()
                val sublist = data.children
                for (x in sublist) {
                    Log.d("usersub", x.getValue(String::class.java)!!)
                    sub.add(x.getValue(String::class.java)!!)
                }

            }
        })
            listenForSubscribedPosts2(object : FirebaseCallbackPost {
                override fun onCallback(PostL: PostLiveData) {
                    savedPosts.value = PostL.value
                }
            })



        return savedPosts




    }

    fun getUserPost() : PostLiveData
    {
        var post = PostLiveData()
        listenforUserPosts(object : FirebaseCallbackPost{
            override fun onCallback(PostL: PostLiveData) {
                post.value = PostL.value
            }
        })
        return post
    }


    private fun listenforUserPosts(callbackPost: FirebaseCallbackPost) {
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


                if (newPost.key != null) {
                    Log.d("ACCESSING", newPost.text ?: "no post")
                    savedPostsList.add(newPost)


                }
                savedPosts.value = savedPostsList
                callbackPost.onCallback(savedPosts)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })

    }



    fun getUsers(): MutableLiveData<List<User>>?{

        var userList: MutableLiveData<List<User>>? = MutableLiveData()
        setUsers(object: FirebaseCallback{
            override fun onCallback(list: MutableList<User>?) {
                userList?.value=list
            }
        })

        return userList
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
                        user.profileImageUrl = imageProfURL
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

                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    chatList.add(chatMessage)
                }

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


    fun sendMessage(message: String?, toID: String?, username: String?){

        val fromID = FirebaseAuth.getInstance().uid


        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toID/$fromID").push()

        val chatMessage = ChatMessage(
            reference.key!!,
            message,
            fromID!!,
            toID!!,
            System.currentTimeMillis() / 1000
        )

        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d("ChatLog", "Saved our chat message")
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
    fun UsersSearch(query: String, result : SearchViewModel.FirebaseResult)
    {
        result.onStart()
        val ref = FirebaseDatabase.getInstance().getReference("Subjects/$query")
       ref.addListenerForSingleValueEvent(object : ValueEventListener{
           override fun onCancelled(p0: DatabaseError) {
               result.onFailure("Query Failed")
           }

           override fun onDataChange(p0: DataSnapshot) {
               result.onSuccess(p0, FirebaseAuth.getInstance().uid.toString())
           }
       })
    }
    fun getallclasses(listen : SearchViewModel.FirebaseResult)
    {
        listen.onStart()
        val ref = FirebaseDatabase.getInstance().getReference("Subjects")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                listen.onFailure("Error")
            }

            override fun onDataChange(p0: DataSnapshot) {
                listen.onSuccess(p0, FirebaseAuth.getInstance().uid.toString())
            }
        })

    }
    fun blockedusersPosts()
    {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid/BlockedUsers")
       ref.addValueEventListener(object : ValueEventListener{
           override fun onDataChange(p0: DataSnapshot) {

           }

           override fun onCancelled(p0: DatabaseError) {

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
    interface FirebaseCallbackPost{
        fun onCallback(PostL : PostLiveData)
    }
    interface  FirebaseCallbackComment{
        fun onCallback(CommentL : CommentLive)
    }
    interface FirebaseCallbackCRN{
        fun onCallback(CRNL : MutableLiveData<MutableList<CRN>>)
    }
    interface FirebaseCallbackString{
        fun onCallback(subs : MutableList<String>)
    }

    interface FirebaseCallbackItem{
        fun onCallback(Item : String)
    }

    interface FirebaseCallbackBool{
        fun onCallback(Item : Boolean)
    }



    companion object {
        @Volatile
        private var instance: FirebaseData? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FirebaseData().also { instance = it }
            }
    }
}
















