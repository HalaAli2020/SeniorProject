package com.example.seniorproject.viewModels
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.data.interfaces.FirebaseCallbackItem
import com.example.seniorproject.data.interfaces.ListActivitycallback
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.toList
import javax.inject.Inject


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {

    var p: MutableList<Post> = mutableListOf()
    var currentUsername : String = "no username available"
    var live : MutableLiveData<MutableList<Post>> = MutableLiveData()
    init {
        //getSubsP()
    }
    fun clearLive()
    {
        live.value?.clear()
        p.clear()
    }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    //call : ListActivitycallback
    suspend  fun getSubsP( call : ListActivitycallback)
    {
        var subs : MutableList<String> = mutableListOf()
        p = mutableListOf()

        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {

            val subF = repository.getUsersSubs()
            subs = subF.toList() as MutableList<String>
        }
        viewModelScope.async (Dispatchers.IO) {
            val flow = repository.getSubscribedPosts(subs)
            flow.buffer().collect(object : FlowCollector<Post>
            {
                override suspend fun emit(value: Post) {
                    p.add(value)
                }

            })
        }.await()

             live.value = p
            //call.onCallback(p)

    }
    @InternalCoroutinesApi
    fun getSubsP2(call : ListActivitycallback)
    {
        val subs : MutableList<String> = mutableListOf()
        p = mutableListOf()
        var count : Long = 0
        var subjob = viewModelScope.launch(Dispatchers.IO) {
            repository.getsubsize()
            val subF = repository.getUsersSubs()
            subF.buffer().collect(object : FlowCollector<String> {
                override suspend fun emit(value: String) {
                    count++
                    val flow = repository.getSubscribedPosts2(value, count)
                    flow.buffer().collect(object : FlowCollector<Post> {
                        override suspend fun emit(value: Post) {
                            p.add(value)
                        }

                    })
                }
            })

            call.onCallback(p)
        }


    }

    //this returns a list of posts
    fun sendPosts() :MutableList<Post>{
        return p
    }

    //these functions call their corresponding functions in the repository
    //used in UserProfileActivity to get the current users username in real time
    fun fetchUsername(UserID: String, callback : EmailCallback){
        repository.fetchUsername(UserID, object : FirebaseCallbackItem {
            override fun onStart() {

            }

            override fun onFailure() {

            }

            override fun onMessage(data: DataSnapshot){
                val email = data.child("Username").getValue(String::class.java)
                currentUsername = email ?: "no email in success"
                callback.getEmail(currentUsername)
            }
        })
    }

    var user = repository.currentUser()

    fun readPhotoValue(useridm: String, callback: EmailCallback) {
        repository.readPhotoValue(useridm, callback)
    }




}





