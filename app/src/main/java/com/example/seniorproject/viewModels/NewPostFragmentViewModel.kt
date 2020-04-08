package com.example.seniorproject.viewModels

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.repositories.PostRepository
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
    var bool = MutableLiveData<Boolean>()



    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            classSpinner = parent?.getItemAtPosition(position) as String
        }
    }


    private var postListener: PostListener? = null


    fun editPost(){
        repository.editPost(crn!!, postKey!!, ctext!!, ctitle!!, textPost!!, titlePost!!, userID!!)

    }
    fun savePostToDatabase() {

        if (titlePost.isNullOrBlank() || textPost.isNullOrBlank() || classSpinner.isNullOrBlank()) {
            postListener?.onFailure("please enter both a post title, a post body and select a class")
            bool.value = false
            return
        }
        Log.d("SELECTED VALUE:", classSpinner ?: " ")
        repository.saveNewPost(textPost!!,titlePost!!,classSpinner!!)
        bool.value = true
    }

   fun saveNewImgPosttoUser(title : String, text:String, CRN: String, uri: Uri, imagePost : Boolean) = repository.saveNewImgPosttoUser(title,text,CRN,uri,imagePost)

}
