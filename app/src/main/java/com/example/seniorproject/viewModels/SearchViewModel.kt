package com.example.seniorproject.viewModels



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.interfaces.FirebaseResult
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.repositories.SearchRepo
import com.google.firebase.database.DataSnapshot
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: SearchRepo) : ViewModel() {
    //var UsersSubs : MutableLiveData<MutableList<CRN>> = MutableLiveData()
    private var clist : MutableLiveData<MutableList<CRN>> = MutableLiveData()
    private var fullist : MutableLiveData<MutableList<CRN>> = MutableLiveData()


/*this function grabs all of the classes available for the search function and set it so the it will populate the full list */
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

/*this function sends the full list forward to the activity so it can be set to the adapter and used to filter the classes */
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


}