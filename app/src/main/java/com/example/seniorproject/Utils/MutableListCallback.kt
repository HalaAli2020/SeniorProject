package com.example.seniorproject.Utils

import com.example.seniorproject.data.models.Post

//interface to grab list of posts via callback
interface MutableListCallback {
    fun onList(list: List<Post>)
}