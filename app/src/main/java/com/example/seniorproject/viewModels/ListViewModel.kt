package com.example.seniorproject.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.example.seniorproject.data.models.CRN
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class ListViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    var listClasses: MutableLiveData<MutableList<CRN>> = repository.getClasses()
    var SubList : LiveData<MutableList<CRN>> = listClasses?.let {
        Transformations.map(it){ list ->
            Log.d("Transform", "Called")
            list

        }
    }
    //var obse : Observer<in MutableList<CRN>>? = null
     var UsersSubs : MutableLiveData<MutableList<String>>? = MutableLiveData()

    init {
        getClasses()
        getUserSub()
        //getClasses()




        /*while(listClasses.isEmpty() && UsersSubs!!.isEmpty())
        {
            Log.d("Loading data", "Loading data")
        }*/
        //combineSubs()

    }
    //: MutableLiveData<MutableList<CRN>>
    private fun getClasses()  {
        //listClasses.clear()

        listClasses = repository.getClasses()

    }
    private fun getUserSub()
    {
        UsersSubs = repository.getUserSub()

    }
     fun getSubs()
    {
       getClasses()
        getUserSub()
        //combineSubs()





    }
    fun check() : Boolean
    {
        return (listClasses.value.isNullOrEmpty() && UsersSubs?.value.isNullOrEmpty())
    }
    fun Getcomnosubs() = repository.getClasses()


    fun combineSubs()
    {
        /*while(UsersSubs.isNullOrEmpty())
        {

            Log.d("UserSUBs", "null")
            //return listClasses
        }*/
        if(listClasses.value.isNullOrEmpty() && UsersSubs?.value.isNullOrEmpty())
        {

            Log.d("Null", "one was null")
        }
        else
        {
            for(data in listClasses.value!!.iterator())
            {
                if(UsersSubs!!.value!!.contains(data.name))
                {
                    data.Subscribed = true
                    Log.d("combine", data.name)
                }

            }


        }
    }


    fun returnClasses(): MutableList<CRN>?{


        return SubList.value

    }
    fun addSub(crn : String)
    {
        repository.addUsersub(crn)
    }
    fun removeSub(crn : String)
    {
        repository.remUsersub(crn)
        UsersSubs = MutableLiveData()
        listClasses = MutableLiveData()
        getClasses()
        getUserSub()
        combineSubs()
    }
}