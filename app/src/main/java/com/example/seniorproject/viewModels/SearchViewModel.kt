package com.example.seniorproject.viewModels



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seniorproject.Utils.FlowCallback
import com.example.seniorproject.Utils.MutableListCallback
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.SearchRepo
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: SearchRepo) : ViewModel() {
    //var UsersSubs : MutableLiveData<MutableList<CRN>> = MutableLiveData()
    var clist : MutableLiveData<MutableList<CRN>> = MutableLiveData()
    var fullist : MutableLiveData<MutableList<CRN>> = MutableLiveData()

    fun search(query: String): CRN {
        var result = CRN()
        repository.search(query, object : FirebaseResult {
            override fun onFailure(message: String) {
                Log.d("onFailure", "On Failure called")
            }

            override fun onStart() {
                Log.d("Start", "Started Query")
            }

            override fun onSuccess(data: DataSnapshot, uid: String) {
                result = separateResult(data, query)
            }
        })
        return result
    }

    fun separateResult(data: DataSnapshot, query: String): CRN {
        val rE = CRN()
        val classResult = CRN()
        if (data.hasChild(query)) {
            val snap = data.child(query)
            classResult.name = snap.key.toString()
            return classResult
        } else {
            // RE  = CRN()
            rE.name = "ERROR in Query"
        }
        return rE

    }

    fun getallclasses() {
        val listC : MutableList<CRN> = mutableListOf()
        repository.getallclasses(object : FirebaseResult {
            override fun onFailure(message: String) {
                Log.d("Failure", "onFailure called")
            }

            override fun onStart() {
                Log.d("started", "onstarted called")
            }

            override fun onSuccess(data: DataSnapshot, uid: String) {
                Log.d("onSuccess", " called")

                for (x in data.children) {
                    val crn = CRN()

                    Log.d("key", x.key.toString())
                    crn.name = x.key.toString()
                    Log.d("crn.name", crn.name)
                    if (x.hasChild("SubList")) {
                        val sub = x.child("SubList").children
                        for (s in sub)
                        {
                            if(uid == s.value.toString())
                            {
                                crn.subscribed = true
                            }
                        }



                    }
                    Log.d("all classes", crn.name)
                    listC.add(crn)
                }

            }


        })
        fullist.value = listC
    }
   /* fun getClassesco(className: String, callback: MutableListCallback){
        repository.getClassPostsco(className, object: FlowCallback {
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
    }*/

    fun sendlistf() : MutableLiveData<MutableList<CRN>>
    {
        clist.value = fullist.value

        return clist
    }
    fun addSub(crn : String)
    {
        repository.addUsersub(crn)
        for (x in clist.value!!.iterator())
        {
            if(x.name.contentEquals(crn))
            {
                x.subscribed = true
            }
        }

    }
    fun removeSub(crn : String)
    {
        repository.remUsersub(crn)
        for (x in clist.value!!.iterator())
        {
            if(x.name.contentEquals(crn))
            {
                x.subscribed = false
            }
        }


    }

    interface FirebaseResult {
        fun onStart()
        fun onSuccess(data: DataSnapshot, uid: String)
        fun onFailure(message: String)
    }
}