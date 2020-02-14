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
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.google.android.gms.tasks.Task
import com.google.common.collect.Iterables
import com.google.common.collect.Iterables.size
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap

private const val TAG = "MyLogTag"
@Singleton
@Suppress("unused")
class FirebaseData @Inject constructor() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

   // var savedPosts: MutableLiveData<List<Post>> = MutableLiveData()
    var savedPosts : PostLiveData = PostLiveData()
    var userPosts : PostLiveData = PostLiveData()
    private lateinit var postlistener : ValueEventListener
    private lateinit var userprofile : User


    fun CurrentUser() : User
    {
        val reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.uid!!)


       postlistener = object : ValueEventListener {
           //var savedPostsList: MutableList<Post> = mutableListOf()
           override fun onDataChange(p0: DataSnapshot) {
               val username = p0.child("username").getValue(String::class.java)
               val email = p0.child("email").getValue(String::class.java)
               userprofile = User(username, email, firebaseAuth.uid)



               if (userprofile != null) {
                   Log.d("ACCESSING", userprofile?.username)
                   //savedPostsList.add(newPost)

                   //repository.saveNewPost(newPost)
                   //adapter.add(PostFrag(newPost.title, newPost.text))
               }


           }
           override fun onCancelled(p0: DatabaseError) {

           }


       }
        reference.addValueEventListener(postlistener)
        return userprofile
    }
    // = firebaseAuth.currentUser


    fun logout()
    {
        firebaseAuth.signOut()

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
                        emitter.onComplete()
                        Log.d(TAG, "user has logged in")
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


    private fun saveUserToFirebaseDatabase(username: String, email: String, password: String) {
        Log.d("Debug", "entered firebase database function")
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = User(username, email, password)

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


    // CRN is a placeholder for a class object
    /*fun saveNewPost(post : Post, crn : CRN) {
        val subject = crn.subject
        val ClassID = crn.classID
        val userID = firebaseAuth.uid
        post.author = userprofile.username!!
        val Class_key = FirebaseDatabase.getInstance().getReference(CRN.crn).child("Posts").push().key
        val User_key = FirebaseDatabase.getInstance().getReference(firebaseAuth.uid!!).child("Posts").push().key
            // implement in viewmodel
        //if (post.title.isNotEmpty() && post.text.isNotEmpty()) {
        val dataupdates = HashMap<String, Any>()
       // val postvalues = post.toMap()
        dataupdates["$subject/$ClassID/Post/$Class_key"] = postvalues
        dataupdates["$userID/Posts/$User_key"] = postvalues
        FirebaseDatabase.getInstance().reference.updateChildren(dataupdates)

        /*Class_reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
            }.addOnFailureListener {
                Log.d(TAG, "Error ${it.message}")
            }

        //}
    }*/ */
    fun getSavedPost() : PostLiveData{
        listenforPosts()
        return savedPosts
    }

    fun getSavedPost(crn : String, subject : String) : PostLiveData{
        listenforPosts(crn, subject)
        return savedPosts
}
    private fun listenforPostsvtwo(crn : String, subject : String) {
        val reference = FirebaseDatabase.getInstance().getReference("Subjects/$subject/$crn/posts")


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


    private fun listenforPosts(crn : String, subject : String) {
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

    private fun getclass(subject :String ,crn : String)
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
                   newPost.subject = post.child("subject").getValue(String ::class.java)
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






