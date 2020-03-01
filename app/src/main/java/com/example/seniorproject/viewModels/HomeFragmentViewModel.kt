package com.example.seniorproject.viewModels

import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.seniorproject.MainForum.HomeFragment
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.auth.FirebaseAuth
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
import javax.inject.Inject

private const val TAG = "MyLogTag"

class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {


    var posts: PostLiveData = PostLiveData()
    var postdata: PostLiveData = PostLiveData.get()
    var p : MutableList<Post>? = null

    init {

        posts = repository.getSavedPosts()
    }


    fun getSavedPosts(): PostLiveData {

        posts = repository.getSavedPosts()
        return posts
    }
    fun getSavedUserPosts(): PostLiveData {

        posts = repository.getSavedUserPosts()
        return posts
    }

    fun editPost(): PostLiveData {
        postdata = getSavedPosts()
        return postdata
    }

    fun uploadUserProfileImage(selectedPhotoUri: Uri) = repository.uploadUserProfileImage(selectedPhotoUri)


    fun fetchCurrentUserName() = repository.fetchCurrentUserName()

    var user = repository.currentUser()




}