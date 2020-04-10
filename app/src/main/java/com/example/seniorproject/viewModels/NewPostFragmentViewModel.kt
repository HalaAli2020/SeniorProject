package com.example.seniorproject.viewModels

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Utils.CheckCallback
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.DataSnapshot
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
    var boolsub : Boolean? = null



    private var postListener: PostListener? = null

    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            classSpinner = parent?.getItemAtPosition(position) as String
        }
    }

    fun editPost(){
        repository.editPost(crn!!, postKey!!, ctext!!, ctitle!!, textPost!!, titlePost!!, userID!!)
    }

    fun checkSubscriptions(classname : String, checkCallback: CheckCallback)
    {
        repository.checkSubscription(classname,object : PostRepository.FirebaseCallbacksubBool {
            override fun onStart() { TODO("not implemented") }
            override fun onFailure() { TODO("not implemented") }

            override fun onSuccess(data: DataSnapshot) {
                if (data.exists()) {
                    for (sub in data.children) {
                        if (sub.value == classname) {
                              boolsub = true
                              checkCallback.check(boolsub ?: false)
                              return
                        }
                    }
                    boolsub = false
                    checkCallback.check(boolsub ?: false)
                }

            }
        })
    }

    fun savePostToDatabase(title : String, text:String, CRN: String)  = repository.saveNewPost(title,text,CRN)


   fun saveNewImgPosttoUser(title : String, text:String, CRN: String, uri: Uri, imagePost : Boolean) = repository.saveNewImgPosttoUser(title,text,CRN,uri,imagePost)

}
