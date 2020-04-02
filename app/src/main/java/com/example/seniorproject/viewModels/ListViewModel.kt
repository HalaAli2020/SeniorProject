package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

class ListViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private var listClasses: MutableList<CRN> = mutableListOf()
    private var UsersSubs : MutableList<String>? = mutableListOf()
    init {
        getUserSub()
        getClasses()


    }

    private fun getClasses(){
        listClasses.clear()

    }
    private fun getUserSub()
    {


    }

    fun combineSubs()
    {
        if(UsersSubs == null)
        {
            Log.d("UserSUBs", "null")
        }


        for(data in listClasses)
        {
           if(UsersSubs!!.contains(data.name))
           {
               data.Subscribed = true
               Log.d("combine", data.name)
           }

        }

    }


    fun returnClasses(): MutableList<CRN>{

        //getClasses()

        combineSubs()
        return listClasses
    }
    fun addSub(crn : String)
    {
        repository.addUsersub(crn)
    }
    fun removeSub(crn : String)
    {
        repository.remUsersub(crn)
        UsersSubs = mutableListOf()
        listClasses = mutableListOf()
        getClasses()
        getUserSub()
        combineSubs()
    }
}