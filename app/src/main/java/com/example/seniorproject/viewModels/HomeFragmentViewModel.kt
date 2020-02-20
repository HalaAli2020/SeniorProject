package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.example.seniorproject.MainForum.CustomAdapter
import com.example.seniorproject.MainForum.HomeFragment
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
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
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "MyLogTag"

class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private lateinit var adapter: CustomAdapter
    private lateinit var postLiveData: Flow<PostLiveData>



    fun getSavedPosts(): Flow<PostLiveData> {

        return repository.getSavedPosts()
        //return posts
    }

    /*var listofposts = object : MutableLiveData<List<Post>>() {
        override fun onActive() {
            value?.let { return }

            viewModelScope.launch {
                var job: Flow<Unit>? = null
                    //mini piece
                repository.getSavedPosts()
            }//.launchIn

        }*/



        var posts: PostLiveData = PostLiveData()
    //var postdata: PostLiveData = PostLiveData.get()

    /*var listofposts = object : MutableLiveData<List<Post>>() {
        override fun onActive() {
            value?.let { return }

            viewModelScope.launch {
                var job: Flow<Unit>? = null


            }

        }*/


       /* init {
            var listofposts = object : MutableLiveData<List<Post>>() {
                override fun onActive() {
                    value?.let { return }

                    viewModelScope.launch {
                        var job: Flow<Unit>? = null

                        repository.getSavedPosts()
                    }

                }
            //FIX: launch viewmodelScope
            //repository.getSavedPosts()
        }


        fun getSavedPosts(): Flow<PostLiveData> {

            return repository.getSavedPosts()
            //return posts
        }*/

        fun editPost(): Flow<PostLiveData> {
        return getSavedPosts()
    }

        fun fetchCurrentUserName() = repository.fetchCurrentUserName()

        var user = repository.currentUser()


        //launch coroutines builder here and
        //emit and collect take that collection to home fragment

    }



