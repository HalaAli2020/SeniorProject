package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.repositories.PostRepository

class HomeFragmentViewModel(private val repository: PostRepository) : ViewModel() {


    var titlePost: String? = null
    var textPost: String? = null

    var postListener: PostListener? = null



    fun savePostToDatabase() {

        if (titlePost.isNullOrEmpty() || textPost.isNullOrEmpty()) {
            postListener?.onFailure("please enter a title and text!")
            //Toast.makeText((RegisterActivity()), "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        repository.saveNewPost(titlePost!!, textPost!!)
    }

    fun getSavedPosts() = repository.getSavedPosts()
}



