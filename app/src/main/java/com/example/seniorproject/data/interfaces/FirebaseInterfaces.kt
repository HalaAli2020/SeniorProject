package com.example.seniorproject.data.interfaces

import com.example.seniorproject.data.models.Post
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface FirebaseValuecallback{
    fun onStart()
    fun onFailure()
    fun onSuccess(data : DataSnapshot)

}
interface FirebaseValue{
    fun onStart()
    fun onFailure()
    suspend fun onSuccess(data : DataSnapshot)
}

interface listActivitycallback{
    fun onCallback(list : List<Post>)
}