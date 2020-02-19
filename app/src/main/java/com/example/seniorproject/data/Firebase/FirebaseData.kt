package com.example.seniorproject.data.Firebase
import android.util.Log
import com.example.seniorproject.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import java.util.*
import java.util.logging.Handler

private const val TAG = "MyLogTag"
@Singleton
@Suppress("unused")
class FirebaseData @Inject constructor() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    //var savedPosts: MutableLiveData<List<Post>> = MutableLiveData()
    var savedPosts : PostLiveData = PostLiveData()
    var changed : Boolean = false
    var classList: MutableLiveData<List<String>> = MutableLiveData()
    var classPostList: PostLiveData = PostLiveData()

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
                        runBlocking {
                            delay(500)
                            emitter.onComplete()
                            Log.d(TAG, "im delayed")
                        }
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


    fun saveNewPost(postTitle: String, postText: String, postSubject: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Subjects/$postSubject/posts").push()

        if (postTitle.isNotEmpty() && postText.isNotEmpty()) {
            val post = Post(postTitle, postText, postSubject)

            reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }
        }
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
                val newPost = p0.getValue(Post::class.java)

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




    fun getClassPosts(className: String) : PostLiveData{
        val reference = FirebaseDatabase.getInstance().getReference("Subjects/$className/posts")


        reference.addChildEventListener(object : ChildEventListener {
            var savedPostsList: MutableList<Post> = mutableListOf()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newPost = p0.getValue(Post::class.java)

                if (newPost != null) {
                    //Log.d("ACCESSING", newPost?.text)
                    savedPostsList.add(newPost)

                    //repository.saveNewPost(newPost)
                    //adapter.add(PostFrag(newPost.title, newPost.text))
                }
                classPostList.value = savedPostsList

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





