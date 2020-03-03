package com.example.seniorproject.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

private const val TAG = "MyLogTag"

class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {


    var posts: PostLiveData = PostLiveData()
    var postdata: PostLiveData = PostLiveData.get()
    var p: MutableList<Post>? = null

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

    fun getSubscribedPosts(): PostLiveData {
        posts = repository.getSubscribedPosts()
        return posts
    }

    fun uploadUserProfileImage(selectedPhotoUri: Uri) =
        repository.uploadUserProfileImage(selectedPhotoUri)


    fun fetchCurrentUserName() = repository.fetchCurrentUserName()

    var user = repository.currentUser()


}