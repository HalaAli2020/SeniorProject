package com.example.seniorproject.data.interfaces

import androidx.lifecycle.MutableLiveData
import com.example.seniorproject.data.models.*
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
interface FirebaseCallback {
    fun onCallback(list: MutableList<User>?)
}

interface FirebaseMessagseCallback {
    fun onCallback(list: MutableList<ChatMessage>?)
}

interface FirebaseRecentMessagseCallback {
    fun onCallback(list: List<LatestMessage>)
}

interface FirebaseCallbackPostC {
    fun onCallback(PostL: PostLiveData)
}

interface FirebaseCallbackCommentC {
    fun onCallback(CommentL: CommentLive)
}

interface FirebaseCallbackCommentFlow {
    fun onCallback(flow: Flow<Comment>)
}

interface FirebaseCallbackPostFlow {
    fun onCallback(flow: Flow<Post>)
}

interface FirebaseCallbackCRNC {
    fun onCallback(CRNL: MutableLiveData<MutableList<CRN>>)
}

interface FirebaseCallbackStringC {
    fun onCallback(subs: MutableList<String>)
}

interface FirebaseCallbackItemC {
    fun onCallback(Item: String)
}

interface FirebaseCallbackBoolC {
    fun onCallback(Item: Boolean)
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

interface FirebaseCallbackBool {

    fun onStart()
    fun onFailure()
    fun onSuccess(data: DataSnapshot): Boolean
}

interface FirebaseCallbackNoComments {
    fun onEmpty(nocomlist: Boolean)
}

interface FirebaseCallbackComment {
    fun onStart()
    fun onFailure()
    fun onSuccess(data: DataSnapshot)
}
interface FirebaseCallbackSubs {
    fun onFailure()
    fun onStart()
    fun onSuccess(data: List<String>)
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
    fun onMessage(data: DataSnapshot): String
}
interface FirebaseResult {
    fun onStart()
    fun onSuccess(data: DataSnapshot, uid: String)
    fun onFailure(message: String)
}
interface CommentListFromFlow {
    fun onList(list: List<Comment>)
}

interface PostListFromFlow {
    fun onList(list: List<Post>)
}