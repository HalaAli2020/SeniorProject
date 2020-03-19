package com.example.seniorproject.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

private const val TAG = "MyLogTag"

class SubscriptionsViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {

    private var UsersSubs: MutableLiveData<MutableList<String>> = MutableLiveData()

    init {
        getUserSub()
    }

    fun getUserSub(): MutableLiveData<MutableList<String>> {
        UsersSubs = repository.getUserSub()!!

        return UsersSubs

    }


}