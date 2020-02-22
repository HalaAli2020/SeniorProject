package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.MainForum.CustomAdapter
import com.example.seniorproject.MainForum.HomeFragment
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "MyLogTag"

class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private lateinit var adapter: CustomAdapter
    private lateinit var listofposts: List<Post>

    //COLLECT VALUES HERE AND DISPLAY THEM IN HOME FRAGMENT
    //take flow from repository and emit values in view model.


    fun fetchCurrentUserName() = repository.fetchCurrentUserName()

    var user = repository.currentUser()



    fun getSavedPosts(): Flow<List<Post>> {
            return repository.getSavedPosts()
        }



    /*viewModelScope.launch {
        repository.getSavedPosts().collect {

        }

    }*/

   /* var listofposts = object : MutableLiveData<List<Post>>() {
        override fun onActive() {
            value?.let { return }

            viewModelScope.launch {
               // var job: Flow<Unit>? = null
                //mini piece
                repository.getSavedPosts().collect {

                    //store them in an arraylist
                }
            }//.launchIn

        }*/



       // var posts: PostLiveData = PostLiveData()
        //var postdata: PostLiveData = PostLiveData.get()

        /*var listofposts = object : MutableLiveData<List<Post>>() {
        override fun onActive() {
            value?.let { return }

            viewModelScope.launch {
                var job: Flow<Unit>? = null


            }

        }*/

  }

