package com.example.seniorproject.Utils

import androidx.lifecycle.LiveData
import com.example.seniorproject.data.models.Post

interface MutableListCallback {
    fun onList(list: List<Post>)
}