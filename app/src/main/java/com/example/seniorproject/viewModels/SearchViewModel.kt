package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.repositories.SearchRepo
import com.google.firebase.database.DataSnapshot
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: SearchRepo) : ViewModel() {

    fun Search(query: String): CRN {
        var Result = CRN()
        repository.Search(query, object : FirebaseResult {
            override fun onFailure(message: String) {
                Log.d("onFailure", "On Failure called")
            }

            override fun onStart() {
                Log.d("Start", "Started Query")
            }

            override fun onSuccess(data: DataSnapshot) {
                Result = separateResult(data, query)
            }
        })
        return Result
    }

    fun separateResult(data: DataSnapshot, query: String): CRN {
        var RList: MutableList<CRN>
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

    fun getallclasses() : MutableList<CRN> {
        var listC : MutableList<CRN> = mutableListOf()
        repository.getallclasses(object : FirebaseResult {
            override fun onFailure(message: String) {
                TODO("Not yet implemented")
            }

            override fun onStart() {
                TODO("Not yet implemented")
            }

            override fun onSuccess(data: DataSnapshot, uid: String) {
                for (x in data.children) {
                    var crn = CRN()
                    crn.name = x.key.toString()
                    if (x.hasChild("SubList")) {
                        if (x.child("SubList").hasChild(uid))
                        {
                            crn.Subscribed = true
                        }
                        else
                        {
                            continue
                        }

                    }
                    listC.add(crn)
                }
            }
        })
        return listC
    }

    interface FirebaseResult {
        fun onStart()
        fun onSuccess(data: DataSnapshot, uid: String)
        fun onFailure(message: String)
    }
}