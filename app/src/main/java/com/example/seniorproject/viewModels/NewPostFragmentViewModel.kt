package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
//import com.example.seniorproject.Utils.startMainForum
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class NewPostFragmentViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    var titlePost: String? = null
    var textPost: String? = null

    var postListener: PostListener? = null


    fun savePostToDatabase() {

        if (titlePost.isNullOrEmpty() || textPost.isNullOrEmpty()) {
            postListener?.onFailure("please enter a title and text!")
            //Toast.makeText((RegisterActivity()), "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        val post = Post(titlePost, textPost)
        Log.d("BIGMood", titlePost)
        repository.saveNewPost(post)


    }


}
