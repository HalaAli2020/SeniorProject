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
import com.example.seniorproject.data.models.Post
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot

private const val TAG = "MyLogTag"
@Singleton
@Suppress("unused")
class FirebaseData @Inject constructor() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    var savedPosts: MutableLiveData<List<Post>> = MutableLiveData()

   //@Provides
    fun CurrentUser() = firebaseAuth.currentUser


    fun logout() = firebaseAuth.signOut()

    //@Provides
    fun RegisterUser(username: String,email: String, password: String) = Completable.create { emitter ->
        Log.d(TAG, "Entered register user function!!! Email: " + email)
        Log.d(TAG, "pass: " + password)

        //Firebase Authentication is being performed inside the completeable
        //emitter indicated weather the task was completed
        //double check this code
        //return livedata objext
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                        Log.d(TAG, "NEW USER, uid: ${it.result?.user?.uid}")
                        saveUserToFirebaseDatabase(username, email, password)
                    } else {
                        emitter.onError(it.exception!!)
                        //return@addOnCompleteListener
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



     fun saveUserToFirebaseDatabase(username: String, email: String, password: String) {
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
         }.addOnFailureListener() {
             Log.d(TAG, "Error ${it.message}")
         }
     }
         fun saveNewPost(postTitle: String, postText: String) {
             val reference = FirebaseDatabase.getInstance().getReference("/posts").push()

             if (postTitle.isNotEmpty() && postText.isNotEmpty()) {
                 val post = Post(postTitle, postText, 0, "")

                 reference.setValue(post).addOnSuccessListener {
                     Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
                 }.addOnFailureListener {
                     Log.d(TAG, "Error ${it.message}")
                 }
             }
         }


         fun getSavedPost(): LiveData<List<Post>> {
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
    return savedPosts

     }




}






