package com.example.seniorproject.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.seniorproject.MainForum.Posts.UpdatePost
//import com.example.seniorproject.Utils.startMainForum
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewPostFragmentViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    var titlePost: String? = null
    var textPost: String? = null
    var classSpinner: String? = null
    var crn: String? = null
    var postKey: String? = null
    var ctext: String? = null
    var ctitle: String? = null
    var userID: String? = null
    //var author: String? = repository.currentUser()?.displayName


    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            classSpinner = parent?.getItemAtPosition(position) as String
        }
    }


    var postListener: PostListener? = null


    fun editPost(){
        repository.editPost(crn!!, postKey!!, ctext!!, ctitle!!, textPost!!, titlePost!!, userID!!)

    }
    fun savePostToDatabase() {

        if (titlePost.isNullOrEmpty() || textPost.isNullOrEmpty() || classSpinner.isNullOrEmpty()) {
            postListener?.onFailure("please enter a title and text!")
            //Toast.makeText((RegisterActivity()), "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("SELECTED VALUE:", classSpinner)
        repository.saveNewPost(textPost!!,titlePost!!,classSpinner!!)

    }

   fun saveNewImgPosttoUser(title : String, text:String, Subject: String, CRN: String, uri: Uri, imagePost : Boolean) = repository.saveNewImgPosttoUser(title,text,Subject,CRN,uri,imagePost)

}
