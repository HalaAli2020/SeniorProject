package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.seniorproject.MainForum.HomeFragment
import com.example.seniorproject.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class HomeFragmentViewModel(private val repository: PostRepository) : ViewModel() {


    var titlePost: String? = null
    var textPost: String? = null

    var postListener: PostListener? = null
   // private val disposables = CompositeDisposable()

   // var posts: MutableLiveData<List<Post>> = PostLiveData()
   var posts: PostLiveData = PostLiveData()
    var postdata :PostLiveData = PostLiveData.get()
    init {

        posts = repository.getSavedPosts()
    }

    fun savePostToDatabase() {

        if (titlePost.isNullOrEmpty() || textPost.isNullOrEmpty()) {
            postListener?.onFailure("please enter a title and text!")
            //Toast.makeText((RegisterActivity()), "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        repository.saveNewPost(titlePost!!, textPost!!)
    }
    fun getSavedPosts(): PostLiveData {

       posts = repository.getSavedPosts()
       return posts
    }
    fun editPost() : PostLiveData
    {
        postdata = getSavedPosts()
        return postdata
    }


}



