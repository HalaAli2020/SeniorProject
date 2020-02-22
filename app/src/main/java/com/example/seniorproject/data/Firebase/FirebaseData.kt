package com.example.seniorproject.data.Firebase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.squareup.okhttp.Dispatcher
import io.reactivex.Completable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine


private const val TAG = "MyLogTag"
@Singleton
@Suppress("unused")
class FirebaseData @Inject constructor() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    //var savedPosts : MutableLiveData<List<Post>> = MutableLiveData()
    var changed : Boolean = false

    fun CurrentUser() = firebaseAuth.currentUser

    fun logout() = firebaseAuth.signOut()

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


    fun RegisterUser(username: String, email: String, password: String) =
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
                            uid?.let { it1 -> saveUserToFirebaseDatabase(username, email, it1) }
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
                            val user = User(username, email, uid)
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
                        Log.d(TAG,CurrentUser()?.displayName ?: "the displayname login1")
                        GlobalScope.launch(Dispatchers.Main){
                            delay(500)
                            emitter.onComplete()
                            Log.d(TAG, "im delayed")
                        }
                        /*runBlocking {
                            delay(500)
                            emitter.onComplete()
                            Log.d(TAG, "im delayed")
                        }*/
                        Log.d(TAG, "user has logged in")
                        val currentuser = FirebaseAuth.getInstance().currentUser
                        currentuser?.let {
                            val username = currentuser.displayName
                            val email = currentuser.email
                            val uid = currentuser.uid
                            val user = User(username, email, uid)
                        }
                        Log.d(TAG,CurrentUser()?.displayName ?: "the displayname login2")
                    } else {
                        emitter.onError(it.exception!!)
                        //should not be using two exclaimation points
                    }
                }
            }
    }


    private fun saveUserToFirebaseDatabase(username: String, email: String, password: String) {
        Log.d("Debug", "entered firebase database function")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = User(username, email, password)
        fetchCurrentUserName()
        //log in and log out user here
        ref.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "saving to database worked")
            } else {
                Log.d(TAG, "not saved")
            }
        }.addOnFailureListener {
            Log.d(TAG, "Error ${it.message}")
        }
    }


    fun saveNewPost(postTitle: String, postText: String) {
        val reference = FirebaseDatabase.getInstance().getReference("/posts").push()

        if (postTitle.isNotEmpty() && postText.isNotEmpty()) {
            val post = Post(postTitle, postText)

            reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
        }
    }


    fun getSavedPost() : Flow<List<Post>>{
        //launch coroutine builder here
        //listenforPosts()

        //return savedPosts
        return getPostsFlow()
    }


    private val scope = CoroutineScope(Dispatchers.IO)

    @ExperimentalCoroutinesApi

    fun getPostsFlow(): Flow<List<Post>> = channelFlow{
        val databaseReference = FirebaseDatabase.getInstance().getReference("/posts")
        var savedPostsList: MutableList<Post?> = mutableListOf()
        val eventListener = databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                launch {
                    val newPost: Post? = p0.getValue(Post::class.java)

                    if(newPost != null){
                        Log.d("ACCESSING", newPost?.text)
                        savedPostsList.add(newPost)

                    }

                    savedPostsList.asFlow()
                }

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) = Unit

            override fun onChildMoved(p0: DataSnapshot, p1: String?) = Unit

            override fun onCancelled(p0: DatabaseError) = Unit
    })


        awaitClose {databaseReference.removeEventListener(eventListener) }

    }


            // var savedPostsList: MutableList<Post> = mutableListOf()
     /*       override fun onChildRemoved(p0: DataSnapshot) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        launch {
            val newPost = p0.getValue(Post::class.java)

            if(newPost != null){
                Log.d("ACCESSING", newPost?.text)
                // emitAll(newPost)
            }
        }

        /*if (newPost != null) {
            Log.d("ACCESSING", newPost?.text)
            savedPostsList.add(newPost)

            //repository.saveNewPost(newPost)
            //adapter.add(PostFrag(newPost.title, newPost.text))
        }*/
    }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

            override fun onCancelled(p0: DatabaseError) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    }
    )*/
  /* @ExperimentalCoroutinesApi
   private val flowpost = com.example.seniorproject.data.models.flowViaChannel<Post>
   {  val databaseReference = FirebaseDatabase.getInstance().getReference("/posts")
       val eventListener = databaseReference.addChildEventListener(object : ChildEventListener {
           var savedPostsList: MutableList<Post> = mutableListOf()
           override fun onChildRemoved(p0: DataSnapshot) {
           }

           override fun onChildAdded(p0: DataSnapshot, p1: String?) {
               val newPost = p0.getValue(Post::class.java)

               if (newPost != null) {
                   Log.d("ACCESSING", newPost?.text)
                   savedPostsList.add(newPost)

                   //repository.saveNewPost(newPost)
                   //adapter.add(PostFrag(newPost.title, newPost.text))
               }
               savedPosts.value = savedPostsList
           }

           override fun onChildChanged(p0: DataSnapshot, p1: String?) {
           }

           override fun onChildMoved(p0: DataSnapshot, p1: String?) {
           }

           override fun onCancelled(p0: DatabaseError) {
           }

       }
       )

           databaseReference.removeEventListener(eventListener)
       //potential problem here. wrap removeEventlistener into await?
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





