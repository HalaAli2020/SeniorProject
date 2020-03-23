package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.repositories.SearchRepo
import com.google.firebase.database.DataSnapshot
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: SearchRepo) : ViewModel() {
    var UsersSubs : MutableLiveData<MutableList<CRN>> = MutableLiveData()
    var Clist : MutableLiveData<MutableList<CRN>> = MutableLiveData()

    fun Search(query: String): CRN {
        var Result = CRN()
        repository.Search(query, object : FirebaseResult {
            override fun onFailure(message: String) {
                Log.d("onFailure", "On Failure called")
            }

            override fun onStart() {
                Log.d("Start", "Started Query")
            }

            override fun onSuccess(data: DataSnapshot, uid: String) {
                Result = separateResult(data, query)
            }
        })
        return Result
    }

    fun separateResult(data: DataSnapshot, query: String): CRN {
        var RList: MutableLiveData<MutableList<CRN>>
        var RE = CRN()
        var classResult = CRN()
        if (data.hasChild(query)) {
            var snap = data.child(query)
            classResult.let { result ->
                result.name = snap.key.toString()
            }
            return classResult
        } else {
            // RE  = CRN()
            RE.name = "ERROR in Query"
        }
        return RE

    }

    fun getallclasses() {
        var listC : MutableList<CRN> = mutableListOf()
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
                    var crn = CRN()

                    Log.d("key", x.key.toString())
                    crn.name = x.key.toString()
                    Log.d("crn.name", crn.name)
                    if (x.hasChild("SubList")) {
                        if (x.child("SubList").hasChild(uid))
                        {
                            crn.Subscribed = true
                            //Log.d("all classes", crn.name)
                        }


                    }
                    Log.d("all classes", crn.name)
                    listC.add(crn)
                }

            }


        })
        Clist.value = listC
    }
    fun sendlist() : MutableLiveData<MutableList<CRN>>
    {
        return Clist
    }
    fun addSub(crn : String)
    {
        repository.addUsersub(crn)
        for (x in Clist.value!!.iterator())
        {
            if(x.name.contentEquals(crn))
            {
                x.Subscribed = true
            }
        }

    }
    fun removeSub(crn : String)
    {
        repository.remUsersub(crn)
        for (x in Clist.value!!.iterator())
        {
            if(x.name.contentEquals(crn))
            {
                x.Subscribed = false
            }
        }


    }

    interface FirebaseResult {
        fun onStart()
        fun onSuccess(data: DataSnapshot, uid: String)
        fun onFailure(message: String)
    }
}