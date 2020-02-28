package com.example.seniorproject.viewModels

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModel
//import com.example.seniorproject.Utils.startMainForum
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class NewPostFragmentViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    var titlePost: String? = null
    var textPost: String? = null
    var classSpinner: String? = null
    var author: String? = repository.currentUser()?.displayName


    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            classSpinner = parent?.getItemAtPosition(position) as String
        }
    }


    var postListener: PostListener? = null


    fun savePostToDatabase() {

        if (titlePost.isNullOrEmpty() || textPost.isNullOrEmpty() || classSpinner.isNullOrEmpty()) {
            postListener?.onFailure("please enter a title and text!")
            //Toast.makeText((RegisterActivity()), "Please fill in both Email and Password fields", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("SELECTED VALUE:", classSpinner)
        repository.saveNewPost(titlePost!!, textPost!!,"1", classSpinner!!, author!!)

    }


}
