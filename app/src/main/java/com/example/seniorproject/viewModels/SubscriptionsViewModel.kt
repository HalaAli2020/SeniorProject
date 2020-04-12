package com.example.seniorproject.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject



class SubscriptionsViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {

    var usersSubs: MutableLiveData<MutableList<String>>? = MutableLiveData()

    init {
        getUserSub()
    }

    fun getUserSub(): MutableList<String>? {
        usersSubs = repository.getsublist2()

        return usersSubs!!.value
    }

    fun retsubs(): MutableList<String>?
    {
        return usersSubs!!.value
    }


}