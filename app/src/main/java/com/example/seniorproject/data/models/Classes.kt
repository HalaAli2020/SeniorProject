package com.example.seniorproject.data.models

import javax.security.auth.Subject

class CRN(var name : String)
{
    constructor(): this("")
    var SUBLIST : List<String>? = listOf()
    var Subscribed : Boolean = false
    var PostList : PostLiveData = PostLiveData()
    var Subject : String? = null




}