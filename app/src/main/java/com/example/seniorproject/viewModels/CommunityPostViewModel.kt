package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.example.seniorproject.Utils.*
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.models.PostLiveData
import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class CommunityPostViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private var listClassesco: Flow<Post> = flow { }
    private var listpostclass: MutableList<Post> = mutableListOf()
    private var babylistnum: List<Post> = mutableListOf()
    private var listofposts: List<Post> = mutableListOf()
    private var postliveData: MutableLiveData<MutableList<Post>>? = null
    private var livepostclass: LiveData<Post>? = null
    private var listClasses: PostLiveData? = null
    private lateinit var className: String
    //private lateinit var className: String

    @InternalCoroutinesApi
    fun getClassesco(className: String, callback: MutableListCallback){
        repository.getClassPostsco(className, object: FlowCallback{
            override fun onFlow(flowCallback: Flow<Post>) {
                viewModelScope.launch {
                    //converting Flow with .toList and .toSet results in storing only the last value of the flow
                    //.asLiveData extension function does not work: unresolved reference
                    //try collectIndexed index next, value to list index value
                    //Log.d("soupvm", "before flow")
                  /*var testfail = flowCallback.toList()
                    var value = testfail[0]
                    var value2 = testfail[5]
                    Log.d("soupvm", "start of $value and $value2")*/
                   /*flowCallback.map {
                       perform(it)
                   }.collect {
                       it -> Log.d("soupvm", "added post $it")
                   }*/


                    listofposts = flowCallback.toList()
                   /* for(item in listofposts){
                        Log.d("soupvm", "item is $item")
                    }*/
                    callback.onList(listofposts)

                   // postliveData = MutableLiveData(listpostclass)
                    //Log.d("soupvm", "after flow")
                    //val size = listpostclass.size
                    //Log.d("soupvm", "size is $size")
                    //Log.d("soupvm", "start of list collection")
                    /*for(item in listpostclass){
                        Log.d("soupvm", "item in list is $item")
                    }*/
                    //Log.d("soupvm", "end of list collection")
                    /* var postlive = flowCallback.asLiveData()
                     Log.d("soupvm", "flow to live data is $postlive")
                     var plive = PostLiveData
                     plive = postlive as PostLiveData.Companion
                     Log.d("soupvm", "live data to postlivedata is $postlive")
                     livepostclass = postlive
                     var listpclass2: ArrayList<Post> = arrayListOf()
                     flowCallback.onEach {
                             value -> listpclass.add(value)
                             Log.d("souponeach", "post list value is $value")
                     }
                    flowCallback.collect{
                             values -> listpclass.addAll(listOf(values))
                             //value -> listpclass.add(value)
                             //Log.d("soupvm", "post list value is $value")
                     }
                     var postsize = listpclass.size
                     //var postsize2 = listpclass2.size
                     for(item in listpclass){
                         Log.d("soupvm", "post list value is $item")
                     }*/

                  //  listpostclass = listpclass
                }
            }
        })
        //listClassesco = repository.getClassPostsco(className)
        /* listClassesco.buffer().collect{
             values -> Log.d("soupvmb", "value is $values")
         }
         Log.d("soupvm", "dummy end")*/
        // this works but you have to refresh twice to see post values in logcat
    }

   /* @InternalCoroutinesApi
    fun getpostsforclass(className: String) : Flow<Post>{
        var listCor = repository.getpostsforclass(className)
        var uiScope = CoroutineScope(Dispatchers.IO)
        /*uiScope.launch {
            listCor.collect {
                    values -> Log.d("soupvm","post is $values")
            }
            /*repository.numFlow().collect {
                    values -> Log.d("soupnumflow","try me $values")
            }*/
        }*/
        return listCor
    }*/

  /*  @InternalCoroutinesApi
    fun returnClassPostsco(className: String): Flow<Post> {
        getClassesco(className)
        return listClassesco
    }*/

    /*private fun getClasses(className: String){

    fun returnClassPosts(className: String): PostLiveData {
        getClasses(className)
        return listClasses!!
    }*/

    fun blockUser(UserID: String){
        repository.blockUser(UserID)
    }

    fun reportUserPost(accusedID: String, complaintext: String, crn: String, classkey: String){
        repository.reportUserPost(accusedID, complaintext, crn, classkey)
    }





}