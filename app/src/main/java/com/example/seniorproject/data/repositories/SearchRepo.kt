package com.example.seniorproject.data.repositories



import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.interfaces.FirebaseResult
import javax.inject.Inject

class SearchRepo @Inject constructor(private val Firebase: FirebaseData) {


/* This function makes a call to grab all of the classes available to return for the search done  */
    fun getallclasses(listen : FirebaseResult)
    {
        Firebase.getallclasses(listen)
    }

// This function is called when the user tries to subscribe to a new class
    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }
    // This function is called when the user tries to unsubscribe from a class
    fun remUsersub(crn: String) {
        Firebase.removeUserSub(crn)
        Firebase.removeClassSub(crn)
    }





    companion object {
        @Volatile
        private var instance: SearchRepo? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: SearchRepo(firebasedata).also { instance = it }
            }
    }
}

