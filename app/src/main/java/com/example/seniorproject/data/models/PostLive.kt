package com.example.seniorproject.data.models

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


class PostLiveData : MutableLiveData<MutableList<Post>>()
{

    private val  ob = Observer<List<Post>> {

    }
    private val Listener = {

    }

    companion object {
        private lateinit var sInstance: PostLiveData

        @MainThread
        fun get(): PostLiveData {
            sInstance = if (::sInstance.isInitialized) sInstance else PostLiveData()
            return sInstance
        }
    }
}