package com.example.seniorproject.data.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.viewModels.SearchViewModel
import javax.inject.Inject

class SearchRepo @Inject constructor(private val Firebase: FirebaseData) {


    fun Search(query : String, reply: SearchViewModel.FirebaseResult)
    {
        Firebase.UsersSearch(query, reply)

    }
    fun getallclasses(listen : SearchViewModel.FirebaseResult)
    {
        Firebase.getallclasses(listen)
    }
    fun getSubscribedPosts() = Firebase.getSubscribedPosts()
    //fun getClasses() = Firebase.getClasses()

    //fun getClassPosts(className: String) = Firebase.getClassPosts(className)

   /* fun getUserSub() : MutableLiveData<MutableList<String>>? {
        return Firebase.sendUserSUB()

    }*/

    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }
    //fun getSubs() = Firebase.getUsersSubsnClass()
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