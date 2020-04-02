package com.example.seniorproject.viewModels
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject



class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {


    var posts: PostLiveData = PostLiveData()
    var p: MutableList<Post>? = null

    init {

        posts = repository.getSubscribedPosts()
    }


    fun getSubscribedPosts(): PostLiveData {
        posts = repository.getSubscribedPosts()
        return posts
    }


    fun fetchCurrentUserName() = repository.fetchCurrentUserName()

    var user = repository.currentUser()


}