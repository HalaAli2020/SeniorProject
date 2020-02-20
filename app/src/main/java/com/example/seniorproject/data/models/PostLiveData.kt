package com.example.seniorproject.data.models

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.seniorproject.data.Firebase.FirebaseData

import com.example.seniorproject.data.repositories.PostRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowViaChannel




class PostLiveData : MutableLiveData<MutableList<Post>>() {
    fun <T> LiveData<T>.asFlow() = com.example.seniorproject.data.models.flowViaChannel<T?> {
        it.offer(value)
        val observer = Observer<T> { t -> it.offer(t) }
        observeForever(observer)
        it.invokeOnClose {
            removeObserver(observer)
        }
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


//class PostLiveData : MutableLiveData<MutableList<Post>>()
/*class PostLiveData : MutableLiveData<MutableList<Post>>() {



    //@ExperimentalCoroutinesApi
    /*fun <Post> LiveData<Post>.asFlow() = flowViaChannel<Post?> {
        it.offer(value)
        val observer = Observer<Post>{t -> it.offer(t)}
            observeForever(observer)
        it.invokeOnClose { removeObserver(observer) }

    }*/

    fun <Post> LiveData<Post>.asFlow() = channelFlow<PostRepository?> {
        it.offer(value)
        val observer = Observer<Post>{t -> it.offer(t)}
        observeForever(observer)
        it.invokeOnClose { removeObserver(observer) }

    awaitClose()
    }


    //creates cold stream




   /* private val  ob = Observer<List<Post>>{

    }
    private val Listener = {

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
    }*/
}*/