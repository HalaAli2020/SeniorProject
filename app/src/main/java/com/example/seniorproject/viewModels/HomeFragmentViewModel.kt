package com.example.seniorproject.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.data.interfaces.ListActivitycallback
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeFragmentViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {

    var p: MutableList<Post> = mutableListOf()

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getSubsP( call : ListActivitycallback)
    {
        val subs : MutableList<String> = mutableListOf()
        p = mutableListOf()

         var subjob = viewModelScope.launch(Dispatchers.IO) {
            val subF = repository.getUsersSubs()
            subF.buffer().collect(object : FlowCollector<String> {
                override suspend fun emit(value: String) {
                    subs.add(value)
                }
            })
             val flow = repository.getSubscribedPosts(subs)
             flow.buffer().collect(object : FlowCollector<Post>
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





