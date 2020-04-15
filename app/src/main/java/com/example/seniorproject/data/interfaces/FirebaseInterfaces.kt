package com.example.seniorproject.data.interfaces

import com.example.seniorproject.data.models.*
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

/* This is a file for all the interfaces used in the application for callbacks */
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
interface sizecall{
    fun onCallback(i : Long)
}

interface ListActivitycallback{
    fun onCallback(list : List<Post>)
}
interface FirebaseCallback {
    fun onCallback(list: MutableList<User>?)
}

interface FirebaseMessagseCallback {
    fun onCallback(list: MutableList<ChatMessage>?)
}

interface FirebaseRecentMessagseCallback {
    fun onCallback(list: List<LatestMessage>)
}


interface FirebaseCallbackCommentFlow {
    fun onCallback(flow: Flow<Comment>)
}

interface FirebaseCallbackPostFlow {
    fun onCallback(flow: Flow<Post>)
}

interface FirebaseCallbackPost {

    fun onStart()
    fun onFailure()
    fun onSuccess(data: DataSnapshot)
}

interface FirebaseCallbacksubBool {
    fun onStart()
    fun onFailure()
    fun onSuccess(data: DataSnapshot)
}

interface FirebaseCallbackNoComments {
    fun onEmpty(nocomlist: Boolean)
}

interface FirebaseCallbackComment {
    fun onStart()
    fun onFailure()
    fun onSuccess(data: DataSnapshot)
}

interface FirebaseCallbackCRN {
    fun onStart()
    fun onFailure()
    fun onSuccess(data: DataSnapshot)
}

interface FirebaseCallbackString {
    fun onStart()
    fun onFailure()
    fun onSuccess(data: DataSnapshot)
}

interface FirebaseCallbackItem {
    fun onStart()
    fun onFailure()
    fun onMessage(data: DataSnapshot)
}
interface FirebaseResult {
    fun onStart()
    fun onSuccess(data: DataSnapshot, uid: String)
    fun onFailure(message: String)
}

interface FirebaseCallbackUserListFlow {
    fun onFlow(flow: Flow<String>)
}

interface CommentListFromFlow {
    fun onList(list: List<Comment>)
}

interface PostListFromFlow {
    fun onList(list: List<Post>)
}