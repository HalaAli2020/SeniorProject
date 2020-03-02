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
    var profilePosts : PostLiveData = PostLiveData()


    private lateinit var postlistener : ValueEventListener
    private lateinit var userprofile : User
    var newComments : Comment? = null


    var newProfilePosts : Post? = null


    var classList : MutableList<CRN> = mutableListOf()
    var classPostList : PostLiveData = PostLiveData()
    var MainPosts : MutableList<Post> = mutableListOf()
    var UserSUB : MutableList<String>? = null
    fun CurrentUser() = FirebaseAuth.getInstance().currentUser



    fun CurrentUserL() {
        val reference =
            FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid!!)

        var changed: Boolean = false
        var classList: MutableLiveData<List<String>> = MutableLiveData()
        var classPostList: PostLiveData = PostLiveData()

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
                            uid?.let { it1 ->
                                saveUserToFirebaseDatabase(
                                    username,
                                    email,
                                    it1,
                                    profileImageUrl
                                )
                            }
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
                        Log.d(TAG, CurrentUser()!!.displayName ?: "the displayname login1")
                        GlobalScope.launch(Dispatchers.Main) {
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
                            val profileImageUrl = currentuser.photoUrl  // not
                            //Log.d(TAG,currentuser!!.photoUrl.toString() ?: "the displayname login2")
                            val user = User(username, email, uid, profileImageUrl)
                            //user not being used
                        }
                        Log.d(
                            TAG,
                            FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
                                ?: "the displayname login2"
                        )
                    } else {
                        emitter.onError(it.exception!!)
                        //should not be using two exclaimation points
                    }
                }
            }
    }


    private fun saveUserToFirebaseDatabase(
        username: String,
        email: String,
        password: String,
        profileImageUrl: Uri
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


    fun saveNewPost(postTitle: String, postText: String, postSubject: String) {
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


    fun listenForUserProfilePosts (): PostLiveData{
        Log.d(TAG, "getUserProfilePosts listener called")
        val uid = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid").child("Posts")

        val profilePostListen = reference.addChildEventListener(object : ChildEventListener {
            var profilePostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newProfilePost = Post()
                try {
                    newProfilePost.let {
                        it.text = p0.child("text").value.toString()
                        it.title = p0.child("title").value.toString()
                        it.key = p0.child("Key").value.toString()
                        // class key is key for this post
                        it.Classkey = p0.child("Classkey").value.toString()
                        // user who posted id
                        it.UserID = p0.child("UserID").value.toString()
                        // need to change this later subject should be subject crn should be different
                        it.crn = p0.child("subject").value.toString()
                        it.author = p0.child("Author").value.toString()
                    }
                } catch (e: Exception) {
                    Log.d("Data Error", "error converting to post")
                }



                if (newProfilePost != null) {
                    Log.d(TAG, newProfilePost.title ?: " Accessing profile post title")
                    Log.d(TAG, newProfilePost.text ?: " Accessing profile post text")
                    Log.d(TAG, newProfilePost.key ?: " Accessing profile post title")
                    Log.d(TAG, newProfilePost.Classkey ?: " Accessing profile post title")
                    Log.d(TAG, newProfilePost.UserID ?: " Accessing profile post title")
                    Log.d(TAG, newProfilePost.crn ?: " Accessing profile post title")

                    profilePostsList.add(newProfilePost)

                    newProfilePosts = newProfilePost

                }
                profilePosts.value = profilePostsList
                //Log.d("TAG", "Comments loaded")

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }




        })
        Log.d("Post function return", "Post function return")

        return profilePosts
    }

    fun getUserProfilePosts() : PostLiveData{
        Log.d(TAG, "getUserProfilePosts called")
        listenForUserProfilePosts()
        return profilePosts
    }




    fun getCommentsCO(Key : String) : CommentLive {

        val uid = FirebaseAuth.getInstance().uid
        val reference =
            FirebaseDatabase.getInstance().getReference("users/$uid/Post/$Key").child("Comments")

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

    fun getComments(Key: String, subject: String): CommentLive {
        val uid = FirebaseAuth.getInstance().uid
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


    fun saveNewCommentC(text: String, postID: String, ClassKey: String, UserID: String, crn: String
    ) {


        //val subject = Subject
        //val ClassID = Classkey
        val userID = firebaseAuth.uid
        val comment = Comment(text, 0, userID, crn, postID)
        //FIX userprofile not init post.author = userprofile.username!!
        //val Class_key = FirebaseDatabase.getInstance().getReference(CRN).child("Posts").push().key
        //FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID")
        val User_key = FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID")
            .child("Comments").push().key
        //val Class_key = FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID").child("Comments").push().key
        // implement in viewmodel
        //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
        val dataupdates = HashMap<String, Any>()
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

    fun saveNewComment(text: String, postID: String, ClassKey: String, UserID: String, crn: String
    ) {


        //val subject = Subject
        //val ClassID = Classkey
        val userID = firebaseAuth.uid
        val author= firebaseAuth.currentUser?.displayName
        val comment = Comment(text, 0, userID, crn, postID)
        comment.author= author
        Log.d("BigMoods", crn)
        //FIX userprofile not init post.author = userprofile.username!!
        //val Class_key = FirebaseDatabase.getInstance().getReference(CRN).child("Posts").push().key
        //FirebaseDatabase.getInstance().getReference("/users/$userID/Post/$postID")
        val Class_key =
            FirebaseDatabase.getInstance().getReference("/Subjects/$crn/Posts/$ClassKey")
                .child("Comments").push().key
        val User_key = FirebaseDatabase.getInstance().getReference("/users/$UserID/Posts/$postID")
            .child("Comments").push().key
        // implement in viewmodel
        //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
        comment.ClassComkey = Class_key
        comment.UserComkey = User_key
        val dataupdates = HashMap<String, Any>()
        val comementvalues = comment.toMap()
        dataupdates["Subjects/$crn/Posts/$ClassKey/Comments/$Class_key"] = comementvalues
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
    fun saveNewPosttoUser(post: Post, Subject: String, CRN: String) {
        val subject = Subject
        // val ClassID = Classkey
        val userID = firebaseAuth.uid
        val author= firebaseAuth.currentUser?.displayName
        post.UserID = userID
        post.author=author
        //FIX userprofile not init post.author = userprofile.username!!
        //val Class_key = FirebaseDatabase.getInstance().getReference(CRN).child("Posts").push().key
        //FirebaseDatabase.getInstance().getReference("/users/$userID")
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
        /*Class_reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
         */

        //}
    }

    fun getSavedPost(): PostLiveData {
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
                val newPost: Post? = p0.getValue(Post::class.java)

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

    fun getUserSub() {

        val uid = FirebaseAuth.getInstance().uid
        Log.d("uid", uid!!)
        val reference = FirebaseDatabase.getInstance().getReference("users/$uid/Subscriptions")
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
        var SubAdd = HashMap<String, String>()
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
        if (selectedPhotoUri == null) {
            return
        }

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
                val post = p0.getValue()
                //val newPost = Post()
                var postdetails: Iterable<DataSnapshot> = p0.children
                //for (n in postdetails) {
                var newPost = Post()
                newPost.let {

                    //Log.d("ACCESSING", newPost?.text)
                    it.title = p0.child("title").getValue(String::class.java)
                    it.text = p0.child("text").getValue(String::class.java)
                    //newPost.author = pos.child("author").getValue(String::class.java)
                    it.crn = className
                    Log.d("CRN", p0.key!!)
                    //newPost.subject = post.child("subject").getValue(String ::class.java)
                    //newPost.ptime = pos.child("Timestamp").getValue(Long::class.java)
                    it.key = p0.child("key").getValue(String::class.java)
                    it.Classkey = p0.child("Classkey").getValue(String::class.java)
                    it.UserID = p0.child("UserID").getValue(String::class.java)
                        it.author= p0.child("author").getValue(String::class.java)

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

    fun getSavedUserPost(): PostLiveData {
        return savedPosts
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
                Log.d("FIRST1-:", p0.key)
                if(sub!!.contains(p0.key)) {
                    for (p2 in p0.getChildren()) {
                        Log.d("FIRST2---:", p2.key)
                        if(p2.key=="Posts") {
                            var counter = 0
                            for (p3 in p2.getChildren().reversed()) {
                                Log.d("FIRST3-------:", p3.key)
                                val newPost = Post()
                                try {
                                    newPost.let {
                                        it.text = p3.child("text").value.toString()
                                        it.title = p3.child("title").value.toString()
                                        it.key = p3.child("Key").value.toString()
                                        // class key is key for this post
                                        it.Classkey = p3.child("Classkey").value.toString()
                                        // user who posted id
                                        it.UserID = p3.child("UserID").value.toString()
                                        // need to change this later subject should be subject crn should be different
                                        it.crn = p3.child("subject").value.toString()
                                    }
                                } catch (e: Exception) {
                                    Log.d("Data Error", "error converting to post")
                                }

                                if (newPost != null) {
                                    Log.d("ACCESSING", newPost?.text)
                                    savedPostsList.add(newPost)
                                }

                                savedPosts.value = savedPostsList
                                counter++
                                if(counter==2)break
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
                        //added crn so it would show up in home frag
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














