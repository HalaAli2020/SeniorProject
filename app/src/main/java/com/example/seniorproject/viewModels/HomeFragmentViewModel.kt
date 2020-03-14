package com.example.seniorproject.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.models.User
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

private const val TAG = "MyLogTag"

class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {


    var posts: PostLiveData = PostLiveData()
    var postdata: LiveData<MutableList<Post>>? = null
    var plist : MutableList<Post>? = null


    //var p: MutableList<Post>? = null
    var RepoUser : MutableLiveData<User>? = null
    var SessionUser : LiveData<User>? = RepoUser?.let {
        Transformations.map(it){
            us -> us

        }
    }

    init {

            Transformations.map(posts){
                    num -> num
                plist = num

            }


        //posts = repository.getSavedPosts()
        RepoUser = repository.SessionUser()
    }




    fun getSavedUserPosts(){

        //posts = repository.getSavedUserPosts()
        //return posts
    }


    fun getSubscribedPosts() {
        posts = repository.getSubscribedPosts()
        //return posts
    }
    fun getPost() : LiveData<MutableList<Post>>?{
        return postdata
    }

    fun uploadUserProfileImage(selectedPhotoUri: Uri) =
        repository.uploadUserProfileImage(selectedPhotoUri)


    fun fetchCurrentUserName() = repository.fetchSessionUserName()

    var user = repository.SessionUser()


}