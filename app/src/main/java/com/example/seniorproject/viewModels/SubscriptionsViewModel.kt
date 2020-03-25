package com.example.seniorproject.viewModels

import androidx.lifecycle.ViewModel
import com.example.seniorproject.data.repositories.PostRepository
import javax.inject.Inject

private const val TAG = "MyLogTag"

class SubscriptionsViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {

    private var UsersSubs: MutableList<String>? = mutableListOf()

    init {
        getUserSub()
    }

    fun getUserSub(): MutableList<String>? {
        UsersSubs = repository.getsublist()

        return UsersSubs

    }


}