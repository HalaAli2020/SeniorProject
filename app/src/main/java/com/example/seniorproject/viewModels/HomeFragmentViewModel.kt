package com.example.seniorproject.viewModels
import android.database.Observable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.data.interfaces.listActivitycallback
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import io.reactivex.internal.operators.flowable.FlowableError
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine


class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {


    var posts: PostLiveData = PostLiveData()
    var p: MutableList<Post> = mutableListOf()
    var FlowP : Flow<Post>? = null


    init {

        //posts = repository.getSubscribedPosts()
    }


    fun getSubscribedPosts(): PostLiveData {
        //posts = repository.getSubscribedPosts()
        return posts
    }
    @InternalCoroutinesApi
    suspend fun getSubsP( call : listActivitycallback)
    {
        var subs : MutableList<String> = mutableListOf()
        var pots : MutableList<Post> = mutableListOf()

         var Subjob = viewModelScope.launch(Dispatchers.IO) {
            Log.d("Coroutine", "Launched")
            var SubF = repository.getUsersSubs()
            SubF.collect(object : FlowCollector<String> {
                override suspend fun emit(value: String) {
                    Log.d("SFlow", value)
                    subs.add(value)
                }
            })
             getPosts(subs)
        }


        //call.onCallback(p)
    }
    @InternalCoroutinesApi
    suspend fun getPosts(subs : List<String>)
    {
        viewModelScope.launch(Dispatchers.IO) {

            FlowP = repository.getSubscribedPosts(subs)
            FlowP!!.collect(object : FlowCollector<Post>
            {
                override suspend fun emit(value: Post) {
                    Log.d("PFlow" ,"${value.title}")
                    p.add(value)
                }

            })
            Log.d("post flow", "flow post")



        }

        //call.onCallback(p)
    }
    fun sendPosts() :MutableList<Post>{
        return p
    }




    fun fetchCurrentUserName() = repository.fetchCurrentUserName()

    var user = repository.currentUser()


    /*suspend fun getSubscribedPostCO(): PostLiveData = withContext(Dispatchers.IO) {
        posts = repository.getSubscribedPostsCO()
        return@withContext posts
    }*/
    fun returnSubscribedpost() : PostLiveData
    {
        return  posts
    }


}





