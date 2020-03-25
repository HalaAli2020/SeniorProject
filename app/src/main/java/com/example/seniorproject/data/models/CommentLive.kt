package com.example.seniorproject.data.models

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class CommentLive : MutableLiveData<MutableList<Comment>>()
{

    private val  ob = Observer<MutableList<Comment>> {

    }
    private val Listener = {

    }




    companion object {
        private lateinit var sInstance: CommentLive

        @MainThread
        fun get(): CommentLive {
            sInstance = if (::sInstance.isInitialized) sInstance else CommentLive()
            return sInstance
        }
    }
}