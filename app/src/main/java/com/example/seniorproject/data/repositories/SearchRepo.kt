package com.example.seniorproject.data.repositories



import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.viewModels.SearchViewModel
import javax.inject.Inject

class SearchRepo @Inject constructor(private val Firebase: FirebaseData) {


    fun search(query : String, reply: SearchViewModel.FirebaseResult)
    {
        Firebase.usersSearch(query, reply)

    }
    fun getallclasses(listen : SearchViewModel.FirebaseResult)
    {
        Firebase.getallclasses(listen)
    }


    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }
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