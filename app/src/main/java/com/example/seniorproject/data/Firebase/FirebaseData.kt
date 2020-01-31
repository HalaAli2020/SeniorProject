package com.example.seniorproject.data.Firebase

import android.util.Log
import android.widget.Toast
import com.example.seniorproject.Login.RegisterActivity
import com.example.seniorproject.data.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable

class FirebaseData {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun CurrentUser() = firebaseAuth.currentUser
    fun logout() = firebaseAuth.signOut()

    fun RegisterUser(email: String, password: String) = Completable.create { emitter ->
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
        Log.d("Debug", "Entered register user function!!! Email: " + email)
        Log.d("Debug", "pass: " + password)

        //Firebase Authentication is being performed inside the completeable
        //emitter indicated weather the task was completed
        //double check this code
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                        Log.d("Debug", "NEW USER, uid: ${it.result?.user?.uid}")
                        Toast.makeText(RegisterActivity(), "Account Creation successful", Toast.LENGTH_SHORT).show()
                        //saveUserToFirebaseDatabase()
                        //redirectToLogin()
                    }else {
                        emitter.onError(it.exception!!)
                        //return@addOnCompleteListener
                    }
                    //.addOnFailureListener {
                    //Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    //}
                }
            }
    }

              /*  if (!it.isSuccessful) return@addOnCompleteListener
                // else if successful
                Log.d("Debug", "NEW USER, uid: ${it.result?.user?.uid}")
                Toast.makeText(
                    RegisterActivity(),
                    "Account Creation successful",
                    Toast.LENGTH_SHORT
                ).show()
                //saveUserToFirebaseDatabase()
                //redirectToLogin()
            }
            .addOnFailureListener {
                //Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }*/

    fun LoginUser(email: String, password: String) = Completable.create { emitter ->

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
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

    companion object{
        @Volatile private var instance: FirebaseData? = null
        //rights to this properties are immediate visible ot other threats
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FirebaseData().also { instance = it }
            }
        //if the instance is not n
    }
}







