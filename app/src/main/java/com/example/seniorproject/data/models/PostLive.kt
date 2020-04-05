package com.example.seniorproject.data.models

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


class PostLiveData : MutableLiveData<MutableList<Post>>()
{

    private val  ob = Observer<List<Post>> {

    }
    private val listener = {

    }



    override fun onActive() {



    }

    override fun onInactive() {

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