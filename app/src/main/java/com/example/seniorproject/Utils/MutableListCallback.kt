package com.example.seniorproject.Utils

import com.example.seniorproject.data.models.Post

interface MutableListCallback {
    fun onList(list: List<Post>)
}