package com.example.seniorproject.data.repositories

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






    companion object {
        @Volatile
        private var instance: SearchRepo? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: SearchRepo(firebasedata).also { instance = it }
            }
    }
}