package com.example.seniorproject.data.repositories



import android.util.Log
import com.example.seniorproject.Utils.FlowCallback
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.viewModels.SearchViewModel
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchRepo @Inject constructor(private val Firebase: FirebaseData) {


    fun search(query : String, reply: SearchViewModel.FirebaseResult)
    {
        Firebase.usersSearch(query, reply)

    }
    fun getallclasses(listen : SearchViewModel.FirebaseResult)
    {
        Firebase.getallclasses(listen)
    }
    @InternalCoroutinesApi
    fun getClassco(className: String, callback: FlowCallback){
        Firebase.getClasses(object : PostRepository.FirebaseCallbackCRN {
            override fun onStart() {
            }

            override fun onFailure() {
            }

            override fun onSuccess(data: DataSnapshot) {
                val postdetail: Iterable<DataSnapshot> = data.child("Posts").children
                val savedPostsList: MutableList<Post> = mutableListOf()
                // var listCor : Flow<Post> = flow {
                for (n in postdetail) {
                    val newPost = Post()
                    newPost.let {

                        //Log.d("ACCESSING", newPost?.text)
                        it.title = n.child("title").getValue(String::class.java)
                        //Log.d("Community post", data.child("title").getValue(String::class.java))
                        it.text = n.child("text").getValue(String::class.java)
                        //newPost.author = pos.child("author").getValue(String::class.java)
                        //it.subject = className
                        Log.d("CRN", data.key!!)
                        it.subject = n.child("subject").getValue(String::class.java)
                        it.Ptime =  n.child("Timestamp").getValue(String::class.java)
                        it.key = n.child("key").getValue(String::class.java)
                        it.Ptime = n.child("Ptime").getValue(String::class.java)
                        it.Classkey = n.child("Classkey").getValue(String::class.java)
                        it.UserID = n.child("UserID").getValue(String::class.java)
                        it.author = n.child("author").getValue(String::class.java)
                        it.uri = n.child("uri").getValue(String::class.java)

                        savedPostsList.add(newPost)

                        // emit(newPost)
                    }
                }
                //  }
                val listCor : Flow<Post> = savedPostsList.asFlow()
                callback.onFlow(listCor)
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    listCor.collect {
                        Log.d("soupcollect", "post is $it")
                    }
                    val check = listCor.toList()
                    Log.d("souprepo", "start here")
                    for(item in check){
                        Log.d("souprepo", "post is $item")
                    }
                    Log.d("souprepo", "end here")
                }
                //when i convert listCor to list from here it returns only last post
                /*
                    var check = listCor.toList()
                    Log.d("soupfinal","start here")
                    for(item in check){
                        Log.d("souplist","post is $item")
                    }
                    Log.d("soupfinal","end here")
                }*/
                //i can collect in realtime fine here, but when i add each item to arraylist of posts on collect
                //and then loop inside the arraylist it returns only last post.
            }
        })
    }

    fun addUsersub(crn: String) {
        Firebase.addUserSUB(crn)
    }
    fun remUsersub(crn: String) {
        Firebase.removeUserSub(crn)
        Firebase.removeClassSub(crn)
    }





    companion object {
        @Volatile
        private var instance: SearchRepo? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: SearchRepo(firebasedata).also { instance = it }
            }
    }
}

