package com.example.seniorproject.viewModels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.Utils.CheckCallback
import com.example.seniorproject.data.interfaces.FirebaseCallbacksubBool
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.DataSnapshot
import javax.inject.Inject

class NewPostFragmentViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    var titlePost: String? = null
    var textPost: String? = null
    var crn: String? = null
    var postKey: String? = null
    var ctext: String? = null
    var ctitle: String? = null
    var userID: String? = null
    var bool = MutableLiveData<Boolean>()
    var boolsub : Boolean? = null

    //calls corresponding function from post repository takes binded variable information from edit xml file
    fun editPost(){
        if (titlePost.isNullOrBlank() || textPost.isNullOrBlank()){
            bool.value = false
            return
        }
        else
        {
            repository.editPost(crn!!, postKey!!, ctext!!, ctitle!!, textPost!!, titlePost!!, userID!!)
            bool.value = true
            return
        }
    }

    //takes classname and searches for a match in the users subscriptions
    fun checkSubscriptions(classname : String, checkCallback: CheckCallback)
    {
        repository.checkSubscription(classname,object : FirebaseCallbacksubBool {
            override fun onStart() { TODO("not implemented") }
            override fun onFailure() { TODO("not implemented") }

            override fun onSuccess(data: DataSnapshot) {
                if (data.exists()) {
                    for (sub in data.children) {
                        if (sub.value == classname) {
                              boolsub = true
                            //callback lets frontend know to make successful post toast message
                              checkCallback.check(boolsub ?: false)
                              return
                        }
                    }
                    //callback lets frontend know to make unsuccessful post toast message
                    boolsub = false
                    checkCallback.check(boolsub ?: false)
                }

            }
        })
    }

    //calls corresponding function from post repository
    fun savePostToDatabase(title : String, text:String, CRN: String)  = repository.saveNewPost(title,text,CRN)
    //calls corresponding function from post repository
   fun saveNewImgPosttoUser(title : String, text:String, CRN: String, uri: Uri, imagePost : Boolean) = repository.saveNewImgPosttoUser(title,text,CRN,uri,imagePost)

}
