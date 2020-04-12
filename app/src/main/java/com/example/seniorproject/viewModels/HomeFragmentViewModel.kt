package com.example.seniorproject.viewModels
import android.database.Observable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.MainForum.Adapters.HomeAdapter
import com.example.seniorproject.data.interfaces.listActivitycallback
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import io.reactivex.internal.operators.flowable.FlowableError
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine


class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {

    var p: MutableList<Post> = mutableListOf()

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getSubsP( call : listActivitycallback)
    {
        var subs : MutableList<String> = mutableListOf()
        p = mutableListOf()

         var Subjob = viewModelScope.launch(Dispatchers.IO) {
            var SubF = repository.getUsersSubs()
            SubF.buffer().collect(object : FlowCollector<String> {
                override suspend fun emit(value: String) {
                    subs.add(value)
                }
            })
             var Flow = repository.getSubscribedPosts(subs)
             Flow.buffer().collect(object : FlowCollector<Post>
             {
                 override suspend fun emit(value: Post) {
                     p.add(value)
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
    fun fetchCurrentUserName() = repository.fetchCurrentUserName()

    var user = repository.currentUser()




}





