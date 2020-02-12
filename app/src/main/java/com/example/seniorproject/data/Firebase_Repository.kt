package com.example.seniorproject.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable

class FirebaseRepository
{
    val firebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()



    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }
    fun currentuser() = firebaseAuth.currentUser

    fun signup(email: String, password: String) = Completable.create { emitter ->
    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
        if (!emitter.isDisposed) {
            if (it.isSuccessful)
                emitter.onComplete()
            else
                emitter.onError(it.exception!!)
        }
    }


}


}
