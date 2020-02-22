package com.example.seniorproject.data.repositories

import android.util.Log
import com.epam.coroutinecache.api.CoroutinesCache
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.cache.CacheProviders
import com.example.seniorproject.data.models.Post
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Tag
import com.squareup.okhttp.*
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
//class PostRepository  @Inject constructor(private val Firebase: FirebaseData, private val cache: CoroutinesCache){
class PostRepository  @Inject constructor(private val Firebase: FirebaseData){

//private val cacheProviders: CacheProviders = cache.using(CacheProviders::class.java)
    //fun getData(): Flow<PostLiveData> = cacheProviders.getData()


    /*fun networkCallFromJsonFile(){
        val url= "https://university-social-media.firebaseio.com/"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call?, response: Response?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            }

            override fun onFailure(request: Request?, e: IOException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("NETWORK_ERROR","Network request has failed to run")
            }
        })
    }*/

    fun saveNewPost(Title: String, Text: String) = Firebase.saveNewPost(Title, Text)

    fun getSavedPosts() = Firebase.getSavedPost()

    fun currentUser() = Firebase.CurrentUser()

    fun fetchCurrentUserName() = Firebase.fetchCurrentUserName()

    companion object {
        @Volatile
        private var instance: PostRepository? = null

        fun getInstance(firebasedata: FirebaseData) =
            instance ?: synchronized(this) {
                instance ?: PostRepository(firebasedata).also { instance = it }
            }
    }
}