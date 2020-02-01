package com.example.seniorproject.data.Firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.seniorproject.Login.RegisterActivity
import com.example.seniorproject.data.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import io.reactivex.Completable

//@Module
//@Suppress("unused")
private const val TAG = "MyLogTag"
class FirebaseData {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun CurrentUser() = firebaseAuth.currentUser


    fun logout() = firebaseAuth.signOut()

    fun RegisterUser(username: String,email: String, password: String) = Completable.create { emitter ->
        /* Completable that is an RxJava class.  Completable basically means it holds something that will complete and we can get an indication when it is completed or failed.
         And it is the perfect class to use with FirebaseAuth because auth is a network operation that will complete
        val email = email_signup_editText.text.toString()
        val password = password_signup_editTExt.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                RegisterActivity(),
                "Please fill in both Email and Password fields",
                Toast.LENGTH_SHORT
            ).show()
            //unsure about registeractivity in place of context here
        }*/
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
                        saveUserToFirebaseDatabase(username, email, password)
                    } else {
                        emitter.onError(it.exception!!)
                        //return@addOnCompleteListener
                    }
                }
            }
    }


    fun LoginUser(email: String, password: String) = Completable.create { emitter ->

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
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
        }.addOnFailureListener() {
            Log.d(TAG, "Error ${it.message}")
        }
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






