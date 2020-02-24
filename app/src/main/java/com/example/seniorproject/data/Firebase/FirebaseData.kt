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
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*
import java.util.logging.Handler
import kotlin.collections.HashMap
//import kotlin.reflect.jvm.internal.impl.load.java.lazy.ContextKt.child
//import javax.swing.UIManager.put



private const val TAG = "MyLogTag"
@Singleton
@Suppress("unused")
class FirebaseData @Inject constructor() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    //var savedPosts: MutableLiveData<List<Post>> = MutableLiveData()
    var savedPosts : PostLiveData = PostLiveData()
    var userPosts : PostLiveData = PostLiveData()
    var Comments : CommentLive = CommentLive()
    private lateinit var postlistener : ValueEventListener
    private lateinit var userprofile : User
    var newComments : Comment? = null
    var classList : MutableLiveData<List<String>> = MutableLiveData()
    var classPostList : PostLiveData = PostLiveData()
    fun CurrentUser() = FirebaseAuth.getInstance().currentUser


    fun CurrentUserL()
    {
        val reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!)

        var changed : Boolean = false
        var classList: MutableLiveData<List<String>> = MutableLiveData()
        var classPostList: PostLiveData = PostLiveData()

        postlistener = object : ValueEventListener {
            //var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onDataChange(p0: DataSnapshot) {
                val username = p0.child("Username").getValue(String::class.java)
                val email = p0.child("email").getValue(String::class.java)
                var profileImageUrl =p0.child("profileImageUrl").getValue(Uri::class.java)
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


    fun logout()
    {
        firebaseAuth.signOut()

    }


    fun fetchCurrentUserName() {
        var uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {

                val currentUser = p0.getValue(User::class.java)
                Log.d(TAG, "Current user fetched ${currentUser?.username}")
                var usernameForum = currentUser?.username.toString()
                val user = CurrentUser()
                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(usernameForum).build()
                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "profile updated, emitter complete?:  ${CurrentUser()?.displayName} .")
                        }
                        else {
                            Log.d(TAG, "in else in fetch current user")
                        }
                    }
            }
        })
    }


    fun RegisterUser(username: String, email: String, password: String, profileImageUrl: Uri) =
        Completable.create { emitter ->
            Log.d(TAG, "Entered register user function!!! Email: " + email)
            Log.d(TAG, "pass: " + password)

            //Firebase Authentication is being performed inside the completeable
            //emitter indicated weather the task was completed
            //double check this code
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!emitter.isDisposed) {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                            Log.d(TAG, "NEW USER, uid: ${it.result?.user?.uid}")
                            val uid = FirebaseAuth.getInstance().uid
                            uid?.let { it1 -> saveUserToFirebaseDatabase(username, email, it1, profileImageUrl) }
                        } else {
                            emitter.onError(it.exception!!)
                            //return@addOnCompleteListener
                        }
                    }
                }
        }


    fun resetPassword(email: String) = Completable.create { emitter ->

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                        Log.d(TAG, "Email sent")
                        val currentuser = FirebaseAuth.getInstance().currentUser
                        currentuser?.let {
                            val username = currentuser.displayName
                            val email = currentuser.email
                            val uid = currentuser.uid
                            val profileImageUrl = currentuser.photoUrl
                            val user = User(username, email, uid, profileImageUrl)
                        }

                    } else {
                        emitter.onError(it.exception!!)
                        //should not be using two exclaimation points
                    }

                }
            }


    }

    //@Provides
    fun LoginUser(email: String, password: String) = Completable.create { emitter ->
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        fetchCurrentUserName()
                        Log.d(TAG,CurrentUser()!!.displayName ?: "the displayname login1")
                        GlobalScope.launch(Dispatchers.Main){
                            delay(500)
                            emitter.onComplete()
                            Log.d(TAG, "im delayed")
                        }
                        //updateUser()
                        val currentuser = FirebaseAuth.getInstance().currentUser
                        currentuser?.let {
                            val username = currentuser.displayName
                            val email = currentuser.email
                            val uid = currentuser.uid
                            val profileImageUrl = currentuser.photoUrl  // grabs a null photo
                            val user = User(username, email, uid, profileImageUrl)
                        }
                        Log.d(TAG,currentuser!!.displayName ?: "the displayname login2")
                    } else {
                        emitter.onError(it.exception!!)
                        //should not be using two exclaimation points
                    }
                }
            }
    }

    /*private fun updateUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref= FirebaseDatabase.getInstance().getReference("users/$uid")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val currentuser = FirebaseAuth.getInstance().currentUser

                    val username = currentuser?.displayName
                    val email = currentuser?.email
                    val password = currentuser?.uid
                    val profileImageUrl = currentuser?.photoUrl
                    val user = User(username, email, password, profileImageUrl)
                    val postValues = user.toMap().toMutableMap()
                    for (snapshot in dataSnapshot.children) {
                        postValues[snapshot.key] = snapshot.value
                    }
                    postValues["profileImageUrl"] = profileImageUrl
                    val uid = FirebaseAuth.getInstance().uid
                    val ref= FirebaseDatabase.getInstance().getReference("users/$uid")
                    ref.updateChildren(postValues)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("error","database could not be updated")
                }
            }
            )
    }*/



    private fun saveUserToFirebaseDatabase(username: String, email: String, password: String, profileImageUrl: Uri) {
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


    fun saveNewPost(postTitle: String, postText: String, postSubject : String) {
        val reference = FirebaseDatabase.getInstance().getReference("/posts").push()

        if (postTitle.isNotEmpty() && postText.isNotEmpty()) {
            val post = Post(postTitle, postText, postSubject)


            reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
        }
    }

    fun getCommentsCO(Key : String) : CommentLive {

        val uid = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid/Post/$Key").child("Comments")

        val commentListen = reference.addChildEventListener(object : ChildEventListener {
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
                    Log.d("ACCESSING", newComment?.text)
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

    fun getComments(Key : String) : CommentLive  {
        val uid = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid/Posts/$Key").child("Comments")

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
                    Log.d("ACCESSING", newComment?.text)
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






    fun saveNewCommentClass(text: String, postID : String) {


        //val subject = Subject
        //val ClassID = Classkey
        val userID = firebaseAuth.uid
        val comment = Comment(text,0, userID, "csc495", postID)
        //FIX userprofile not init post.author = userprofile.username!!
        //val Class_key = FirebaseDatabase.getInstance().getReference(CRN).child("Posts").push().key
        //FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID")
        val User_key = FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID").child("Comments").push().key
        //val Class_key = FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID").child("Comments").push().key
        // implement in viewmodel
        //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
        val dataupdates = HashMap<String, Any>()
        val comementvalues = comment.toMap()
        //dataupdates["$subject/$ClassID/Post/$Class_key"] = postvalues
        //dataupdates["$userID/Posts/$User_key"] = postvalues
        FirebaseDatabase.getInstance().getReference("users/$userID/Post/$postID").child("Comments/$User_key").setValue(comementvalues)

        /*Class_reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
         */

        //}
    }
    fun saveNewComment(text: String, postID : String, ClassKey: String, UserID : String) {


        //val subject = Subject
        //val ClassID = Classkey
        val userID = firebaseAuth.uid
        val comment = Comment(text,0, userID, "csc495", postID)
        //FIX userprofile not init post.author = userprofile.username!!
        //val Class_key = FirebaseDatabase.getInstance().getReference(CRN).child("Posts").push().key
        //FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID")
        val Class_key = FirebaseDatabase.getInstance().getReference("/Subjects/CSC1500/Posts/$ClassKey").child("Comments").push().key
        val User_key = FirebaseDatabase.getInstance().getReference("/users/$UserID/Posts/$postID").child("Comments").push().key
        // implement in viewmodel
        //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
        comment.ClassComkey = Class_key
        comment.UserComkey = User_key
        val dataupdates = HashMap<String, Any>()
        val comementvalues = comment.toMap()
        dataupdates["Subjects/CSC1500/Posts/$ClassKey/Comments/$Class_key"] = comementvalues
        dataupdates["users/$UserID/Posts/$postID/Comments/$User_key"] = comementvalues
        //FirebaseDatabase.getInstance().getReference("users/$userID/Post/$postID").child("Comments/$User_key").setValue(comementvalues)
        FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)
        /*Class_reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
         */

        //}
    }

    // CRN is a placeholder for a class object
    fun saveNewPosttoUser(post : Post, Subject: String, CRN : String) {


        val subject = Subject
        // val ClassID = Classkey
        val userID = firebaseAuth.uid
        post.UserID = userID
        //FIX userprofile not init post.author = userprofile.username!!
        //val Class_key = FirebaseDatabase.getInstance().getReference(CRN).child("Posts").push().key
        //FirebaseDatabase.getInstance().getReference("/users/$userID")
        val User_key = FirebaseDatabase.getInstance().getReference("/users/$userID").child("Posts").push().key
        val Class_key = FirebaseDatabase.getInstance().getReference("/Subjects/$CRN").child("Posts").push().key
        post.key = User_key
        post.Classkey = Class_key
        // implement in viewmodel
        //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
        val dataupdates = HashMap<String, Any>()
        val postvalues = post.toMap()
        dataupdates["/Subjects/$CRN/Posts/$Class_key"] = postvalues
        dataupdates["/users/$userID/Posts/$User_key"] = postvalues
        //FirebaseDatabase.getInstance().getReference("users/$userID").child("Post/$User_key").setValue(postvalues)
        FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)
        /*Class_reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
         */

        //}
    }
    fun getSavedPost() : PostLiveData{
        listenforPosts()
        return savedPosts
    }



    private fun listenforPosts() {
        val reference = FirebaseDatabase.getInstance().getReference("/posts")


        reference.addChildEventListener(object : ChildEventListener {
            var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newPost : Post? = p0.getValue(Post::class.java)

                if (newPost != null) {
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


    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri){
        if(selectedPhotoUri == null){
            return
        }

        Log.d(TAG,"photo url is null")

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
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


    fun getClassPosts(className: String) : PostLiveData{
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
                val post = p0.getValue()
                val newPost = Post()
                //var postdetails: Iterable<DataSnapshot> = p0.children

                //Log.d("ACCESSING", newPost?.text)
                newPost.title = p0.child("title").getValue(String::class.java)
                newPost.text = p0.child("text").getValue(String::class.java)
                //newPost.author = pos.child("author").getValue(String::class.java)
                newPost.crn = p0.child("crn").getValue(String::class.java)
                //newPost.subject = post.child("subject").getValue(String ::class.java)
                //newPost.ptime = pos.child("Timestamp").getValue(Long::class.java)
                newPost.key = p0.child("key").getValue(String::class.java)
                newPost.Classkey = p0.child("Classkey").getValue(String::class.java)
                newPost.UserID = p0.child("UserID").getValue(String :: class.java)
                // comments might need to be gotten separatley to properly convert values

                //repository.saveNewPost(newPost)
                //adapter.add(PostFrag(newPost.title, newPost.text))

                if (newPost != null) {
                    Log.d("ACCESSING", newPost?.text)
                    if (savedPostsList.size < 3) {
                        savedPostsList.add(newPost)
                    }

                    classPostList.value = savedPostsList

                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })

        return classPostList
    }



    fun getClasses() : MutableLiveData<List<String>>{
        val reference = FirebaseDatabase.getInstance().getReference().child("/Subjects")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            var classes: MutableList<String> = mutableListOf()
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (datas in dataSnapshot.children) {
                    val classnames = datas.key
                    Log.d("BIGMOODS", classnames)
                    classnames?.let { classes.add(it) }
                }
                classList.value = classes
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        return classList
    }
    fun getSavedUserPost() : PostLiveData
    {
        return savedPosts
    }


    private fun listenforUserPosts() {
        // val userID = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("users/1XN3H62rMJhe6CiCbN4os2TNp5H2").child("Post")


        reference.addChildEventListener(object : ChildEventListener {
            var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newPost :Post  = Post()
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